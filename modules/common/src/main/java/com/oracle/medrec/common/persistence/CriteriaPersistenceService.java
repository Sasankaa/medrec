package com.oracle.medrec.common.persistence;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * Business service utilizes {@link CriteriaPersistenceSupport} to query
 * informations. The service needs to extend this abstract class.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class CriteriaPersistenceService extends BasePersistenceService {

  protected CriteriaBuilder criteriaBuilder;

  /**
   * Initiate CriteriaBuilder.
   */
  @PostConstruct
  public void initBuilder() {
    criteriaBuilder = entityManager.getCriteriaBuilder();
  }

}
