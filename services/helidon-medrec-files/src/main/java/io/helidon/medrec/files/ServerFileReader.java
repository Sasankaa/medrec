/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
 */
package io.helidon.medrec.files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import io.helidon.common.http.DataChunk;
import io.helidon.common.reactive.Flow;

/**
 * Flow.Publisher that reads data from a file.
 */
public class ServerFileReader implements Flow.Publisher<DataChunk> {
    private static final Logger LOGGER = Logger.getLogger(ServerFileReader.class.getName());

    private static final int BUFFER_SIZE = 4096;

    private final ExecutorService executorService;
    private final Path filePath;


    ServerFileReader(ExecutorService executorService, Path filePath) {
        this.executorService = executorService;
        this.filePath = filePath;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super DataChunk> subscriber) {
        LinkedBlockingQueue<Long> requestQueue = new LinkedBlockingQueue<>();
        AtomicBoolean cancelled = new AtomicBoolean();

        CompletableFuture<FileChannel> channelFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                channelFuture.complete(FileChannel.open(filePath, StandardOpenOption.READ));
            } catch (IOException e) {
                channelFuture.completeExceptionally(e);
            }
        });

        channelFuture.thenAccept(theChannel -> {
            // channel is open, we can start reading
            subscriber.onSubscribe(new Flow.Subscription() {
                @Override
                public void request(long n) {
                    requestQueue.add(n);
                }

                @Override
                public void cancel() {
                    cancelled.set(true);
                }
            });

            // block a thread until fully written or cancelled
            executorService.submit(() -> {
                ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
                try (FileChannel channel = theChannel) {
                    while (!cancelled.get()) {
                        Long nextElement;
                        try {
                            nextElement = requestQueue.poll(10, TimeUnit.MINUTES);
                        } catch (InterruptedException e) {
                            LOGGER.severe("Interrupted while polling for requests, terminating file read on: " + filePath);
                            subscriber.onError(e);
                            break;
                        }
                        if (nextElement == null) {
                            LOGGER.severe("No data requested for 10 minutes, terminating file read on: " + filePath);
                            subscriber.onError(new TimeoutException("No data requested in 10 minutes"));
                            break;
                        }

                        for (long i = 0; i < nextElement; i++) {
                            int bytes = channel.read(buffer);
                            if (bytes < 0) {
                                subscriber.onComplete();
                                return;
                            }

                            if (bytes > 0) {
                                // copying the bytes, as the DataChunk may be
                                // cached before being written over the wire
                                // simple implementation - we should also cache the
                                // buffers here
                                byte[] theBytes = new byte[bytes];
                                buffer.flip();
                                buffer.get(theBytes, 0, bytes);
                                subscriber.onNext(DataChunk.create(theBytes));
                                buffer.clear();
                            }

                            if (cancelled.get()) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.severe("Exception while reading a file: " + filePath);
                    subscriber.onError(e);
                }
            });
        }).exceptionally(throwable -> {
            subscriber.onError(throwable);
            return null;
        });
    }
}
