package com.oracle.physician.web.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracle.medrec.common.config.ExternalConfiguration;
import com.oracle.medrec.common.config.Services;
import com.oracle.medrec.facade.model.AuthenticatedPhysician;
import com.oracle.medrec.model.Physician;
import com.oracle.physician.service.PhysicianService;
import com.oracle.physician.web.Constants;

/**
 * AuthenticationController is a JSF ManagedBean that is responsible for
 * authentication of physician.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its affiliates. All rights
 *         reserved.
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

		AuthenticatedPhysician physician = null;

		LOGGER.info("Physician " + username + " is loging in.");

		String physicianServiceUrl = System.getenv(ExternalConfiguration.PHYSICIAN_SERVICE_URL);
				//ExternalConfiguration.getExternalConfigValue(ExternalConfiguration.PHYSICIAN_SERVICE_URL);

		String errorMessageKey = "message.invalidLogin";
		String additionalMessage = "";
		if (physicianServiceUrl != null) {
			LOGGER.info("Physician service URL found in external configuration.");

			try {
				Client client = ClientBuilder.newClient();
				WebTarget physicianTarget = client
						.target(physicianServiceUrl + "/" + Services.AUTHENTICATE_AND_RETURN_PHYSICIAN)
						.queryParam("userName", username).queryParam("password", password);

				Response response = physicianTarget.request(MediaType.APPLICATION_JSON).get();
				LOGGER.info("Physician service invocation was successful: " + response.getStatus());
				
				if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
					physician = null;
					LOGGER.info("Physician not found.");
				} else if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					physician = new AuthenticatedPhysician(response.readEntity(Physician.class));
					LOGGER.info("Physician found: " + physician.getUsername());
				} else {
					errorMessageKey = "message.physician.service.response.error";
					additionalMessage = Integer.toString(response.getStatus());
				}
			} catch (Exception e) {
				LOGGER.warning("Physician service invocation error.");
				e.printStackTrace();
				errorMessageKey = "message.physician.service.unavailable";
				additionalMessage = e.getMessage();
			}
		} else {
			LOGGER.info("Physician service URL NOT found in external configuration.");
			physician = physicianService.authenticateAndReturnPhysician(username, password);
		}
		LOGGER.info("Physician " + username + " logged in.");

		if (physician != null) {
			getPageContext().getSessionMap().put(Constants.AUTHENTICATED_USER_SESSION_KEY, physician);
			return Constants.PHYSICIAN_BASE_PATH + Constants.PHYSICIAN_HOME_RDT;
		}

		getPageContext().addGlobalOnlyErrorMessageWithKey(errorMessageKey, additionalMessage);
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
