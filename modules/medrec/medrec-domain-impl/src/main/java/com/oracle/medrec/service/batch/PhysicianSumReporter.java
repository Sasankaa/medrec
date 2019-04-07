package com.oracle.medrec.service.batch;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * A {@link javax.batch.api.Batchlet} for physician statistic creating physician report.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
@Named("PhysicianSumBatchLet")
public class PhysicianSumReporter implements Batchlet {

  private static final Logger LOGGER = Logger.getLogger(PhysicianSumReporter.class.getName());

  private BufferedWriter writer;

  @Inject
  private StepContext stepContext;

  @Override
  public String process() throws Exception {
    LOGGER.info("Start processing batch of physician ...");
    PhysicianSumCache.PhysicianSum physicianSum = (PhysicianSumCache.PhysicianSum) stepContext.getTransientUserData();
    String filePath = stepContext.getProperties().getProperty(BatchConstants.PARAM_FILE_PATH);
    writer = new BufferedWriter(new FileWriter(filePath));
    for (PhysicianSumCache.PhysicianUnit reporter : physicianSum.getUnits()) {
      writer.write(String.valueOf(reporter.physician.getId()));
      writer.write(" | ");
      writer.write(reporter.physician.getName().toString());
      writer.write(" | ");
      writer.write(String.valueOf(reporter.record_num));
      writer.write(" | ");
      writer.write(NumberFormat.getCurrencyInstance().format(reporter.cost_sum));
      writer.newLine();
    }
    writer.close();
    LOGGER.info("Report of physician has completed.");
    return BatchStatus.COMPLETED.name();
  }

  @Override
  public void stop() throws Exception {
    LOGGER.info("Processing batch of physician has stopped.");
    if (writer != null) {
      writer.close();
    }
  }
}
