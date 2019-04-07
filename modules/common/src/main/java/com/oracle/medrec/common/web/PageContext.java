package com.oracle.medrec.common.web;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

/**
 * Class that decouples common operations on something like FacesContext from
 * page controllers to ease the unit tests with mock objects. And also, these
 * operations could be used in something like filter servlets.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface PageContext extends Serializable {

  FacesContext getFacesContext();

  Map<String, Object> getSessionMap();

  Map<String, Object> getRequestMap();

  String getRequestParam(String name);

  void invalidateSession();

  void addGlobalOnlyMessage(String message);

  void addGlobalOnlyErrorMessage(String message);

  void addGlobalOnlyInfoMessage(String message);

  void addGlobalOnlyMessageWithKey(String messageKey, Object... parameters);

  void addGlobalOnlyErrorMessageWithKey(String messageKey, Object... parameters);

  void addGlobalOnlyInfoMessageWithKey(String messageKey, Object... parameters);

  String getMessage(String messageKey, Object... parameters);
}
