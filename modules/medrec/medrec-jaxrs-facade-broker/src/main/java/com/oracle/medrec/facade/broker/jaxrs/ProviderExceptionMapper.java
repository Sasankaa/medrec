package com.oracle.medrec.facade.broker.jaxrs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 2014-2-11
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Provider
public class ProviderExceptionMapper implements ExceptionMapper<Exception> {
  private static final Logger LOGGER = Logger.getLogger(ProviderExceptionMapper.class.getName());

  public Response toResponse(Exception ex) {
    LOGGER.log(Level.WARNING, "jax-rs brock error. ", ex);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
  }

}
