package com.oracle.medrec.common.naming;

import com.oracle.medrec.common.core.SystemException;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class NamingException extends SystemException {

  private static final long serialVersionUID = 1L;

  public NamingException() {
  }

  public NamingException(String message) {
    super(message);
  }

  public NamingException(String message, Throwable cause) {
    super(message, cause);
  }

  public NamingException(Throwable cause) {
    super(cause);
  }
}
