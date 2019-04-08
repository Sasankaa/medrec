/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.medrec.patient;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import com.oracle.medrec.common.persistence.CriteriaPersistenceSupport;
import com.oracle.medrec.common.persistence.PredicationFactory;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.DuplicateSsnException;
import com.oracle.medrec.service.DuplicateUsernameException;
import com.oracle.medrec.service.PatientService;
import com.oracle.medrec.service.impl.BaseUserServiceImpl;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Provider for Patients.
 */
@ApplicationScoped
public class PatientProvider extends BaseUserServiceImpl<Patient> implements PatientService {

    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    private Config config = ConfigProvider.getConfig();

    @Produces
    private EntityManager createEntityManager() {
        return Persistence.createEntityManagerFactory(config.getValue("persistence.unit.name", String.class))
                .createEntityManager();
    }

    public PatientProvider() {
        super();
        entityManager = createEntityManager();
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public void createPatient(Patient patient) throws DuplicateUsernameException, DuplicateSsnException {
        isDuplicateSsn(patient);
        entityManager.getTransaction().begin();
        entityManager.persist(patient);
        entityManager.getTransaction().commit();
        //return patient.getId();
    }

    @Override
    public Patient getPatient(Long patientId) {
        Patient patient = entityManager.find(entityClass, patientId);
        entityManager.refresh(patient);
        return patient;
    }

    @Override
    public Patient findApprovedPatientBySsn(String ssn) {
        return CriteriaPersistenceSupport.findUnique(entityManager, criteriaBuilder, entityClass,
                PredicationFactory.createEqualPredication(ssn, "ssn"),
                PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));
    }

    @Override
    public List<Patient> findApprovedPatientsByLastName(String lastName) {
        return CriteriaPersistenceSupport.find(entityManager, criteriaBuilder, entityClass,
                PredicationFactory.createEqualPredication(lastName, "name", "lastName"),
                PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));
    }

    @Override
    public List<Patient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn) {
        return CriteriaPersistenceSupport.find(entityManager, criteriaBuilder, entityClass, 0, 10,
                PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"),
                PredicationFactory.createLikePredication(lastName + "%", "name", "lastName"),
                PredicationFactory.createLikePredication(ssn + "%", "ssn"));
    }

    @Override
    public boolean authenticatePatient(String username, String password) {
        int number = CriteriaPersistenceSupport.count(entityManager, criteriaBuilder, entityClass,
                PredicationFactory.createEqualPredication(username, "username"),
                PredicationFactory.createEqualPredication(password, "password"),
                PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));
        return (number == 1);
    }

    @Override
    public void approvePatient(Long patientId) {
        Patient patient = getPatient(patientId);
        entityManager.getTransaction().begin();
        patient.approve();
        entityManager.merge(patient);
        entityManager.getTransaction().commit();
        
        // patientNotifier.notifyPatient(patient);
    }

    @Override
    public List<Patient> getNewlyRegisteredPatients() {
        return CriteriaPersistenceSupport.find(entityManager, criteriaBuilder, entityClass,
                PredicationFactory.createEqualPredication(Patient.Status.REGISTERED, "status"));
    }

    @Override
    public void denyPatient(Long patientId) {
        Patient patient = getPatient(patientId);
        entityManager.getTransaction().begin();
        patient.deny();
        entityManager.merge(patient);
        entityManager.getTransaction().commit();
        
        // super.update(patient);
        // patientNotifier.notifyPatient(patient);
    }

    @Override
    public Patient authenticateAndReturnPatient(String username, String password) {
        return CriteriaPersistenceSupport.findUnique(entityManager, criteriaBuilder, entityClass,
                PredicationFactory.createEqualPredication(username, "username"),
                PredicationFactory.createEqualPredication(password, "password"),
                PredicationFactory.createEqualPredication(Patient.Status.APPROVED, "status"));

    }

    @Override
    public Patient updatePatient(Patient patient) throws DuplicateSsnException {
        // if ssn has been changed
        if (patient.isSsnChanged()) {
            isDuplicateSsn(patient);
        }

        entityManager.getTransaction().begin();
        patient = entityManager.merge(patient);
        entityManager.getTransaction().commit();

        patient.setSsnChanged(false);
        
        return patient;
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
