package com.oracle.medrec.facade.broker.jaxrs;

import com.oracle.medrec.facade.DrugFacade;
import com.oracle.medrec.facade.model.DrugInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

/**
 * Physician Facade's jax-rs broker.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ApplicationScoped
@Path("/drug")
public class JaxRsDrugFacadeBroker {

  private static final Logger LOGGER = Logger.getLogger(JaxRsDrugFacadeBroker.class.getName());

  @Inject
  private DrugFacade drugFacade;

  @GET
  @Produces(MediaType.APPLICATION_XML)
  public List<DrugInfo> authenticateAndReturnPhysician() {
    LOGGER.finer("Get drugs.");
    return drugFacade.getAllDrugsInfo();
  }

}
