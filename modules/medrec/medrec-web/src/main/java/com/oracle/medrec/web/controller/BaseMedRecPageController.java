package com.oracle.medrec.web.controller;

import com.oracle.medrec.common.web.PageContext;

import javax.inject.Inject;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class BaseMedRecPageController {

  @Inject
  private PageContext pageContext;

  protected PageContext getPageContext() {
    return pageContext;
  }
}
