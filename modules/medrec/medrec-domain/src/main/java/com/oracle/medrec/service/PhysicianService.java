package com.oracle.medrec.service;

import com.oracle.medrec.model.Physician;

import javax.ejb.Local;

/**
 * Physician business service interface.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Local
public interface PhysicianService {

  /**
   * Authenticates physician by user name and password.
   *
   * @param username
   * @param password
   * @return
   */
  boolean authenticatePhysician(String username, String password);

  /**
   * Authenticates physician by user name and password. Then return
   * {@link com.oracle.medrec.model.Physician} entity.
   *
   * @param username
   * @param password
   * @return
   */
  Physician authenticateAndReturnPhysician(String username, String password);

}
