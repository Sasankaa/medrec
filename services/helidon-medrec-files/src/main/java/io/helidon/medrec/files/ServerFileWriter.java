/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
 */
package io.helidon.medrec.files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.helidon.common.http.DataChunk;
import io.helidon.common.http.Http;
import io.helidon.common.reactive.Flow;
import io.helidon.webserver.ServerResponse;

/**
 * Writes a file from http entity to local file system.
 */
public class ServerFileWriter implements Flow.Subscriber<DataChunk> {
    private static final Logger LOGGER = Logger.getLogger(ServerFileWriter.class.getName());

    private final BlockingQueue<Data> chunkQueue = new LinkedBlockingQueue<>();
    private final CompletableFuture<Boolean> contentWrittenFuture = new CompletableFuture<>();
    private final ServerResponse response;
    private final FilesService.FileMetadata metadata;
    private final ExecutorService executorService;
    private final long maxSizeBytes;

    private long requested;
    private long bytesWritten;
    private FileChannel channel;
    private Path tmpPath;

    ServerFileWriter(ServerResponse response,
                     FilesService.FileMetadata metadata,
                     ExecutorService executorService,
                     long maxSizeBytes) {
        this.response = response;
        this.metadata = metadata;
        this.executorService = executorService;
        this.maxSizeBytes = maxSizeBytes;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        CompletableFuture<Void> initialized = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                tmpPath = Files.createTempFile("large-file", ".tmp");
                channel = FileChannel.open(tmpPath, StandardOpenOption.WRITE);
                requested = 32;
                subscription.request(32);
                initialized.complete(null);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to open channel to temp file for new file", e);
                sendError("Failed to write file " + metadata.fileName());
                subscription.cancel();
                initialized.completeExceptionally(e);
            }
        });

        initialized.thenAccept(nothing -> {
            try (FileChannel theChannel = channel) {
                processRequestEntity(subscription);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to write file: " + metadata.fileName(), e);
            }
        });
    }

    private void processRequestEntity(Flow.Subscription subscription) throws InterruptedException, IOException {
        while (true) {
            Data dataHolder = chunkQueue.poll(10, TimeUnit.MINUTES);
            if (null == dataHolder) {
                subscription.cancel();
                LOGGER.severe("No data received for 10 minutes for file: " + metadata.fileName());
                sendError("No data received from request for 10 minutes");
                return;
            }
            if (dataHolder.terminal) {
                break;
            }
            ByteBuffer data = dataHolder.theChunk.data();
            bytesWritten += data.remaining();
            if (bytesWritten > maxSizeBytes) {
                subscription.cancel();
                LOGGER.severe("Too big file: " + metadata.fileName());

                return;
            }
            channel.write(data);
            dataHolder.theChunk.release();
            requested--;
            if (requested == 16) {
                requested = 32;
                subscription.request(16);
            }
        }
        contentWrittenFuture.complete(true);
    }

    private void sendError(String message) {
        response.status(Http.Status.INTERNAL_SERVER_ERROR_500);
        response.send(message);
    }

    @Override
    public void onNext(DataChunk dataChunk) {
        chunkQueue.add(new Data(dataChunk));
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.log(Level.SEVERE, "Failed to write file", throwable);
        sendError("Failed to write file: " + metadata.fileName());
    }

    @Override
    public void onComplete() {
        chunkQueue.add(new Data());
        contentWrittenFuture.thenAccept(isWritten -> {
            try {
                // write metadata
                metadata.write(metadata.metadataPath());
                // move the file to its final position
                Files.move(tmpPath, metadata.filePath(), StandardCopyOption.REPLACE_EXISTING);
                response.send();
            } catch (IOException e) {
                sendError("Failed to move files to final location");
                LOGGER.log(Level.SEVERE, "Failed to create metadata or move file", e);
            }
        });
    }

    private static final class Data {
        private boolean terminal = false;
        private DataChunk theChunk;

        private Data() {
            terminal = true;
        }

        private Data(DataChunk theChunk) {
            this.theChunk = theChunk;
        }
    }
}
