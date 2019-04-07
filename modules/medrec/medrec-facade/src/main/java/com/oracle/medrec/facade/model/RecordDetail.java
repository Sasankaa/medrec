package com.oracle.medrec.facade.model;

import com.oracle.medrec.common.util.DateAdapter;
import com.oracle.medrec.model.Prescription;
import com.oracle.medrec.model.Record;
import com.oracle.medrec.model.VitalSigns;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@XmlRootElement
public class RecordDetail extends TransferObject {

  private static final long serialVersionUID = -98237492374L;

  private Long id;

  private String diagnosis;

  private String notes;

  @XmlJavaTypeAdapter(DateAdapter.class)
  private Date date;

  private String symptoms;

  private VitalSigns vitalSigns;

  private List<Prescription> prescriptions;

  public RecordDetail() {
    prescriptions = new LinkedList<Prescription>();
  }

  public RecordDetail(Record record) {
    id = record.getId();
    diagnosis = record.getDiagnosis();
    notes = record.getNotes();
    date = record.getDate();
    symptoms = record.getSymptoms();
    vitalSigns = record.getVitalSigns();
    prescriptions = record.getPrescriptions();
  }

  public String getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
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

  public List<Prescription> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(List<Prescription> prescriptions) {
    this.prescriptions = prescriptions;
  }

  public void addPrescription(Prescription prescription) {
    prescriptions.add(prescription);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RecordDetail that = (RecordDetail) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
