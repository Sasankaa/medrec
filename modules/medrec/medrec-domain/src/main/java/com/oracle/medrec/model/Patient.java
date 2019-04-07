package com.oracle.medrec.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Entity
@Table(name = "patients")
public class Patient extends RegularUser {

  private static final long serialVersionUID = 313728838021028177L;

  @Temporal(TemporalType.DATE)
  @NotNull()
  private Date dob;

  @Enumerated(EnumType.STRING)
  @NotNull()
  private Gender gender;

  @Column(unique = true)
  @NotNull()
  @Size(min = 9, max = 9)
  private String ssn;

  private Address address = new Address();

  @Enumerated(EnumType.STRING)
  private Patient.Status status = Patient.Status.REGISTERED;

  // No setter and getter... Now used to do cascading
  @OneToMany(cascade = CascadeType.ALL)
  private List<Record> records;

  @Transient
  private boolean isSsnChanged = false;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    if (this.ssn != null && !this.ssn.equals(ssn)) {
      setSsnChanged(true);
    }
    this.ssn = ssn;
  }

  public void approve() {
    setStatus(Patient.Status.APPROVED);
  }

  public void deny() {
    setStatus(Patient.Status.DENIED);
  }

  public boolean isApproved() {
    return Patient.Status.APPROVED.equals(getStatus());
  }

  public boolean isDenied() {
    return Patient.Status.DENIED.equals(getStatus());
  }

  public Patient.Status getStatus() {
    return status;
  }

  public void setStatus(Patient.Status status) {
    this.status = status;
  }

  public boolean isSsnChanged() {
    return isSsnChanged;
  }

  public void setSsnChanged(boolean ssnChanged) {
    this.isSsnChanged = ssnChanged;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Patient)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    Patient patient = (Patient) o;

    if (isSsnChanged != patient.isSsnChanged) {
      return false;
    }
    if (address != null ? !address.equals(patient.address) : patient.address != null) {
      return false;
    }
    if (dob != null ? !dob.equals(patient.dob) : patient.dob != null) {
      return false;
    }
    if (gender != patient.gender) {
      return false;
    }
    if (ssn != null ? !ssn.equals(patient.ssn) : patient.ssn != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (dob != null ? dob.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    result = 31 * result + (ssn != null ? ssn.hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (isSsnChanged ? 1 : 0);
    return result;
  }

  public enum Status {
    REGISTERED, APPROVED, DENIED
  }

  public enum Gender {
    MALE, FEMALE
  }
}
