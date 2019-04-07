package com.oracle.physician;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class PhysicianSystemException extends RuntimeException {

  public PhysicianSystemException() {
  }

  public PhysicianSystemException(String message) {
    super(message);
  }

  public PhysicianSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public PhysicianSystemException(Throwable cause) {
    super(cause);
  }
}
