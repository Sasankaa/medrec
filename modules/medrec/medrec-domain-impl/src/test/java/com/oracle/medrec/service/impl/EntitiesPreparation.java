package com.oracle.medrec.service.impl;

import com.oracle.medrec.model.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
public class EntitiesPreparation {

  public static Patient createPatient() {
    return createPatient("a@oracle.com", "SSN000000");
  }

  public static Patient createPatient(String email, String ssn) {
    Patient user = new Patient();
    user.setEmail(email);
    user.setPassword("password");
    user.setGender(Patient.Gender.FEMALE);
    user.setDob(new Date());
    user.setSsn(ssn);
    user.setPhone("11111111");
    user.approve();

    Address address = new Address();
    address.setCity("city");
    address.setCountry("country");
    address.setState("state");
    address.setStreet1("street1");
    address.setStreet2("street2");
    address.setZip("11111");
    user.setAddress(address);

    user.setName(createName());
    return user;
  }

  public static Physician createPhysician() {
    Physician user = new Physician();
    user.setPassword("password");
    user.setEmail("a@oracle.com");
    user.setName(createName());
    user.setPhone("11111111");
    return user;
  }

  public static PersonName createName() {
    return createName("firstname", "middlename", "lastname");
  }

  public static PersonName createName(String first, String middle, String last) {
    PersonName name = new PersonName();
    name.setFirstName(first);
    name.setMiddleName(middle);
    name.setLastName(last);
    return name;
  }

  public static Record createRecord() {
    return createRecord(createPatient(), createPhysician(), createDrug());
  }

  public static Record createRecord(Patient patient, Physician physician, Drug drug) {
    return createRecord(patient, physician, drug, new Date());
  }

  public static Record createRecord(Patient patient, Physician physician, Drug drug, Date date) {
    Record rd = new Record();
    rd.setDate(date);
    rd.setDiagnosis("diagnosis");
    rd.setNotes("notes");
    rd.setSymptoms("symptoms");
    VitalSigns vitalSigns = new VitalSigns();
    vitalSigns.setDiastolicBloodPressure(100.0);
    vitalSigns.setHeight(100.0);
    vitalSigns.setPulse(80.0);
    vitalSigns.setSystolicBloodPressure(120.0);
    vitalSigns.setTemperature(100.0);
    vitalSigns.setWeight(100.0);
    rd.setPhysician(physician);
    rd.setPatient(patient);
    Prescription prescription = new Prescription();
    prescription.setDatePrescribed(new Date());
    prescription.setDosage(50);
    prescription.setDrug(drug);
    prescription.setFrequency("frequency");
    prescription.setInstructions("instructions");
    prescription.setRefillsRemaining(1);
    rd.addPrescription(prescription);
    return rd;
  }


  public static Drug createDrug() {
    Drug drug = new Drug();
    drug.setName("Drixoral");
    drug.setPrice(new BigDecimal("2.00"));
    return drug;
  }

}
