package com.oracle.medrec.service.impl;

import com.oracle.medrec.common.persistence.BasePersistenceService;
import com.oracle.medrec.common.persistence.JPQLPersistenceSupport;
import com.oracle.medrec.model.Drug;
import com.oracle.medrec.service.DrugService;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Diagnostic record business service implementation. which is responsible for
 * all business operations to record.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
public class DrugServiceImpl extends BasePersistenceService implements DrugService {

  @Override
  public List<Drug> getAllDrugsInfo() {
    return JPQLPersistenceSupport.findByProperties(entityManager, Drug.class, "Drug.findAllDrugs");
  }

}
