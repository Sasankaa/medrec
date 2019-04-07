package com.oracle.medrec.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Entity
@Table(name = "drugs")
@NamedQuery(name = "Drug.findAllDrugs", query = "SELECT d FROM Drug d ORDER BY d.id")
public class Drug extends VersionedEntity {

  private static final long serialVersionUID = 3610658213331393425L;

  @NotNull
  private String name;

  @NotNull
  private BigDecimal price;

  public void setID(long id) {
    super.setId(id);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // todo producer mock. It needs to add Producer column and table
  public String getProducer() {
    return "Oracle";
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

}
