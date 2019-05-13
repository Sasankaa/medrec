/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.medrec.files;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.helidon.common.CollectionsHelper;
import io.helidon.common.configurable.ThreadPoolSupplier;
import io.helidon.common.http.Http;
import io.helidon.common.http.MediaType;
import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import static io.helidon.config.ConfigMappers.toPath;

/**
 * A simple service to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe:
 * curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object
 */

public class FilesService implements Service {
    private static final Logger LOGGER = Logger.getLogger(FilesService.class.getName());
    // 100 MB
    private static final Long DEFAULT_MAX_LENGTH = 100L * 1024 * 1024;
    private static final String DEFAULT_STORAGE_PATH = "./storage";
    public static final String DEFAULT_MEDIA_TYPE = "application/octet-stream";

    private final Map<String, FileMetadata> metadataCache = new ConcurrentHashMap<>();

    /**
     * Configurable root of the storage.
     */
    private final Path storageRoot;
    private final long maxSizeBytes;
    private final ExecutorService executorService;

    FilesService(Config config) {
        this.storageRoot = config.get("storage-path")
                .as(Path.class)
                .orElseGet(() -> Paths.get(DEFAULT_STORAGE_PATH));

        this.executorService = config.get("executor-service")
                // create from config
                .as(ThreadPoolSupplier::create)
                // create using defaults
                .orElseGet(ThreadPoolSupplier::create)
                .get();

        this.maxSizeBytes = config.get("max-size-mb")
                .asLong()
                // megabytes
                .map(size -> size * 1024 * 1024)
                .orElse(DEFAULT_MAX_LENGTH);
    }

    /**
     * A service registers itself by updating the routine rules.
     * @param rules the routing rules.
     */
    @Override
    public void update(Routing.Rules rules) {
        // ensure directory
        if (Files.exists(storageRoot)) {
            if (!Files.isDirectory(storageRoot)) {
                throw new FilesException("Storage root exists, yet it is not a directory: " + storageRoot.toAbsolutePath());
            }
        } else {
            try {
                Files.createDirectories(storageRoot);
            } catch (IOException e) {
                throw new FilesException("Failed to create storage root directory: " + storageRoot.toAbsolutePath(), e);
            }
        }

        rules.get("/{file_name}", this::getFile)
                .head("/{file_name}", this::headFile)
                .put("/{file_name}", this::createFile)
                .post("/{file_name}", this::updateFile)
                .delete("/{file_name}", this::deleteFile);
    }

