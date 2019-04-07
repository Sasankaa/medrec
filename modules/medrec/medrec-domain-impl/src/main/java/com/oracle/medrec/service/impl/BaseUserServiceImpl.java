package com.oracle.medrec.service.impl;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.oracle.medrec.common.persistence.CriteriaPersistenceService;
import com.oracle.medrec.common.persistence.CriteriaPersistenceSupport;
import com.oracle.medrec.common.persistence.PredicationFactory;
import com.oracle.medrec.common.util.ClassUtils;
import com.oracle.medrec.model.User;
import com.oracle.medrec.model.User_;
import com.oracle.medrec.service.DuplicateUsernameException;

/**
 * Contains operations about {@link User}. If the business service manages the
 * entity which extends {@link User}, it should extend this abstract class.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public abstract class BaseUserServiceImpl<T extends User> extends CriteriaPersistenceService {

  @SuppressWarnings("unchecked")
  protected final Class<T> entityClass = ClassUtils.getGenericArgumentType(getClass());

  /**
   * Creates new user to the database. If the use name is duplicated, then
   * throw {@link DuplicateUsernameException}.
   *
   * @param user
   * @throws DuplicateUsernameException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  protected void createUser(T user) throws DuplicateUsernameException {
    isDuplicateUser(user);
    // If duplicate user is persisted after existence checking, just leave
    // the save operation failing
    super.save(user);
  }

  /**
   * Authenticates username and password.
   *
   * @param username
   * @param password
   * @return
   */
  protected boolean authenticateUser(String username, String password) {
    int number = CriteriaPersistenceSupport.count(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(username, User_.username),
        PredicationFactory.createEqualPredication(password, User_.password));
    return (number == 1);
  }

  /**
   * Authenticates username and password. Then return User T.
   *
   * @param username
   * @param password
   * @return
   */
  protected T authenticateAndReturnUser(String username, String password) {
    return CriteriaPersistenceSupport.findUnique(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(username, User_.username),
        PredicationFactory.createEqualPredication(password, User_.password));
  }

  /**
   * Finds out if the user name has existed in database. If it has, throw
   * {@link DuplicateUsernameException}.
   *
   * @param user
   * @throws DuplicateUsernameException
   */
  private void isDuplicateUser(T user) throws DuplicateUsernameException {
    // count user with this user name
    if (CriteriaPersistenceSupport.count(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(user.getUsername(), User_.username)) > 0) {
      throw new DuplicateUsernameException(user.getUsername());
    }
  }

}
