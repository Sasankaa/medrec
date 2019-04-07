package com.oracle.physician.service.delegate;

import com.oracle.medrec.common.core.MethodInvocationCacheUpdateEvent;
import com.oracle.medrec.common.core.MethodInvocationCached;
import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.medrec.facade.model.RecordSummary;
import com.oracle.medrec.facade.model.RecordToCreate;
import com.oracle.physician.JaxRsProperties;
import com.oracle.physician.PhysicianSystemException;
import com.oracle.physician.service.RecordService;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.net.URI;

/**
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class RecordServiceDelegate implements RecordService {

  @Inject
  private Event<MethodInvocationCacheUpdateEvent> cacheUpdateEventEvent;

  private WebTarget recordFacade;

  @PostConstruct
  public void init() {
    recordFacade = ClientBuilder.newClient().target(URI.create(JaxRsProperties.RECORD_URI));
  }

  public void createRecord(RecordToCreate recordToCreate) {
    final Response response = recordFacade.request().post(Entity.xml(recordToCreate));

    if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
      final String msg = response.readEntity(String.class);
      throw new PhysicianSystemException("Failed : HTTP error code : " + response.getStatus() + ", " + msg);
    }
    MethodInvocationCacheUpdateEvent event = new MethodInvocationCacheUpdateEvent();
    try {
      event.setMethod(RecordServiceDelegate.class.getMethod("getRecordSummaryByPatientId", Long.class));
    } catch (NoSuchMethodException e) {
      // not happen
      e.printStackTrace();
    }
    event.setParameters(new Object[]{recordToCreate.getPatientId()});
    cacheUpdateEventEvent.fire(event);
  }

  @MethodInvocationCached
  public RecordSummary getRecordSummaryByPatientId(Long patientId) {
    return recordFacade.queryParam("patientId", Long.toString(patientId)).request().get(RecordSummary.class);
  }

  @MethodInvocationCached
  public RecordDetail getRecordDetail(Long id) {
    return recordFacade.path(Long.toString(id)).request().get(RecordDetail.class);
  }
}
