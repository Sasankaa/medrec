package com.oracle.medrec.common.mail;

import com.oracle.medrec.common.core.SystemException;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MailException extends SystemException {

  private static final long serialVersionUID = 1L;

  public MailException() {
  }

  public MailException(String message) {
    super(message);
  }

  public MailException(String message, Throwable cause) {
    super(message, cause);
  }

  public MailException(Throwable cause) {
    super(cause);
  }
}
