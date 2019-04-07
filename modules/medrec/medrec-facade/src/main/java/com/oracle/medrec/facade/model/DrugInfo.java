package com.oracle.medrec.facade.model;

import com.oracle.medrec.model.Drug;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@XmlRootElement
public class DrugInfo extends TransferObject {

  private static final long serialVersionUID = 338578453534L;

  private Long id;

  private String name;

  public DrugInfo() {

  }

  public DrugInfo(Drug drug) {
    id = drug.getId();
    name = drug.getName();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
