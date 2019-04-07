package com.oracle.physician.service;

import com.oracle.medrec.facade.model.AuthenticatedPhysician;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface PhysicianService {

  AuthenticatedPhysician authenticateAndReturnPhysician(String username, String password);

}
