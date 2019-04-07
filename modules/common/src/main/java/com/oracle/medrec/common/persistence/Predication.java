package com.oracle.medrec.common.persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Decorating the criteria query {@link Predicate}. Subclasses need to support
 * SQL predicates in 'where' clause respectively. E.g. '=', 'like'.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @see {@link EqualPredication}, {@link LikePredication}.
 */
public abstract class Predication<P> {

  protected String[] attribute;

  protected SingularAttribute<Object, P>[] singular;

  protected P value;

  protected Path<P> path = null;

  /**
   * Prediction constructor. As for the attribute array, an entity may has a
   * field Name which is an object and contains inner attribute. For instance,
   * a User entity has an Object attribute 'name', And the 'name' has a String
   * attribute 'lastName'. If you want to use 'lastName' as key, the attribute
   * array likes {"name", "lastName"}.
   *
   * @param value     - P field value.
   * @param attribute - String... attribute path with String.
   */
  public Predication(P value, String... attribute) {
    this.attribute = attribute;
    this.value = value;
  }

  /**
   * Prediction constructor. Similar to the Predication(P value, String...
   * attribute), but using {@link SingularAttribute}, JPA MetaModel.
   *
   * @param value
   * @param attribute
   */
  public Predication(P value, SingularAttribute<Object, P>... attribute) {
    this.singular = attribute;
    this.value = value;
  }

  /**
   * Obtain the {@link Predicate} from Predication.
   *
   * @param cb
   * @param root
   * @return
   */
  protected <T> Predicate getPredicate(CriteriaBuilder cb, Root<T> root) {
    if (singular == null || singular.length == 0) {
      for (String att : attribute) {
        if (path == null) {
          path = root.get(att);
        } else {
          path = path.get(att);
        }
      }
    } else {
      for (SingularAttribute<Object, P> att : singular) {
        if (path == null) {
          path = root.get(att);
        } else {
          path = path.get(att);
        }
      }
    }
    return createPredicate(cb);
  }

  /**
   * Sub Class should implement this method to create specified
   * {@link Predicate}.
   *
   * @param cb
   * @return
   */
  protected abstract Predicate createPredicate(CriteriaBuilder cb);
}
