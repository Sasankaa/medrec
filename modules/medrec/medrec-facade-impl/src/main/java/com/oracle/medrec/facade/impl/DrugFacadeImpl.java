package com.oracle.medrec.facade.impl;

//import com.oracle.medrec.common.core.MethodParameterValidated;
//import com.oracle.medrec.common.core.ThrowableLogged;

import com.oracle.medrec.common.core.MethodParameterValidated;
import com.oracle.medrec.common.core.ThrowableLogged;
import com.oracle.medrec.facade.DrugFacade;
import com.oracle.medrec.facade.model.DrugInfo;
import com.oracle.medrec.model.Drug;
import com.oracle.medrec.service.DrugService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@MethodParameterValidated
@ThrowableLogged
public class DrugFacadeImpl implements DrugFacade {

  @Inject
  private DrugService drugService;

  @Override
  public List<DrugInfo> getAllDrugsInfo() {
    List<Drug> drugList = drugService.getAllDrugsInfo();
    List<DrugInfo> drugInfoList = new ArrayList<>(drugList.size());
    for (Drug drug : drugList) {
      drugInfoList.add(new DrugInfo(drug));
    }
    return drugInfoList;
  }
}
