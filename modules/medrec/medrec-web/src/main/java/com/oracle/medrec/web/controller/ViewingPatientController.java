package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Address;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.DuplicateSsnException;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.web.Constants;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 * JSF managed bean for updating patient profile.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingPatientController extends BaseMedRecPageController {

  @Inject
  private PatientService patientService;

  private Patient patient;

  @PostConstruct
  public void initialFields() {
    // get patient entity from JSF session
    patient = (Patient) getPageContext().getSessionMap().get(Constants.AUTHENTICATED_USER_SESSION_KEY);

    // re-fetch entity to ensure we have the latest updates
    patient = patientService.getPatient(patient.getId());

    if (patient.getAddress() == null) {
      patient.setAddress(new Address());
    }
  }

  public Patient getPatient() {
    return patient;
  }

  public String updatePatient() {
    try {
      patient = patientService.updatePatient(patient);
    } catch (DuplicateSsnException e) {
      getPageContext().addGlobalOnlyErrorMessageWithKey("message.duplicateSsn");
      return Constants.VIEW_PATIENT;
    }
    // put the updated patient into session
    getPageContext().getSessionMap().put(Constants.AUTHENTICATED_USER_SESSION_KEY, patient);
    getPageContext().addGlobalOnlyInfoMessageWithKey("message.updateProfileSuccessfully");
    return Constants.VIEW_PATIENT;
  }
}
