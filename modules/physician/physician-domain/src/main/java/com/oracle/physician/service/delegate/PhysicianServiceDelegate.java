package com.oracle.physician.service.delegate;

import com.oracle.medrec.common.core.MethodInvocationCached;
import com.oracle.medrec.facade.model.AuthenticatedPhysician;
import com.oracle.physician.JaxRsProperties;
import com.oracle.physician.service.PhysicianService;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.net.URI;

/**
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class PhysicianServiceDelegate implements PhysicianService {

  private WebTarget physicianFacade;

  @PostConstruct
  public void init() {
    physicianFacade = ClientBuilder.newClient().target(URI.create(JaxRsProperties.PHYSICIAN_URI));
  }

  @MethodInvocationCached
  public AuthenticatedPhysician authenticateAndReturnPhysician(String username, String password) {
    final Response response = physicianFacade.queryParam("username", username).queryParam("password",
        password).request().get();
    return response.hasEntity() ? response.readEntity(AuthenticatedPhysician.class) : null;
  }

}
