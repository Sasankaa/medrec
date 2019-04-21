package io.helidon.medrec.patient.dataimport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.oracle.medrec.model.Address;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.model.PersonName;

import java.util.Calendar;

/**
 * Now we simply hard code all the data inside Java code.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public final class DataImporter {


  public static void importData() {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PatientServiceEM");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();

    try {
      cleanData(entityManager);
      importPatients(entityManager);
      entityTransaction.commit();
    } catch (RuntimeException e) {
      if (entityTransaction.isActive()) {
        entityTransaction.rollback();
      }
      System.out.println("The data import failed!");
      throw e;
    } finally {
      try {
        entityManager.close();
      } catch (Exception e) {
      }
    }
    System.out.println("All the data has been imported successfully!");
  }

  private static void cleanData(EntityManager entityManager) {
    entityManager.createQuery("delete from Patient p").executeUpdate();
    entityManager.flush();
  }

  private static void importPatients(EntityManager entityManager) {
    Patient patient = new Patient();
    patient.setPassword("weblogic");
    Calendar dob = Calendar.getInstance();
    dob.set(1975, 3, 26, 17, 18, 36);
    patient.setDob(dob.getTime());
    Address address = new Address();
    address.setCity("San Francisco");
    address.setCountry("United States");
    address.setState("California");
    address.setStreet1("1224 Post St");
    address.setStreet2("Suite 100");
    address.setZip("94115");
    patient.setAddress(address);
    patient.setSsn("123456789");
    patient.setGender(Patient.Gender.MALE);
    patient.setEmail("fred@golf.com");
    PersonName name = new PersonName();
    name.setFirstName("Fred");
    name.setMiddleName("I");
    name.setLastName("Winner");
    patient.setName(name);
    patient.setPhone("4151234564");
    patient.setStatus(Patient.Status.APPROVED);
    entityManager.persist(patient);

    patient = new Patient();
    patient.setPassword("weblogic");
    dob = Calendar.getInstance();
    dob.set(1969, 2, 13, 12, 9, 24);
    patient.setDob(dob.getTime());
    address = new Address();
    address.setCity("San Francisco");
    address.setCountry("United States");
    address.setState("California");
    address.setStreet1("1224 Post St");
    address.setStreet2("Suite 100");
    address.setZip("94115");
    patient.setAddress(address);
    patient.setSsn("777777777");
    patient.setGender(Patient.Gender.MALE);
    patient.setEmail("larry@bball.com");
    name = new PersonName();
    name.setFirstName("Larry");
    name.setMiddleName("J");
    name.setLastName("Parrot");
    patient.setName(name);
    patient.setPhone("4151234564");
    patient.setStatus(Patient.Status.APPROVED);
    entityManager.persist(patient);
    
    patient = new Patient();
    patient.setPassword("weblogic");
    dob = Calendar.getInstance();
    dob.set(1983, 10, 29, 23, 14, 31);
    patient.setDob(dob.getTime());
    address = new Address();
    address.setCity("Ponte Verde");
    address.setCountry("United States");
    address.setState("Florida");
    address.setStreet1("235 Montgomery St");
    address.setStreet2("Suite 15");
    address.setZip("32301");
    patient.setAddress(address);
    patient.setSsn("444444444");
    patient.setGender(Patient.Gender.MALE);
    patient.setEmail("charlie@star.com");
    name = new PersonName();
    name.setFirstName("Charlie");
    name.setMiddleName("E");
    name.setLastName("Florida");
    patient.setName(name);
    patient.setPhone("4151234564");
    entityManager.persist(patient);
    patient = new Patient();
    patient.setPassword("weblogic");
    dob = Calendar.getInstance();
    dob.set(1981, 8, 17, 9, 4, 55);
    patient.setDob(dob.getTime());
    address = new Address();
    address.setCity("San Francisco");
    address.setCountry("United States");
    address.setState("California");
    address.setStreet1("1224 Post St");
    address.setStreet2("Suite 100");
    address.setZip("94115");
    patient.setAddress(address);
    patient.setSsn("333333333");
    patient.setGender(Patient.Gender.MALE);
    patient.setEmail("volley@ball.com");
    name = new PersonName();
    name.setFirstName("Gabrielle");
    name.setMiddleName("H");
    name.setLastName("Spiker");
    patient.setName(name);
    patient.setPhone("4151234564");
    patient.setStatus(Patient.Status.APPROVED);
    entityManager.persist(patient);

    patient = new Patient();
    patient.setPassword("weblogic");
    dob = Calendar.getInstance();
    dob.set(1982, 2, 18, 12, 17, 41);
    patient.setDob(dob.getTime());
    address = new Address();
    address.setCity("Ponte Verde");
    address.setCountry("United States");
    address.setState("Florida");
    address.setStreet1("235 Montgomery St");
    address.setStreet2("Suite 15");
    address.setZip("32301");
    patient.setAddress(address);
    patient.setSsn("888888888");
    patient.setGender(Patient.Gender.MALE);
    patient.setEmail("page@fish.com");
    name = new PersonName();
    name.setFirstName("Page");
    name.setMiddleName("A");
    name.setLastName("Trout");
    patient.setName(name);
    patient.setPhone("4151234564");
    patient.setStatus(Patient.Status.APPROVED);
    entityManager.persist(patient);

  }

}
