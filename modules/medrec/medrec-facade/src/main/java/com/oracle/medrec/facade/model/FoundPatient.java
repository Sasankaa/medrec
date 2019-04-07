package com.oracle.medrec.facade.model;

import com.oracle.medrec.model.Patient;
import com.oracle.medrec.model.PersonName;

import java.util.Date;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class FoundPatient extends TransferObject {

  private static final long serialVersionUID = 48578453534L;

  private Long id;

  private PersonName name;

  private String ssn;

  private Date dob;

  public FoundPatient() {
  }

  public FoundPatient(Patient patient) {
    id = patient.getId();
    name = patient.getName();
    ssn = patient.getSsn();
    dob = patient.getDob();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    if (!id.equals(((FoundPatient) object).getId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

}
