/**
 * @author Copyright (c) 2008, 2017, Oracle and/or its affiliates. All rights reserved.
 *
 */
package com.oracle.medrec.web.login;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.validation.constraints.NotNull;

/**
 * CDI Model Bean Class.
 */
@Model
public class Login {

  @NotNull
  private String username;

  @NotNull
  private String password;

  @Produces
  @Username
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Produces
  @Password
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
