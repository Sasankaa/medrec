package com.oracle.medrec.common.messaging;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface PayloadHandler<T> {

  void handlePayload(T payload);
}
