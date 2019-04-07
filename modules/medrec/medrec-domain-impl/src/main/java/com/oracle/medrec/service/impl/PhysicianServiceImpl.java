package com.oracle.medrec.service.impl;

import com.oracle.medrec.model.Physician;
import com.oracle.medrec.service.PhysicianService;

import javax.ejb.Stateless;

/**
 * Physician buisness service implementation. which is responsible for all
 * business operations to physicain.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
public class PhysicianServiceImpl extends BaseUserServiceImpl<Physician> implements PhysicianService {

  /**
   * Not used now.
   */
  public boolean authenticatePhysician(String username, String password) {
    return super.authenticateUser(username, password);
  }

  public Physician authenticateAndReturnPhysician(String username, String password) {
    return super.authenticateAndReturnUser(username, password);
  }

}
