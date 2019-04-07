package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingNewlyRegisteredPatientsController extends BaseMedRecPageController {

  @Inject
  private PatientService patientService;

  @SuppressWarnings("unchecked")
  public List<Patient> getPatients() {
    return (List<Patient>) getPageContext().getSessionMap().get(Constants.PATIENTS_SESSION_KEY);
  }

  public String viewNewlyRegisteredPatients() {
    List<Patient> patients = patientService.getNewlyRegisteredPatients();
    getPageContext().getSessionMap().put(Constants.PATIENTS_SESSION_KEY, patients);
    return Constants.VIEW_NEW_PATIENTS;
  }

}