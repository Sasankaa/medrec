package com.oracle.medrec.service.batch;

import com.oracle.medrec.model.Record;

import javax.annotation.PostConstruct;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Processes every {@link com.oracle.medrec.model.Record} from {@link RecordItemReader}.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
@Named("RecordItemProcessor")
public class RecordItemProcessor implements ItemProcessor {

  private static final Logger LOGGER = Logger.getLogger(RecordItemProcessor.class.getName());

  @Inject
  private StepContext stepContext;

  private DrugsSumCache.DrugSum drugSum;

  private PhysicianSumCache.PhysicianSum physicianSum;

  @PostConstruct
  public void addSumObjectsIntoContext() {
    LOGGER.info("Init record item processor.");
    Object[] cache = (Object[]) stepContext.getTransientUserData();
    drugSum = (DrugsSumCache.DrugSum) cache[0];
    physicianSum = (PhysicianSumCache.PhysicianSum) cache[1];
  }

  @Override
  public Object processItem(Object item) throws Exception {
    LOGGER.info("Process record " + item.toString());
    Record record = (Record) item;
    record.countCost();
    drugSum.addPrescription(record.getPrescriptions());
    physicianSum.addRecord(record);
    return record;
  }

}
