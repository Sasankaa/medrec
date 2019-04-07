package com.oracle.physician.web.controller;

import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.physician.service.PatientService;
import com.oracle.physician.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SearchingPatientController is a JSF ManagedBean that is responsible for
 * searching patients with last name and ssn.
 *
 * @author Copyright (c) 2007,2013, Oracle and/or its affiliates. All rights
 *         reserved.
 */
@Model
public class SearchingPatientController extends BasePhysicianPageController {

  private static final Logger LOGGER = Logger.getLogger(SearchingPatientController.class.getName());

  @Inject
  private PatientService patientService;

  private String lastName;

  private String ssn;

  private List<FoundPatient> patients;

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  @SuppressWarnings("unchecked")
  public List<FoundPatient> getPatients() {
    if (patients == null) {
      LOGGER.log(Level.FINEST, "Get Patients from session");
      patients = (List<FoundPatient>) getPageContext().getSessionMap().get(Constants.PATIENTS_SESSION_KEY);
    }
    return patients;
  }

  @SuppressWarnings("unchecked")
  public void search() {
    List<FoundPatient> patients = Collections.EMPTY_LIST;

    LOGGER.log(Level.FINER, "Searching patients... last name: " + lastName + ", ssn: " + ssn);
    if (ssn == null) {
      ssn = "";
    }
    if (lastName == null) {
      lastName = "";
    }
    if (!ssn.equals("") || !lastName.equals("")) {
      patients = patientService.fuzzyFindApprovedPatientsByLastNameAndSsn(lastName, ssn);

      LOGGER.log(Level.FINER, "Found " + patients.size() + " patient(s)");
    }

    // put result into session
    getPageContext().getSessionMap().put(Constants.PATIENTS_SESSION_KEY, patients);
  }

}
