package io.helidon.medrec.physician.dataimport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.oracle.medrec.model.PersonName;
import com.oracle.medrec.model.Physician;


/**
 * Now we simply hard code all the data inside Java code.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its affiliates. All rights
 *         reserved.
 */
public final class DataImporter {

	public static void importData() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PhysicianService");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		try {
			cleanData(entityManager);
			importPhysicians(entityManager);
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
		entityManager.createQuery("delete from Physician p").executeUpdate();
		entityManager.flush();
	}

	private static void importPhysicians(EntityManager entityManager) {
		Physician physician = new Physician();

		physician.setPassword("weblogic");
		physician.setEmail("mary@md.com");
		PersonName name = new PersonName();
		name.setFirstName("Mary");
		name.setMiddleName("J");
		name.setLastName("Oblige");
		physician.setName(name);
		physician.setPhone("1234567812");
		entityManager.persist(physician);

	}
}
