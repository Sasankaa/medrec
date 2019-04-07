package com.oracle.medrec.common.core;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class ApplicationException extends Exception {

  private static final long serialVersionUID = 1L;

  public ApplicationException() {
  }

  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApplicationException(Throwable cause) {
    super(cause);
  }
}
