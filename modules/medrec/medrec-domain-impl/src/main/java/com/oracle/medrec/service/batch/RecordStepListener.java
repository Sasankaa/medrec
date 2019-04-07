package com.oracle.medrec.service.batch;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Record step listener.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 *         Created by xiaojwu on 26/11/14.
 */
@Named("RecordStepListener")
public class RecordStepListener extends AbstractReportStepListener {

  @Inject
  private DrugsSumCache drugsSumCache;

  @Inject
  private PhysicianSumCache physicianSumCache;

  @Override
  public void initExtraUserData() throws Exception {
    // init drug and physician statistic cache
    Object[] cache = new Object[2];
    cache[0] = drugsSumCache.cache(seq);
    cache[1] = physicianSumCache.cache(seq);
    stepContext.setTransientUserData(cache);
  }

  @Override
  protected BatchFinishedUpEvent.Type getType() {
    return BatchFinishedUpEvent.Type.RECORD;
  }

}
