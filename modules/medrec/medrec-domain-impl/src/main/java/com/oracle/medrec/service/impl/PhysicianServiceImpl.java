package com.oracle.medrec.service.impl;

import java.util.logging.Logger;

import javax.ejb.Stateless;

import com.oracle.medrec.model.Physician;
import com.oracle.medrec.service.PhysicianService;

/**
 * Physician buisness service implementation. which is responsible for all
 * business operations to physicain.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its affiliates. All rights
 *         reserved.
 */
@Stateless
public class PhysicianServiceImpl extends BaseUserServiceImpl<Physician> implements PhysicianService {

	private static final Logger LOGGER = Logger.getLogger(PhysicianServiceImpl.class.getName());

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
