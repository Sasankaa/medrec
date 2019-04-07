package com.oracle.medrec.model;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

/**
 * Base class defining properties common to any regular users (not including
 * administrators).
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@MappedSuperclass
public abstract class RegularUser extends User {

  private PersonName name = new PersonName();

  @Size(min = 1, max = 12)
  private String phone;

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RegularUser)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    RegularUser that = (RegularUser) o;

    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (phone != null ? !phone.equals(that.phone) : that.phone != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (phone != null ? phone.hashCode() : 0);
    return result;
  }
}
