package com.oracle.medrec.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Embeddable
public class Address implements Serializable {

  private static final long serialVersionUID = 4907071444700296617L;
  private String city;
  private String country;
  private String state;
  private String street1;
  private String street2;
  private String zip;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStreet1() {
    return street1;
  }

  public void setStreet1(String street1) {
    this.street1 = street1;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Address address = (Address) o;

    if (city != null ? !city.equals(address.city) : address.city != null) {
      return false;
    }
    if (country != null ? !country.equals(address.country) : address.country != null) {
      return false;
    }
    if (state != null ? !state.equals(address.state) : address.state != null) {
      return false;
    }
    if (street1 != null ? !street1.equals(address.street1) : address.street1 != null) {
      return false;
    }
    if (street2 != null ? !street2.equals(address.street2) : address.street2 != null) {
      return false;
    }
    if (zip != null ? !zip.equals(address.zip) : address.zip != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = city != null ? city.hashCode() : 0;
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (state != null ? state.hashCode() : 0);
    result = 31 * result + (street1 != null ? street1.hashCode() : 0);
    result = 31 * result + (street2 != null ? street2.hashCode() : 0);
    result = 31 * result + (zip != null ? zip.hashCode() : 0);
    return result;
  }
}
