package com.oracle.medrec.facade.model;

import com.oracle.medrec.model.Prescription;
import com.oracle.medrec.model.Record;
import com.oracle.medrec.model.VitalSigns;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@XmlRootElement
public class RecordToCreate extends TransferObject {

  private static final long serialVersionUID = -7485494494L;

  private String diagnosis;

  private String notes;

  private Date date = new Date();

  private String symptoms;

  private VitalSigns vitalSigns = new VitalSigns();

  private List<Prescription> prescriptions = new LinkedList<Prescription>();

  private Long patientId;

  private Long physicianId;

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

  public void addPrescription(Prescription prescription) {
    prescriptions.add(prescription);
  }

  public Long getPatientId() {
    return patientId;
  }

  public void setPatientId(Long patientId) {
    this.patientId = patientId;
  }

  public Long getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(Long physicianId) {
    this.physicianId = physicianId;
  }

  public List<Prescription> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(List<Prescription> prescriptions) {
    this.prescriptions = prescriptions;
  }

  public Record toRecord() {
    Record record = new Record();
    record.setDate(date);
    record.setDiagnosis(diagnosis);
    record.setNotes(notes);
    record.setPrescriptions(prescriptions);
    record.setSymptoms(symptoms);
    record.setVitalSigns(vitalSigns);
    return record;
  }

}
