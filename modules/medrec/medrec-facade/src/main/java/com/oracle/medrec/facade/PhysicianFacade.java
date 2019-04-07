package com.oracle.medrec.facade;

import com.oracle.medrec.facade.model.AuthenticatedPhysician;

import javax.ejb.Local;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Local
public interface PhysicianFacade {

  boolean authenticatePhysician(String username, String password);

  AuthenticatedPhysician authenticateAndReturnPhysician(String username, String password);
}
