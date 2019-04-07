package com.oracle.medrec.service.batch;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Drug step listener.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 *         Created by xiaojwu on 26/11/14.
 */
@Named("DrugStepListener")
public class DrugStepListener extends AbstractReportStepListener {

  @Inject
  private DrugsSumCache drugsSumCache;

  @Override
  public void initExtraUserData() throws Exception {
    stepContext.setTransientUserData(drugsSumCache.getCache(seq));
  }

  @Override
  public void cleanExtraUserData() throws Exception {
    drugsSumCache.release(seq);
  }

  @Override
  protected BatchFinishedUpEvent.Type getType() {
    return BatchFinishedUpEvent.Type.DRUG;
  }
}
