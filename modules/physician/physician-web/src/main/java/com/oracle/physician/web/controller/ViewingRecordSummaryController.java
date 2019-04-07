package com.oracle.physician.web.controller;

import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.facade.model.RecordSummary;
import com.oracle.medrec.model.PersonName;
import com.oracle.physician.service.RecordService;
import com.oracle.physician.web.Constants;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ViewingRecordSummaryController is a JSF ManagedBean that is responsible for
 * supporting the view of showing record summary of current patient.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingRecordSummaryController extends BasePhysicianPageController {

  private static final Logger LOGGER = Logger.getLogger(ViewingRecordSummaryController.class.getName());

  @Inject
  private RecordService recordService;

  private FoundPatient patient;

  private RecordSummary recordSummary;

  @PostConstruct
  public void initPatient() {
    // retrieve patient from session
    patient = (FoundPatient) getPageContext().getSessionMap().get(Constants.PATIENT_SESSION_KEY);
    if (patient == null) {
      // initialize patient
      patient = new FoundPatient();
      patient.setName(new PersonName());
    }
    if (getPageContext().getRequestParam("patientId") != null) {
      // patient information in request parameters
      LOGGER.info("Get new patient information from request parameters.");
      // set patient
      patient.setId(Long.parseLong(getPageContext().getRequestParam("patientId")));
      patient.getName().setFirstName(getPageContext().getRequestParam("patientFirstName"));
      patient.getName().setMiddleName(getPageContext().getRequestParam("patientMiddleName"));
      patient.getName().setLastName(getPageContext().getRequestParam("patientLastName"));
      // restore patient into session map
      LOGGER.info("Set new patient " + patient.getName().getLastName() + " into session, as current patient.");
      getPageContext().getSessionMap().put(Constants.PATIENT_SESSION_KEY, patient);
    }
    if (patient == null) {
      // also null, error
      LOGGER.log(Level.SEVERE, "No patient in current session.");
    } else {
      // get record summary of very patient.
      recordSummary = recordService.getRecordSummaryByPatientId(patient.getId());
      LOGGER.info("Retrieved patient record summary.");
    }
  }

  public FoundPatient getPatient() {
    return patient;
  }

  public RecordSummary getRecordSummary() {
    return recordSummary;
  }

}
