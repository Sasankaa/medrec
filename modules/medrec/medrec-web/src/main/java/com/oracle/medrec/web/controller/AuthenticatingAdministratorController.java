package com.oracle.medrec.web.controller;

import com.oracle.medrec.web.Constants;
import com.oracle.medrec.web.login.Password;
import com.oracle.medrec.web.login.Username;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class AuthenticatingAdministratorController extends BaseMedRecPageController {

  private static final Logger LOGGER = Logger.getLogger(AuthenticatingAdministratorController.class.getName());

  private final static String ROLE_ADMIN = "admin";

  @Username
  @Inject
  private String username;

  @Password
  @Inject
  private String password;

  public String login() {
    LOGGER.info("Username: " + username);
    HttpServletRequest request = (HttpServletRequest) getPageContext().getFacesContext().getExternalContext()
        .getRequest();
    HttpServletResponse response = (HttpServletResponse) getPageContext().getFacesContext().getExternalContext()
        .getResponse();
    try {
      if (request.authenticate(response) && request.isUserInRole(ROLE_ADMIN)) {
        request.logout();
      }
    } catch (ServletException | IOException e) {
      LOGGER.info(e.getMessage());
      return Constants.INDEX_PATH;
    }
    try {
      request.login(username, password);
      if (!request.isUserInRole(ROLE_ADMIN)) {
        request.logout();
        addFailureMessage();
        LOGGER.info("Login failed: Invalid role.");
        return Constants.INDEX_PATH;
      }
    } catch (ServletException e) {
      addFailureMessage();
      LOGGER.info("Login failed: " + e.getMessage());
      return Constants.INDEX_PATH;
    }
    getPageContext().getSessionMap().put("adminName", username);
    LOGGER.info(request.getUserPrincipal().getName() + " logged in.");
    return Constants.ADMIN_BASE_PATH + Constants.ADMIN_HOME_RDT;
  }

  public String logout() {
    HttpServletRequest request = (HttpServletRequest) getPageContext().getFacesContext().getExternalContext()
        .getRequest();
    HttpServletResponse response = (HttpServletResponse) getPageContext().getFacesContext().getExternalContext()
        .getResponse();
    try {
      if (request.authenticate(response)) {
        request.logout();
      }
    } catch (ServletException | IOException e) {
      addFailureMessage();
      LOGGER.warning("Logout failed: " + e.getMessage());
      return Constants.ADMIN_HOME_RDT;
    }
    LOGGER.info("Administrator logged out.");
    getPageContext().invalidateSession();
    return Constants.INDEX_PATH_RDT;
  }


  protected void addFailureMessage() {
    getPageContext().addGlobalOnlyErrorMessage(getPageContext().getMessage("message.invalidLogin"));
  }

}
