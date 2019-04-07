package com.oracle.physician.web.controller;

import com.oracle.medrec.facade.model.AuthenticatedPhysician;
import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.facade.model.RecordToCreate;
import com.oracle.medrec.model.Drug;
import com.oracle.medrec.model.Prescription;
import com.oracle.physician.service.RecordService;
import com.oracle.physician.web.Constants;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * CreatingRecordController is a JSF ManagedBean that is responsible for
 * creating diagnostic record of a patient by physician.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Named
@SessionScoped
public class CreatingRecordController extends BasePhysicianPageController implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = Logger.getLogger(CreatingRecordController.class.getName());

  private RecordToCreate record;

  private Prescription prescription;

  private String drugInfo;

  @PostConstruct
  public void initRecord() {
    record = new RecordToCreate();
    initPrescription();
  }

  private void initPrescription() {
    prescription = new Prescription();
    prescription.setDrug(new Drug());
    prescription.setDosage(0);
    prescription.setRefillsRemaining(0);
    prescription.setFrequency("");
    prescription.setInstructions("");
  }

  public String getDrugInfo() {
    return drugInfo;
  }

  public void setDrugInfo(String drugInfo) {
    this.drugInfo = drugInfo;
  }

  public RecordToCreate getRecord() {
    return record;
  }

  public Prescription getPrescription() {
    return prescription;
  }

  public String saveRecord(RecordService recordService) {

    // get physician
    AuthenticatedPhysician physician = (AuthenticatedPhysician) getPageContext().getSessionMap().get(Constants
        .AUTHENTICATED_USER_SESSION_KEY);
    // get current patient
    FoundPatient patient = (FoundPatient) getPageContext().getSessionMap().get(Constants.PATIENT_SESSION_KEY);
    // set relevance
    record.setPhysicianId(physician.getId());
    record.setPatientId(patient.getId());

    LOGGER.info("Creating record...");
    LOGGER.finer("Patient ID: " + patient.getId());
    LOGGER.finer("Physician ID: " + physician.getId());
    LOGGER.finer("Symptoms: " + record.getSymptoms());
    LOGGER.finer("Number of prescriptions: " + record.getPrescriptions().size());

    // create
    recordService.createRecord(record);
    LOGGER.info("Creating record finished successfully.");

    initRecord();
    return Constants.VIEW_RECORD_CREATION_RESULT;
  }

  public void addPrescription() {
    Drug drug = prescription.getDrug();
    String[] info = drugInfo.split("-");
    drug.setID(Long.valueOf(info[0]));
    drug.setName(info[1]);
    record.addPrescription(prescription);
    initPrescription();
  }

}
