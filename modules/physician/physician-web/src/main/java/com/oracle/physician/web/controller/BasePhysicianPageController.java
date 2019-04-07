package com.oracle.physician.web.controller;

import com.oracle.medrec.common.web.PageContext;

import javax.inject.Inject;

/**
 * todo remove
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class BasePhysicianPageController {

  @Inject
  private PageContext pageContext;

  protected PageContext getPageContext() {
    return pageContext;
  }
}
