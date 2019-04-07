package com.oracle.medrec.web.rest;

import com.oracle.medrec.service.batch.BatchFinishedUpEvent;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SSE server end point. If batch job has finished, sever will notify the client to download the report.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved
 */

@ApplicationScoped
@Path("/sse")
public class BatchSSEHandler {

  private static final Logger LOGGER = Logger.getLogger(BatchSSEHandler.class.getName());

  /**
   * keep the SSE output
   */
  private ConcurrentHashMap<String, EventOutput> eventMap = new ConcurrentHashMap<>();

  /**
   * Gets the new SSE message stream channel.
   * The SSE output will be established if the method runs successfully.
   *
   * @return
   */
  @GET
  @Path("{name}")
  @Produces(SseFeature.SERVER_SENT_EVENTS)
  public EventOutput getMessageStream(@PathParam("name") String name) {
    LOGGER.info("--> SSE connection received.");
    // create new SSE
    final EventOutput newEventOutput = new EventOutput();
    final EventOutput eventOutput = eventMap.get(name);
    eventMap.put(name, newEventOutput);

    // if has connected, close old one
    if (eventOutput != null && !eventOutput.isClosed()) {
      try {
        eventOutput.close();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "Close former SSE connection failed.", e);
      }
    }
    return newEventOutput;
  }

  /**
   * A CDI Observer to handle the event notifying the batch job has finished.
   *
   * @param batchFinishedUpEvent
   */
  public void batchFinishedUp(@Observes BatchFinishedUpEvent batchFinishedUpEvent) {
    final OutboundEvent event = createOutboundEvent(batchFinishedUpEvent);
    // get the file owner
    EventOutput eventOutput = eventMap.get(batchFinishedUpEvent.getAdminName());
    if (eventOutput != null) {
      if (eventOutput.isClosed()) {
        eventMap.remove(batchFinishedUpEvent.getAdminName());
      } else {
        try {
          LOGGER.info("--> write sse");
          eventOutput.write(event);
        } catch (IOException e) {
          LOGGER.log(Level.SEVERE, "Send batch-finished message to client failed.", e);
          try {
            eventOutput.close();
          } catch (IOException e1) {
            LOGGER.log(Level.WARNING, "Close abnormal SSE connection failed.", e);
          }
          eventMap.remove(batchFinishedUpEvent.getAdminName());
        }
      }
    }
  }

  private OutboundEvent createOutboundEvent(BatchFinishedUpEvent batchFinishedUpEvent) {
    JsonObject jsonObject = Json.createObjectBuilder().add("seqId", batchFinishedUpEvent.getSeqId()).add("startDate",
        batchFinishedUpEvent.getStartDate()).add("endDate", batchFinishedUpEvent.getEndDate()).add("fileName",
        batchFinishedUpEvent.getFilename()).add("type", batchFinishedUpEvent.getType().ordinal()).build();
    return new OutboundEvent.Builder().mediaType(MediaType.APPLICATION_JSON_TYPE).data(JsonObject.class,
        jsonObject).build();
  }

}
