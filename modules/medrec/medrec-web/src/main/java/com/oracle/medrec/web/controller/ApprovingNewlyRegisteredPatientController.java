package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ApprovingNewlyRegisteredPatientController extends BaseMedRecPageController {

  @Inject
  private PatientService patientService;

  private String patientId;

  private Patient patient;

  public Patient getPatient() {
    return patient;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String viewNewlyRegisteredPatient() {
    patientId = this.getPageContext().getRequestParam("patientId");
    patient = patientService.getPatient(Long.parseLong(patientId));
    return Constants.VIEW_NEW_PATIENT;
  }

  public String approvePatient() {
    patientService.approvePatient(Long.parseLong(patientId));
    return Constants.VIEW_APPROVAL_RESULT;
  }

  public String denyPatient() {
    patientService.denyPatient(Long.parseLong(patientId));
    return Constants.VIEW_APPROVAL_RESULT;
  }
}
