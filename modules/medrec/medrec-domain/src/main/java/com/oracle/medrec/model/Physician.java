package com.oracle.medrec.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Entity
@Table(name = "physicians")
public class Physician extends RegularUser {

  private static final long serialVersionUID = 2298019356529544739L;

}
