package com.oracle.medrec.service.impl;

import com.oracle.medrec.common.persistence.CriteriaPersistenceSupport;
import com.oracle.medrec.common.persistence.PredicationFactory;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.DuplicateSsnException;
import com.oracle.medrec.service.DuplicateUsernameException;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.service.impl.notification.PatientNotifier;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * A patient business service implementation which is responsible for all
 * business operations to patient.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
public class PatientServiceImpl extends BaseUserServiceImpl<Patient> implements PatientService {

  @EJB(beanName = "PatientNotifierDelegate")
  private PatientNotifier patientNotifier;

  public void createPatient(Patient patient) throws DuplicateUsernameException, DuplicateSsnException {
    isDuplicateSsn(patient);
    super.createUser(patient);
  }

  public Patient getPatient(Long patientId) {
    // Refresh patient, to ensure that we see updates that may have 
    // been made by other cluster members
    Patient patient =  entityManager.find(entityClass, patientId);
    entityManager.refresh(patient);
    return patient;
  }

  public Patient findApprovedPatientBySsn(String ssn) {
    return CriteriaPersistenceSupport.findUnique(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(ssn, "ssn"), PredicationFactory.createEqualPredication(Patient
            .Status.APPROVED, "status"));
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<Patient> findApprovedPatientsByLastName(String lastName) {
    return CriteriaPersistenceSupport.find(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(lastName, "name", "lastName"),
        PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));
  }

  public boolean authenticatePatient(String username, String password) {
    int number = CriteriaPersistenceSupport.count(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(username, "username"), PredicationFactory.createEqualPredication
            (password, "password"), PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));
    return (number == 1);
  }

  public Patient authenticateAndReturnPatient(String username, String password) {
    return CriteriaPersistenceSupport.findUnique(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(username, "username"), PredicationFactory.createEqualPredication
            (password, "password"), PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void approvePatient(Long patientId) {
    Patient patient = getPatient(patientId);
    patient.approve();
    super.update(patient);
    patientNotifier.notifyPatient(patient);
  }

  public List<Patient> getNewlyRegisteredPatients() {
    return CriteriaPersistenceSupport.find(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(Patient.Status.REGISTERED, "status"));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void denyPatient(Long patientId) {
    Patient patient = getPatient(patientId);
    patient.deny();
    super.update(patient);
    patientNotifier.notifyPatient(patient);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Patient updatePatient(Patient patient) throws DuplicateSsnException {
    // if ssn has been changed
    if (patient.isSsnChanged()) {
      isDuplicateSsn(patient);
    }
    patient = super.update(patient);
    patient.setSsnChanged(false);
    return patient;
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<Patient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn) {
    return CriteriaPersistenceSupport.find(entityManager, criteriaBuilder, entityClass, 0, 10,
        PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"),
        PredicationFactory.createLikePredication(lastName + "%", "name", "lastName"),
        PredicationFactory.createLikePredication(ssn + "%", "ssn"));
  }

  /**
   * Find out if the ssn of this patient has existed in database. If it has,
   * throw @{link.DuplicateSsnException}.
   *
   * @param patient
   * @throws com.oracle.medrec.service.DuplicateSsnException
   */
  private void isDuplicateSsn(Patient patient) throws DuplicateSsnException {
    // count patient with this ssn
    int ssnExistedAmount = CriteriaPersistenceSupport.count(entityManager, criteriaBuilder, entityClass,
        PredicationFactory.createEqualPredication(patient.getSsn(), "ssn"));
    // if the very ssn has existed in database
    if (ssnExistedAmount > 0) {
      throw new DuplicateSsnException(patient.getSsn());
    }
  }

}
