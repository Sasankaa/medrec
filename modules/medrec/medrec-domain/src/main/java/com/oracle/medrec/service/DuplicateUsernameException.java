package com.oracle.medrec.service;

import com.oracle.medrec.MedRecException;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class DuplicateUsernameException extends MedRecException {

  private static final long serialVersionUID = 1L;

  private String username;

  public DuplicateUsernameException(String username) {
    super("The username '" + username + "' already exists");
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
