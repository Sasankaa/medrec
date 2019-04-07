package com.oracle.medrec.service.batch;

import javax.batch.api.listener.AbstractStepListener;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Abstract report step listener.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 *         Created by xiaojwu on 26/11/14.
 */
public abstract class AbstractReportStepListener extends AbstractStepListener {

  private static final Logger LOGGER = Logger.getLogger(RecordStepListener.class.getName());

  @Inject
  private BatchEventFirer firer;

  @Inject
  private JobContext jobContext;

  @Inject
  protected StepContext stepContext;

  @Inject
  private DrugsSumCache drugsSumCache;

  @Inject
  private PhysicianSumCache physicianSumCache;

  private Properties jobProperties;

  private String filePath;

  private String stepName;

  protected String seq;

  @Override
  public void beforeStep() throws Exception {
    jobProperties = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());
    Properties stepProperties = stepContext.getProperties();

    // assemble report file path
    stepName = stepContext.getStepName();
    filePath = jobProperties.getProperty(BatchConstants.PARAM_DIR) + stepName + BatchConstants.SUFFIX;
    stepProperties.setProperty(BatchConstants.PARAM_FILE_PATH, filePath);

    seq = jobProperties.getProperty(BatchConstants.PARAM_SEQ_ID);

    initExtraUserData();
    LOGGER.info("Preparing batch seq=" + seq + ", step=" + stepName + ", filePath=" + filePath);
  }

  public void initExtraUserData() throws Exception {

  }

  @Override
  public void afterStep() throws Exception {
    firer.fireBatchFinishedUpEvent(jobProperties, getType(), filePath);
    cleanExtraUserData();
    LOGGER.info("Finishing batch seq=" + seq + ", step=" + stepName + ", filePath=" + filePath);
  }

  public void cleanExtraUserData() throws Exception {

  }

  protected abstract BatchFinishedUpEvent.Type getType();
}
