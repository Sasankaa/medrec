package com.oracle.medrec.common.persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

/**
 * A predicate for testing the arguments for equality, namely '=' SQL.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class EqualPredication extends Predication<Object> {

  public EqualPredication(Object value, String... attribute) {
    super(value, attribute);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public EqualPredication(Object value, SingularAttribute... attribute) {
    super(value, attribute);
  }

  @Override
  protected Predicate createPredicate(CriteriaBuilder cb) {
    return cb.equal(path, value);
  }
}
