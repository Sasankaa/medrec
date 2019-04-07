package com.oracle.physician.web.controller;

import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;
import com.oracle.physician.service.PatientService;
import com.oracle.physician.web.Constants;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ViewingPatientController is a JSF ManagedBean that is responsible for showing
 * patient's profile.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingPatientController extends BasePhysicianPageController {

  private static final Logger LOGGER = Logger.getLogger(ViewingPatientController.class.getName());

  @Inject
  private PatientService patientService;

  private Patient patient;

  @PostConstruct
  public void initPatient() {
    Long patientId = ((FoundPatient) getPageContext().getSessionMap().get(Constants.PATIENT_SESSION_KEY)).getId();
    LOGGER.log(Level.FINEST, "Patient ID: " + patientId);
    patient = patientService.getPatient(patientId);
    LOGGER.log(Level.FINEST, "Patient gender: " + patient.getGender());
  }

  public Patient getPatient() {
    return patient;
  }

}
