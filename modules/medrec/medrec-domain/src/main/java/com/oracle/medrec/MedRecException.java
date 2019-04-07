package com.oracle.medrec;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MedRecException extends Exception {

  private static final long serialVersionUID = 1L;

  public MedRecException() {
    super();
  }

  public MedRecException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public MedRecException(String message) {
    super(message);
  }

  public MedRecException(Throwable throwable) {
    super(throwable);
  }
}
