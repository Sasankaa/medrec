package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.DuplicateSsnException;
import com.oracle.medrec.service.DuplicateUsernameException;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class RegisteringPatientController extends BaseMedRecPageController {

  @Inject
  private PatientService patientService;

  private Patient patient = new Patient();

  @NotNull
  @Size(min = 6, max = 20)
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Patient getPatient() {
    return patient;
  }

  public String registerPatient() {
    if (password != null && !password.equals(patient.getPassword())) {
      getPageContext().addGlobalOnlyErrorMessageWithKey("message.password.notConsistent");
    } else {
      try {
        patientService.createPatient(patient);
        return "viewPatientRegistrationResult";
      } catch (DuplicateUsernameException e) {
        getPageContext().addGlobalOnlyErrorMessageWithKey("message.duplicateUsername");
      } catch (DuplicateSsnException e) {
        getPageContext().addGlobalOnlyErrorMessageWithKey("message.duplicateSsn");
      }
    }
    return Constants.REGISTER_PATIENT;
  }

}
