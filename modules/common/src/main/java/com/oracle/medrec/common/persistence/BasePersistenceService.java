package com.oracle.medrec.common.persistence;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Basic persistence service.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public abstract class BasePersistenceService {

  @PersistenceContext
  protected EntityManager entityManager;

  /**
   * Just for test.
   *
   * @return
   */
  public EntityManager getEntityManager() {
    return entityManager;
  }

  /**
   * Just for test.
   *
   * @param entityManager
   */
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * EntityManager persists entity.
   *
   * @param object
   */
  protected void save(Object object) {
    try {
      entityManager.persist(object);
    } catch (ConstraintViolationException e) {
      throw handleValidationError(e);
    }
  }

  /**
   * EntityManager merges entity.
   *
   * @param object
   */
  protected <E> E update(E object) {
    try {
      return entityManager.merge(object);
    } catch (ConstraintViolationException e) {
      throw handleValidationError(e);
    }
  }

  private PersistenceException handleValidationError(ConstraintViolationException e) {
    StringBuilder sb = new StringBuilder("There are " + e.getConstraintViolations().size() + " issues of entity by " +
        "validation.");
    int i = 1;
    for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
      sb.append(" ").append(i++).append(". ");
      sb.append(cv.getPropertyPath());
      sb.append("(").append(cv.getInvalidValue()).append("): ");
      sb.append(cv.getMessage());
    }
    return new PersistenceException(sb.toString());
  }

}
