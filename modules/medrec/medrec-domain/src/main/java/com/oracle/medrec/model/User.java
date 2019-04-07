package com.oracle.medrec.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//import org.hibernate.validator.constraints.Email;

/**
 * Base class defining the stuff common to any types of users.
 * <p/>
 * In current MedRec, each user only has one role identified by its class type.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@MappedSuperclass
public abstract class User extends VersionedEntity {

  private static final long serialVersionUID = -1847921044253901787L;

  @NotNull
  @Size(min = 1, max = 60)
  @Column(unique = true)
  private String username;

  @NotNull
  @Size(min = 6, max = 20)
  private String password;

  @NotNull
  @Size(min = 3, max = 60)
  @Column(unique = true)
  private String email;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
    setUsername(email);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    if (!email.equals(user.email)) {
      return false;
    }
    if (!password.equals(user.password)) {
      return false;
    }
    if (!username.equals(user.username)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = username.hashCode();
    result = 31 * result + password.hashCode();
    result = 31 * result + email.hashCode();
    return result;
  }
}
