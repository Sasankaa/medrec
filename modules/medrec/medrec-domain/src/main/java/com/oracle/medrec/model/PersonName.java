package com.oracle.medrec.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Embeddable
public class PersonName implements Serializable {

  private static final long serialVersionUID = 5610658213331393425L;

  @NotNull
  @Size(min = 1, max = 60)
  private String firstName;

  @NotNull
  @Size(min = 1, max = 60)
  private String lastName;

  @Size(min = 1, max = 60)
  private String middleName;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(lastName);
    builder.append(" ");
    if (middleName != null) {
      builder.append(middleName);
      builder.append(" ");
    }
    builder.append(lastName);
    return builder.toString();
  }
}
