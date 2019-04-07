package com.oracle.medrec.web.rest;

import com.oracle.medrec.service.FinanceService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.logging.Logger;

/**
 * Report jax-rs resource that is in charge of creating and shipping reports.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved
 */

@Path("/report")
public class BatchResource {

  private static final Logger LOGGER = Logger.getLogger(BatchResource.class.getName());

  @Inject
  private FinanceService financeService;

  @POST
  @Path("{name}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public JsonObject createReport(
            @NotNull @PathParam("name") String name,
            @NotNull @FormParam("startDate") String startDate,
            @NotNull @FormParam("endDate") String endDate) {
    LOGGER.info("--> create report." + startDate + "-" + endDate);
    String seq = financeService.createFinanceReport(startDate, endDate, name);
    JsonObject jsonObject = Json.createObjectBuilder().add("seqId", seq).add("startDate", startDate).add("endDate",
        endDate).build();
    return jsonObject;
  }

  @GET
  @Path("{name}/{date-interval}/{filename}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getReport(
            @NotNull @PathParam("name") String name,
            @NotNull @PathParam("date-interval") String interval,
            @NotNull @PathParam("filename") String filename) {
    StringBuilder builder = new StringBuilder();
    builder.append(name).append(File.separator).append(interval).append(File.separator).append(filename);
    File file = new File(builder.toString());

    Response.ResponseBuilder response = Response.ok(file);
    response.header("Content-Disposition", "attachment; filename=" + filename);
    return response.build();
  }

}
