package com.oracle.medrec.common.persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

/**
 * A predicate for testing whether the expression satisfies the given pattern,
 * namely 'like' SQL.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class LikePredication extends Predication<String> {

  public LikePredication(String value, String... attribute) {
    super(value, attribute);
  }

  public LikePredication(String value, SingularAttribute<Object, String>... attribute) {
    super(value, attribute);
  }

  @Override
  protected Predicate createPredicate(CriteriaBuilder cb) {
    return cb.like(path, value);
  }
}
