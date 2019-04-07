package com.oracle.medrec.common.testing;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.oracle.medrec.common.persistence.BasePersistenceService;
import com.oracle.medrec.common.util.ClassUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 * Persistence test base class.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class EntityRepositoryTestCaseSupport<T extends BasePersistenceService> {

  /**
   * Default is true to ensure that transaction always gets rollbacked so that
   * we don't have to do database cleanup.
   */
  public static final boolean DEFAULT_ROLLBACK_ONLY = true;
  private boolean rollbackOnly = DEFAULT_ROLLBACK_ONLY;
  private static final Logger LOGGER = Logger.getLogger(EntityRepositoryTestCaseSupport.class.getName());
  private static EntityManagerFactory entityManagerFactory;
  protected T service;
  /**
   * JUnit should ensure that it won't be concurrently accessed. It'll be
   * assigned value before each test method gets executed.
   */
  private EntityManager entityManager;
  private EntityTransaction entityTransaction;

  @BeforeClass
  public static void initJpaEngine() {
    // FIXME hard-coded
    entityManagerFactory = Persistence.createEntityManagerFactory("MedRecTest");
    LOGGER.log(Level.FINER, "EntityManagerFactory created");
  }

  @AfterClass
  public static void destroyJpaEngine() {
    if (entityManagerFactory != null) {
      try {
        entityManagerFactory.close();
      } catch (Exception e) {
        LOGGER.log(Level.WARNING, "Cannot close EntityManagerFactory", e);
      }
    }

    entityManagerFactory = null;
    LOGGER.log(Level.FINER, "EntityManagerFactory closed");
  }

  @Before
  @SuppressWarnings("unchecked")
  public void beforeTestMethod() {
    Class<T> entityRepositoryClass = ClassUtils.getGenericArgumentType(getClass());
    try {
      service = ClassUtils.instantiateClass(entityRepositoryClass);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    try {
      entityManager = entityManagerFactory.createEntityManager();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    LOGGER.log(Level.FINER, "EntityManager created");

    service.setEntityManager(entityManager);

    startTransaction();
  }

  @After
  public void afterTestMethod() {
    closeEntityManager();
  }

  // Protected methods that could be used by subclasses
  // -----------------------------------

  protected final boolean isRollbackOnly() {
    return rollbackOnly;
  }

  protected final void setRollbackOnly(boolean rollbackOnly) {
    this.rollbackOnly = rollbackOnly;
  }

  protected final void entityTransactionCommit() { entityTransaction.commit(); }

  protected final void startTransaction() {
    entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    LOGGER.log(Level.FINER, "Transaction started");
  }

  protected final EntityManager getEntityManager() {
    return entityManager;
  }

  protected final void closeEntityManager() {
    if (entityManager == null) {
      return;
    }
    try {
      // in case exception happened in before()
      if (entityTransaction != null && entityTransaction.isActive()) {
        try {
          entityManager.flush();
          if (rollbackOnly) {
            entityTransaction.setRollbackOnly();
          }
        } catch (RuntimeException e) {
          entityTransaction.setRollbackOnly();
          throw e;
        } finally {
          // TODO if exception occured in test method, the transaction
          // should be automatically marked as rollbackonly?
          if (entityTransaction.getRollbackOnly()) {
            entityTransaction.rollback();
            LOGGER.log(Level.FINER, "Transaction rolled back");
          } else {
            try {
              entityTransaction.commit();
              LOGGER.log(Level.FINER, "Transaction committed");
            } catch (RollbackException e) {
              LOGGER.log(Level.FINER, "Transaction rolled back");
            }
          }
        }
      }
    } finally {
      try {
        entityManager.close();
        LOGGER.log(Level.FINER, "EntityManager closed");
      } catch (Exception e) {
        LOGGER.log(Level.WARNING, "Cannot close EntityManager", e);
      }
      entityTransaction = null;
      rollbackOnly = DEFAULT_ROLLBACK_ONLY;
      entityManager = null;
    }
  }

}
