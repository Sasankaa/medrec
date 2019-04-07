package com.oracle.medrec.common.persistence;

import javax.persistence.metamodel.SingularAttribute;

/**
 * {@link Predication} factory.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class PredicationFactory {

  /**
   * Create equal '=' SQL {@link EqualPredication} by String attribute array.
   *
   * @param value
   * @param attribute
   * @return
   */
  public static final Predication<Object> createEqualPredication(Object value, String... attribute) {
    return new EqualPredication(value, attribute);
  }

  /**
   * Create equal '=' SQL {@link EqualPredication} by JPA MedaModel array.
   *
   * @param value
   * @param attribute
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static final Predication<Object> createEqualPredication(Object value, SingularAttribute... attribute) {
    return new EqualPredication(value, attribute);
  }

  /**
   * Create 'like' SQL {@link LikePredication} by String array.
   *
   * @param value
   * @param attribute
   * @return
   */
  public static final Predication<String> createLikePredication(String value, String... attribute) {
    return new LikePredication(value, attribute);
  }

  /**
   * Create 'like' SQL {@link LikePredication} by JPA MedaModel array.
   *
   * @param value
   * @param attribute
   * @return
   */
  public static final Predication<String> createLikePredication(String value, SingularAttribute<Object,
      String>... attribute) {
    return new LikePredication(value, attribute);
  }

}
