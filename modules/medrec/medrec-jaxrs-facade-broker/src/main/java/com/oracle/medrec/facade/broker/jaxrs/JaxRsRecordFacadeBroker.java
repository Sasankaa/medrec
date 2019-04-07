package com.oracle.medrec.facade.broker.jaxrs;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracle.medrec.facade.RecordFacade;
import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.medrec.facade.model.RecordSummary;
import com.oracle.medrec.facade.model.RecordToCreate;

/**
 * Record Facade's jax-rs broker.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Path("/records")
@ApplicationScoped
public class JaxRsRecordFacadeBroker {

  private static final Logger LOGGER = Logger.getLogger(JaxRsRecordFacadeBroker.class.getName());

  @Inject
  private RecordFacade recordFacade;

  /**
   * Create new record which may contains prescriptions. This shows 'POST'
   * consuming media type 'XML' and returning standard {@link Response}.
   * Client end point send date by XML.
   *
   * @param recordToCreate
   * @return
   */
  @POST
  @Consumes(MediaType.APPLICATION_XML)
  public void createRecord(RecordToCreate recordToCreate) {
    recordFacade.createRecord(recordToCreate);
    LOGGER.info("Create new Record.");
  }

  /**
   * Get record summary by patient id. This shows 'GET' producing response by
   * media type 'JSON'. Using @QueryParam annotation to inject URI query
   * parameter into Java method.
   *
   * @param patientId
   * @return
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public RecordSummary getRecordSummaryByPatientId(@QueryParam("patientId") Long patientId) {
    try {
      RecordSummary rs = recordFacade.getRecordSummaryByPatientId(patientId);
      return rs;
    } catch (Exception e) {
      return new RecordSummary();
    }
  }

  /**
   * Get record detail by record id. This shows 'GET' producing response by
   * media type 'JSON'. Using @PathParam annotation to inject the value of URI
   * parameter that defined in @Path expression into Java method.
   *
   * @param id
   * @return
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public RecordDetail getRecordDetail(@PathParam("id") Long id) {
    try {
      RecordDetail rd = recordFacade.getRecordDetail(id);
      return rd;
    } catch (Exception e) {
      return new RecordDetail();
    }
  }

}
