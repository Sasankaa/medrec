package com.oracle.medrec.common.messaging;

import com.oracle.medrec.common.core.SystemException;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MessageException extends SystemException {

  private static final long serialVersionUID = 1L;

  public MessageException() {
  }

  public MessageException(String message) {
    super(message);
  }

  public MessageException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageException(Throwable cause) {
    super(cause);
  }
}
