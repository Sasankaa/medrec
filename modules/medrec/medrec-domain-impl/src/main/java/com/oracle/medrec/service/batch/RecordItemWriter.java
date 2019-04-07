package com.oracle.medrec.service.batch;

import com.oracle.medrec.model.Prescription;
import com.oracle.medrec.model.Record;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Writes record report by the result from {@link com.oracle.medrec.service.batch.RecordItemProcessor}.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
@Named("RecordItemWriter")
public class RecordItemWriter extends AbstractItemWriter {

  private static final Logger LOGGER = Logger.getLogger(RecordItemWriter.class.getName());

  private BufferedWriter writer;

  @Inject
  private StepContext stepContext;

  @Override
  public void open(Serializable checkpoint) throws Exception {
    LOGGER.info("Init record item writer");
    String filePath = stepContext.getProperties().getProperty(BatchConstants.PARAM_FILE_PATH);
    File file = new File(filePath);
    file.getParentFile().mkdirs();
    LOGGER.info("New record report is located at " + file.getCanonicalPath());
    writer = new BufferedWriter(new FileWriter(file, (checkpoint != null)));
    LOGGER.info("Init record item writer successfully");
  }

  @Override
  public void writeItems(List<Object> items) throws Exception {
    LOGGER.info("Start writing report of record ...");
    for (int i = 0; i < items.size(); i++) {
      Record record = (Record) items.get(i);
      writer.write(record.getDate().toString());
      writer.write(" | ");
      writer.write(String.valueOf(record.getPhysician().getId()));
      writer.write(" | ");
      writer.write(String.valueOf(record.getPatient().getId()));
      writer.write(" | ");
      writer.write(record.getDiagnosis());
      writer.write(" | ");
      writer.write(NumberFormat.getCurrencyInstance().format(record.getCost()));
      writer.newLine();
      for (Prescription p : record.getPrescriptions()) {
        writer.write("--> ");
        writer.write(p.getDatePrescribed().toString());
        writer.write(" | ");
        writer.write(String.valueOf(p.getDrug().getId()));
        writer.write(" | ");
        writer.write(p.getDrug().getName());
        writer.write(" | ");
        writer.write(NumberFormat.getCurrencyInstance().format(p.getDrug().getPrice()));
        writer.write(" | ");
        writer.write(String.valueOf(p.getDosage()));
        writer.write(" | ");
        writer.write(NumberFormat.getCurrencyInstance().format(p.getCost()));
        writer.newLine();
      }
      writer.newLine();
    }
    writer.close();
    LOGGER.info("Report of record has completed.");
  }

  @Override
  public void close() throws Exception {
    LOGGER.info("Writer of batch of record has stopped.");
    if (writer != null) {
      writer.close();
    }
  }

}
