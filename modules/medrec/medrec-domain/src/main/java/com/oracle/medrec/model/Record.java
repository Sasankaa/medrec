package com.oracle.medrec.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Entity
@Table(name = "records")
@NamedQueries({@NamedQuery(name = "Record.findRecordsByPatientId", query = "SELECT r FROM Record r WHERE r.patient.id" +
    " = :patientId"), @NamedQuery(name = "Record.findRecordsByDateInterval", query = "SELECT r FROM Record r WHERE r" +
    ".date >= :startDate and r.date <= :endDate")})
public class Record extends VersionedEntity {

  private static final long serialVersionUID = -4395051789276646078L;

  private String diagnosis;

  private String notes;

  @Temporal(TemporalType.DATE)
  @Column(name = "CREATE_DATE")
  private Date date;

  private String symptoms;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, updatable = false)
  private Physician physician;

  private VitalSigns vitalSigns;

  @Transient
  private BigDecimal cost = BigDecimal.ZERO;

  /**
   * Prescriptions need to be acessed whenever record is accessed
   */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Prescription> prescriptions = new LinkedList<Prescription>();

  public String getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(String diagnosis) {
    System.out.println("*** Diagnosis = "+diagnosis);
    this.diagnosis = diagnosis;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    System.out.println("*** Notes = "+notes);
    this.notes = notes;
  }

  public Physician getPhysician() {
    return physician;
  }

  public void setPhysician(Physician physician) {
    System.out.println("*** Physician = "+physician);
    this.physician = physician;
  }

  public List<Prescription> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(List<Prescription> prescriptions) {
    if (prescriptions != null) {
      for (int i=0; i< prescriptions.size(); i++) {
        System.out.println("*** Prescription["+i+"] = "+prescriptions.get(i));
      }
    }
    this.prescriptions = prescriptions;
  }

  public void addPrescription(Prescription prescription) {
    prescriptions.add(prescription);
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(String symptoms) {
    this.symptoms = symptoms;
  }

  public VitalSigns getVitalSigns() {
    return vitalSigns;
  }

  public void setVitalSigns(VitalSigns vitalSigns) {
    this.vitalSigns = vitalSigns;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public BigDecimal countCost() {
    cost = BigDecimal.ZERO;
    if (prescriptions != null) {
      for (Prescription p : prescriptions) {
        cost = cost.add(p.getCost());
      }
    }
    return cost;
  }

  public BigDecimal getCost() {
    return cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Record record = (Record) o;

    if (diagnosis != null ? !diagnosis.equals(record.diagnosis) : record.diagnosis != null) {
      return false;
    }
    if (patient != null ? !patient.equals(record.patient) : record.patient != null) {
      return false;
    }
    if (physician != null ? !physician.equals(record.physician) : record.physician != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = diagnosis != null ? diagnosis.hashCode() : 0;
    result = 31 * result + (patient != null ? patient.hashCode() : 0);
    result = 31 * result + (physician != null ? physician.hashCode() : 0);
    return result;
  }
}
