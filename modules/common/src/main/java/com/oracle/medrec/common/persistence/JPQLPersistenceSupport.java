package com.oracle.medrec.common.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Convenient JPA query language base class for generic repository (DAO).
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class JPQLPersistenceSupport {

  public static <T> T findByUniqueProperty(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                           String propertyName, Object propertyValue) {
    return findByUniqueProperties(entityManager, entityClass, namedQuery, new Property(propertyName, propertyValue));
  }

  public static <T> T findByUniqueProperty(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                           Object propertyValue) {
    return findByUniqueProperties(entityManager, entityClass, namedQuery, propertyValue);
  }

  @SuppressWarnings("unchecked")
  public static <T> T findByUniqueProperties(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                             Object... propertyValues) {
    try {
      return (T) createNamedQuery(entityManager, namedQuery, propertyValues).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static <T> T findByUniqueProperties(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                             Property... properties) {
    return findByUniqueProperties(entityManager, entityClass, namedQuery, (Object[]) properties);
  }

  public static <T> List<T> findByProperty(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                           String propertyName, Object propertyValue) {
    return findByProperties(entityManager, entityClass, namedQuery, new Property(propertyName, propertyValue));
  }

  public static <T> List<T> findByProperty(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                           Object propertyValue) {
    return findByProperties(entityManager, entityClass, namedQuery, propertyValue);
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> findByProperties(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                             Object... propertyValues) {
    return createNamedQuery(entityManager, namedQuery, propertyValues).getResultList();
  }

  public static <T> List<T> findByProperties(EntityManager entityManager, Class<T> entityClass, String namedQuery,
                                             Property... properties) {
    return findByProperties(entityManager, entityClass, namedQuery, (Object[]) properties);
  }

  public static int countByProperty(EntityManager entityManager, String namedQuery, String propertyName,
                                    Object propertyValue) {
    return countByProperties(entityManager, namedQuery, new Property(propertyName, propertyValue));
  }

  public static int countByProperty(EntityManager entityManager, String namedQuery, Object propertyValue) {
    return countByProperties(entityManager, namedQuery, propertyValue);
  }

  public static int countByProperties(EntityManager entityManager, String namedQuery, Property... properties) {
    return countByProperties(entityManager, namedQuery, (Object[]) properties);
  }

  public static int countByProperties(EntityManager entityManager, String namedQuery, Object... propertyValues) {
    Long count = (Long) createNamedQuery(entityManager, namedQuery, propertyValues).getSingleResult();
    return count.intValue();
  }

  private static Query createNamedQuery(EntityManager entityManager, String namedQuery, Object... propertyValues) {
    Query query = entityManager.createNamedQuery(namedQuery);
    int i = 1;
    for (Object propertyValue : propertyValues) {
      if (propertyValue instanceof Property) {
        Property property = (Property) propertyValue;
        query.setParameter(property.name, property.value);
      } else {
        query.setParameter(i, propertyValue);
      }
      i++;
    }
    return query;
  }

  public static class Property {

    public String name;

    public Object value;

    public Property(String name, Object value) {
      this.name = name;
      this.value = value;
    }
  }
}
