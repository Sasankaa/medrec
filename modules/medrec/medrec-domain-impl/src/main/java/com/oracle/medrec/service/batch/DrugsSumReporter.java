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
 * A {@link javax.batch.api.Batchlet} for drug statistic creating drug report.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
@Named("DrugSumBatchLet")
public class DrugsSumReporter implements Batchlet {

  private static final Logger LOGGER = Logger.getLogger(DrugsSumReporter.class.getName());

  private BufferedWriter writer;

  @Inject
  private StepContext stepContext;

  @Override
  public String process() throws Exception {
    LOGGER.info("Start processing batch of drug ...");
    DrugsSumCache.DrugSum drugsSum = (DrugsSumCache.DrugSum) stepContext.getTransientUserData();
    String filePath = stepContext.getProperties().getProperty(BatchConstants.PARAM_FILE_PATH);
    writer = new BufferedWriter(new FileWriter(filePath));
    for (DrugsSumCache.DrugUnit reporter : drugsSum.getUnits()) {
      writer.write(String.valueOf(reporter.drug.getId()));
      writer.write(" | ");
      writer.write(reporter.drug.getName());
      writer.write(" | ");
      writer.write(NumberFormat.getCurrencyInstance().format(reporter.drug.getPrice()));
      writer.write(" | ");
      writer.write(String.valueOf(reporter.dosage_sum));
      writer.write(" | ");
      writer.write(NumberFormat.getCurrencyInstance().format(reporter.cost_sum));
      writer.newLine();
    }
    writer.close();
    LOGGER.info("Report of drug has completed.");
    return BatchStatus.STARTED.name();
  }

  @Override
  public void stop() throws Exception {
    LOGGER.info("Processing batch of drug has stopped.");
    if (writer != null) {
      writer.close();
    }
  }
}