    private void headFile(ServerRequest request, ServerResponse response) {
        // make sure the file exists
        String fileName = request.path().param("file_name");
        String encodedFileName = encode(fileName);
        Path filePath = storageRoot.resolve(encodedFileName);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            executorService.submit(() -> {
                FileMetadata metadata = FileMetadata.load(filePath);
                response.headers().add(Http.Header.CONTENT_LENGTH, size(filePath));
                response.headers().add(Http.Header.CONTENT_TYPE, metadata.mediaType);
                response.send();
            });
        } else {
            response.status(Http.Status.NOT_FOUND_404);
            response.send("File not found.");
        }
    }

    private void getFile(ServerRequest request, ServerResponse response) {
        String fileName = request.path().param("file_name");
        String encodedFileName = encode(fileName);
        Path filePath = storageRoot.resolve(encodedFileName);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            executorService.submit(() -> {
                FileMetadata metadata = FileMetadata.load(filePath);
                response.headers().add(Http.Header.CONTENT_TYPE, metadata.mediaType);
                response.send(new ServerFileReader(executorService, filePath));
            });

        } else {
            response.status(Http.Status.NOT_FOUND_404);
            response.send("File not found.");
        }
    }

    private void deleteFile(ServerRequest request, ServerResponse response) {
        String fileName = request.path().param("file_name");
        String encodedFileName = encode(fileName);
        Path filePath = storageRoot.resolve(encodedFileName);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            // delete file and metadata
            executorService.submit(() -> {
                try {
                    Path metaPath = FileMetadata.load(filePath).metadataPath();
                    Files.delete(metaPath);
                    Files.delete(filePath);
                    response.status(Http.Status.ACCEPTED_202);
                    response.send();
                } catch (Exception e) {
                    response.status(Http.Status.INTERNAL_SERVER_ERROR_500);
                    response.send("Server failed to process the request");
                    LOGGER.log(Level.WARNING, "Failed to delete file: " + filePath.toAbsolutePath(), e);
                }
            });
        } else {
            response.status(Http.Status.NOT_FOUND_404);
            response.send("File not found");
        }
    }

    private void createFile(ServerRequest request, ServerResponse response) {
        String fileName = request.path().param("file_name");
        String encodedFileName = encode(fileName);
        Path filePath = storageRoot.resolve(encodedFileName);
        String mediaType = request.headers().contentType().map(MediaType::toString).orElse(DEFAULT_MEDIA_TYPE);

        if (Files.exists(filePath)) {
            response.headers()
                    .add(Http.Header.ALLOW, CollectionsHelper.listOf("GET", "HEAD", "DELETE"));
            response.status(Http.Status.METHOD_NOT_ALLOWED_405);
            response.send("File already exists");
        } else {
            FileMetadata metadata = new FileMetadata(fileName, encodedFileName, filePath, mediaType);
            request.content().subscribe(new ServerFileWriter(response,
                                                             metadata,
                                                             executorService,
                                                             maxSizeBytes));
        }
    }

    private void updateFile(ServerRequest request, ServerResponse response) {
        String fileName = request.path().param("file_name");
        String encodedFileName = encode(fileName);
        Path filePath = toPath(fileName);
        String mediaType = request.headers().contentType().map(MediaType::toString).orElse(DEFAULT_MEDIA_TYPE);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            FileMetadata metadata = new FileMetadata(fileName, encodedFileName, filePath, mediaType);
            request.content().subscribe(new ServerFileWriter(response,
                                                             metadata,
                                                             executorService,
                                                             maxSizeBytes));
        } else {
            response.headers()
                    .add(Http.Header.ALLOW, CollectionsHelper.listOf("PUT"));
            response.status(Http.Status.METHOD_NOT_ALLOWED_405);
            response.send("File does not exist");
        }
    }

    private String size(Path filePath) {
        try {
            return String.valueOf(Files.size(filePath));
        } catch (IOException e) {
            throw new FilesException("Failed to get size of file", e);
        }
    }

    private String encode(String fileName) {
        return Base64.getUrlEncoder().encodeToString(fileName.getBytes());
    }

    static final class FileMetadata {
        private final String mediaType;
        private final String fileName;
        private final String encodedFileName;
        private final Path filePath;

        FileMetadata(String fileName,
                     String encodedFileName,
                     Path filePath,
                     String mediaType) {

            this.fileName = fileName;
            this.encodedFileName = encodedFileName;
            this.filePath = filePath;
            this.mediaType = mediaType;
        }

        public void write(Path metadata) {
            Properties props = new Properties();
            props.setProperty("media-type", mediaType);
            // not encoded file name
            props.setProperty("file-name", fileName);
            // full path to the file
            props.setProperty("file-path", filePath.toAbsolutePath().toString());
            // file name encoded
            props.setProperty("file-name-encoded", encodedFileName);

            try (Writer w = Files.newBufferedWriter(metadata, StandardOpenOption.CREATE)) {
                props.store(w, "metadata");
            } catch (IOException e) {
                throw new FilesException("Failed to write file metadata", e);
            }
        }

        String fileName() {
            return fileName;
        }

        Path metadataPath() {
            return filePath.getParent().resolve(filePath.getFileName() + ".meta");
        }

        public static FileMetadata load(Path filePath) {
            Path metadata = filePath.getParent().resolve(filePath.getFileName() + ".meta");
            if (Files.exists(metadata)) {
                Properties props = new Properties();
                try (Reader r = Files.newBufferedReader(metadata)) {
                    props.load(r);
                    return new FileMetadata(
                            props.getProperty("file-name"),
                            props.getProperty("file-name-encoded"),
                            Paths.get(props.getProperty("file-path")),
                            props.getProperty("media-type"));
                } catch (IOException e) {
                    throw new FilesException("Failed to read file metadata", e);
                }
            } else {
                throw new FilesException("Metadata not found");
            }
        }

        public Path filePath() {
            return filePath;
        }
    }

}
