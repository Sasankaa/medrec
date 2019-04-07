package com.oracle.medrec.service.batch;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;
import java.util.Properties;

/**
 * A CDI Event Firer to notify a batch job has finished.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
public class BatchEventFirer {

  @Inject
  private Event<BatchFinishedUpEvent> bfuEvent;

  /**
   * Notifies a batch job has finished.
   *
   * @param jobParameters Properties
   * @param type          {@link com.oracle.medrec.service.batch.BatchFinishedUpEvent.Type}
   * @param filename      String
   */
  public void fireBatchFinishedUpEvent(Properties jobParameters, BatchFinishedUpEvent.Type type, String filename) {
    BatchFinishedUpEvent event = new BatchFinishedUpEvent(
        filename.replace(File.separator, "/"),
        jobParameters.getProperty(BatchConstants.PARAM_START_DATE),
        jobParameters.getProperty(BatchConstants.PARAM_END_DATE),
        jobParameters.getProperty(BatchConstants.PARAM_SEQ_ID),
        jobParameters.getProperty(BatchConstants.PARAM_ADMIN_NAME),
        type);
    bfuEvent.fire(event);
  }
}
