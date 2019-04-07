package com.oracle.medrec.service.impl;

import com.oracle.medrec.common.testing.EntityRepositoryTestCaseSupport;
import com.oracle.medrec.model.Drug;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * {@link com.oracle.medrec.service.impl.RecordServiceImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class DrugServiceImplTestCase extends EntityRepositoryTestCaseSupport<DrugServiceImpl> {

  private Drug drug, drug1;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(DrugServiceImplTestCase.class);
  }

  @Before
  public void before() {
    drug = EntitiesPreparation.createDrug();
    service.getEntityManager().persist(drug);

    drug1 = EntitiesPreparation.createDrug();
    service.getEntityManager().persist(drug1);
  }

  @After
  public void after() {
    service.getEntityManager().remove(drug);
    service.getEntityManager().remove(drug1);
  }

  @Test
  public void testGetAllDrugs() {
    List<Drug> list = service.getAllDrugsInfo();
    assertEquals(list.size(), 2);
  }

}
