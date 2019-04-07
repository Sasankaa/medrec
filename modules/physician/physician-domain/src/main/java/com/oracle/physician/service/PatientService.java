package com.oracle.physician.service;

import com.oracle.medrec.facade.PatientFacade;

/**
 * That this service interface directly inherit from the medrec facade interface
 * is just for simplicity here, since we don't want to re-create another set of
 * interfaces and models in this sample app. For real-world apps, (webservices)
 * clients are totally decoupled from server side, and should have their object
 * models (maybe in different language).
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface PatientService extends PatientFacade {

}
