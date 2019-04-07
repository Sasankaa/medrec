package com.oracle.medrec.model;

import com.oracle.medrec.common.util.DateAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Entity
@XmlRootElement
@Table(name = "prescriptions")
public class Prescription extends VersionedEntity {

  private static final long serialVersionUID = 4993043341973318975L;

  @Column(name = "date_prescribed")
  @Temporal(TemporalType.DATE)
  @XmlJavaTypeAdapter(DateAdapter.class)
  private Date datePrescribed = new Date();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, updatable = false)
  private Drug drug;

  private int dosage;

  private String frequency;

  private String instructions;

  @Column(name = "refills_remaining")
  private int refillsRemaining;

  public Date getDatePrescribed() {
    return datePrescribed;
  }

  public void setDatePrescribed(Date datePrescribed) {
    this.datePrescribed = datePrescribed;
  }

  public Drug getDrug() {
    return drug;
  }

  public void setDrug(Drug drug) {
    this.drug = drug;
  }

  public int getDosage() {
    return dosage;
  }

  public void setDosage(int dosage) {
    this.dosage = dosage;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public int getRefillsRemaining() {
    return refillsRemaining;
  }

  public void setRefillsRemaining(int refillsRemaining) {
    this.refillsRemaining = refillsRemaining;
  }

  public BigDecimal getCost() {
    if (drug != null && dosage != 0) {
      return drug.getPrice().multiply(new BigDecimal(dosage));
    }
    return BigDecimal.ZERO;
  }

}
