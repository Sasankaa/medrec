package com.oracle.physician.web.controller;

import com.oracle.medrec.facade.model.AuthenticatedPhysician;
import com.oracle.physician.service.PhysicianService;
import com.oracle.physician.web.Constants;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * AuthenticationController is a JSF ManagedBean that is responsible for
 * authentication of physician.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class AuthenticationController extends BasePhysicianPageController {

  private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class.getName());

  @Inject
  private PhysicianService physicianService;

  private String username;

  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String login() {
    LOGGER.info("Physician " + username + " is loging in.");
    AuthenticatedPhysician physician = physicianService.authenticateAndReturnPhysician(username, password);
    LOGGER.info("Physician " + username + " logged in.");

    if (physician != null) {
      getPageContext().getSessionMap().put(Constants.AUTHENTICATED_USER_SESSION_KEY, physician);
      return Constants.PHYSICIAN_BASE_PATH + Constants.PHYSICIAN_HOME_RDT;
    }
    getPageContext().addGlobalOnlyErrorMessageWithKey("message.invalidLogin");
    return Constants.LOGIN_PATH;
  }

  public String logout() {
    LOGGER.info("Physician logged out.");
    getPageContext().getSessionMap().remove(Constants.AUTHENTICATED_USER_SESSION_KEY);
    getPageContext().invalidateSession();
    FacesContext context = FacesContext.getCurrentInstance();
    try {
      context.getExternalContext().redirect(Constants.LOGIN_PATH_RDT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
