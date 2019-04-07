package com.oracle.medrec.common.persistence;

import com.oracle.medrec.common.core.SystemException;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class PersistenceException extends SystemException {

  private static final long serialVersionUID = 1L;

  public PersistenceException() {
  }

  public PersistenceException(String message) {
    super(message);
  }

  public PersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public PersistenceException(Throwable cause) {
    super(cause);
  }
}
