package com.oracle.medrec;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MedRecSystemException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MedRecSystemException() {
    super();
  }

  public MedRecSystemException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public MedRecSystemException(String message) {
    super(message);
  }

  public MedRecSystemException(Throwable throwable) {
    super(throwable);
  }
}
