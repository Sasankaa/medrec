package com.oracle.medrec.service;

import com.oracle.medrec.model.Patient;

import javax.ejb.Local;
import java.util.List;

/**
 * Patient business service interface.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Local
public interface PatientService {

  /**
   * Creates one patient.
   *
   * @param patient
   * @throws DuplicateUsernameException
   * @throws DuplicateSsnException
   */
  void createPatient(Patient patient) throws DuplicateUsernameException, DuplicateSsnException;

  /**
   * Retrieves a patient by id.
   *
   * @param patientId
   * @return
   */
  Patient getPatient(Long patientId);

  /**
   * Retrieves the patient approved by ssn.
   *
   * @param ssn
   * @return
   */
  Patient findApprovedPatientBySsn(String ssn);

  /**
   * Retrieves the patient approved by last name.
   *
   * @param lastName
   * @return
   */
  List<Patient> findApprovedPatientsByLastName(String lastName);

  /**
   * Retrieves patients approved whose last name starts as param lastName and
   * ssn starts as param ssn.
   *
   * @param lastName
   * @param ssn
   * @return
   */
  List<Patient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn);

  /**
   * Authenticates patient by user name and password.
   *
   * @param username
   * @param password
   * @return
   */
  boolean authenticatePatient(String username, String password);

  /**
   * Approves pending patient.
   *
   * @param patientId
   */
  void approvePatient(Long patientId);

  /**
   * Gets the newly pending patients.
   *
   * @return
   */
  List<Patient> getNewlyRegisteredPatients();

  /**
   * Denies pending patient.
   *
   * @param patientId
   */
  void denyPatient(Long patientId);

  /**
   * Authenticates patient by user name and password. Then return the
   * {@link com.oracle.medrec.model.Patient} entity.
   *
   * @param username
   * @param password
   * @return
   */
  Patient authenticateAndReturnPatient(String username, String password);

  /**
   * Updates patient to datebase.
   *
   * @param patient
   * @return
   * @throws DuplicateSsnException
   */
  Patient updatePatient(Patient patient) throws DuplicateSsnException;

}
