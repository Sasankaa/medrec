package com.oracle.medrec.common.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Convenient JPA criteria query base class for generic repository (DAO) or
 * business services.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class CriteriaPersistenceSupport {

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static <E> CriteriaQuery<E> query(CriteriaBuilder cb, Class<E> entityClass, Predication... predications) {
    CriteriaQuery<E> cq = cb.createQuery(entityClass);
    Root<E> root = cq.from(entityClass);
    cq.select(root);
    Predicate[] ps = new Predicate[predications.length];
    for (int i = 0; i < predications.length; i++) {
      ps[i] = predications[i].getPredicate(cb, root);
    }
    cq.where(cb.and(ps));
    return cq;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static <E> CriteriaQuery<Long> queryCount(CriteriaBuilder cb, Class<E> entityClass,
                                                    Predication... predications) {
    CriteriaQuery<Long> cql = cb.createQuery(Long.class);
    CriteriaQuery<E> cq = cb.createQuery(entityClass);
    Root<E> root = cq.from(entityClass);
    Expression<Long> count = cb.count(root);
    cql.select(count);

    Predicate[] ps = new Predicate[predications.length];
    for (int i = 0; i < predications.length; i++) {
      ps[i] = predications[i].getPredicate(cb, root);
    }
    cql.where(cb.and(ps));
    return cql;
  }

  /**
   * Execute a criteria query and return the query results as a 'T' typed
   * List.
   *
   * @param entityManager
   * @param cb
   * @param entityClass
   * @param predications
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public static <E> List<E> find(EntityManager entityManager, CriteriaBuilder cb, Class<E> entityClass,
                                 Predication... predications) {
    try {
      CriteriaQuery<E> cq = query(cb, entityClass, predications);
      return entityManager.createQuery(cq).getResultList();
    } catch (NoResultException ex) {
      return null;
    }
  }

  /**
   * Execute a criteria query and return the query results as a 'T' typed List
   * with certain range.
   *
   * @param entityManager
   * @param cb
   * @param entityClass
   * @param offset          - Set the position of the first result to retrieve.
   * @param limit           - Set the maximum number of results to retrieve.
   * @param predications
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public static <E> List<E> find(EntityManager entityManager, CriteriaBuilder cb, Class<E> entityClass, int offset,
                                 int limit, Predication... predications) {
    try {
      CriteriaQuery<E> cq = query(cb, entityClass, predications);
      return entityManager.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();
    } catch (NoResultException ex) {
      return null;
    }
  }

  /**
   * Execute a criteria query that returns a single result.
   *
   * @param entityManager
   * @param cb
   * @param entityClass
   * @param predications
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public static <E> E findUnique(EntityManager entityManager, CriteriaBuilder cb, Class<E> entityClass,
                                 Predication... predications) {
    try {
      CriteriaQuery<E> cq = query(cb, entityClass, predications);
      return entityManager.createQuery(cq).getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  /**
   * Execute a criteria count operation that returns value.
   *
   * @param entityManager
   * @param cb
   * @param entityClass
   * @param predications
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static int count(EntityManager entityManager, CriteriaBuilder cb, Class entityClass,
                          Predication... predications) {
    try {
      CriteriaQuery<Long> cq = queryCount(cb, entityClass, predications);
      return entityManager.createQuery(cq).getSingleResult().intValue();
    } catch (NoResultException ex) {
      return 0;
    }
  }

}
