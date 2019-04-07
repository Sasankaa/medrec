package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.web.Constants;
import com.oracle.medrec.web.login.Password;
import com.oracle.medrec.web.login.Username;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class AuthenticatingPatientController extends BaseMedRecPageController {

  private static final Logger LOGGER = Logger.getLogger(AuthenticatingPatientController.class.getName());

  @Inject
  private PatientService patientService;

  @Username
  @Inject
  private String username;

  @Password
  @Inject
  private String password;

  public String login() {
    LOGGER.info("Username: " + username);
    Patient user = patientService.authenticateAndReturnPatient(username, password);

    if (user != null) {
      getPageContext().getSessionMap().put(Constants.AUTHENTICATED_USER_SESSION_KEY, user);
      LOGGER.info("User " + username + " logged in");
      return Constants.PATIENT_BASE_PATH + Constants.PATIENT_HOME_RDT;
    }

    LOGGER.log(Level.FINER, "Username: " + username);
    getPageContext().addGlobalOnlyErrorMessage(
        getPageContext().getMessage("message.invalidPatientLogin"));
    return Constants.INDEX_PATH;
  }

  @SuppressWarnings("unchecked")
  public String logout() {
    Patient user = (Patient) getPageContext().getSessionMap().remove(Constants.AUTHENTICATED_USER_SESSION_KEY);
    getPageContext().invalidateSession();
    if (user != null) {
      LOGGER.info("User " + user.getUsername() + " logged out");
    }
    return Constants.INDEX_PATH_RDT;
  }

}
