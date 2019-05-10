/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
 */
package io.helidon.medrec.files;

/**
 * Something failed in the file processing.
 */
public class FilesException extends RuntimeException {
    public FilesException(String message) {
        super(message);
    }

    public FilesException(String message, Throwable cause) {
        super(message, cause);
    }
}
