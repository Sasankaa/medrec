package com.oracle.medrec.common.core;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class SystemException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public SystemException() {
  }

  public SystemException(String message) {
    super(message);
  }

  public SystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public SystemException(Throwable cause) {
    super(cause);
  }
}
