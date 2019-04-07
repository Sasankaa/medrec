package com.oracle.medrec.common.web;

import com.oracle.medrec.common.core.SystemException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class that decouples common operations on something like FacesContext from
 * page controllers to ease the unit tests with mock objects. And also, these
 * operations could be used in something like filter servlets.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class PageContextImpl implements PageContext {

  private static final String INVALIDATE_SESSION_METHOD_NAME = "invalidate";

  /**
   * Not supposed to be directly accessed
   */
  private FacesContext facesContext;

  public FacesContext getFacesContext() {
    if (facesContext == null) {
      // Cannot hold the instance in backing bean if it's not
      // request-scoped
      return FacesContext.getCurrentInstance();
    }
    return facesContext;
  }

  /**
   * Just for unit test now.
   */
  public void setFacesContext(FacesContext facesContext) {
    this.facesContext = facesContext;
  }

  public Map<String, Object> getSessionMap() {
    return getFacesContext().getExternalContext().getSessionMap();
  }

  public Map<String, Object> getRequestMap() {
    return getFacesContext().getExternalContext().getRequestMap();
  }

  public String getRequestParam(String name) {
    return getFacesContext().getExternalContext().getRequestParameterMap().get(name);
  }

  public void invalidateSession() {
    // Use reflection so that we don't have to depend on servlet or portlet
    // API
    Object session = getFacesContext().getExternalContext().getSession(false);
    try {
      session.getClass().getMethod(INVALIDATE_SESSION_METHOD_NAME).invoke(session);
    } catch (IllegalAccessException e) {
      throw new SystemException("Cannot invalidate session", e);
    } catch (InvocationTargetException e) {
      throw new SystemException("Cannot invalidate session", e.getTargetException());
    } catch (NoSuchMethodException e) {
      throw new AssertionError("Cannot invalidate session, because the session of class " + session.getClass()
          .getName() + " doesn't contain method '" + INVALIDATE_SESSION_METHOD_NAME + "'");
    }
  }

  // TODO for now ignore severity and detail

  public void addGlobalOnlyMessage(String message) {
    getFacesContext().addMessage(null, new FacesMessage(message));
  }

  public void addGlobalOnlyErrorMessage(String message) {
    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));

  }

  public void addGlobalOnlyInfoMessage(String message) {
    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));

  }

  public void addGlobalOnlyMessageWithKey(String messageKey, Object... parameters) {
    addGlobalOnlyMessage(getMessage(messageKey, parameters));
  }

  public void addGlobalOnlyErrorMessageWithKey(String messageKey, Object... parameters) {
    addGlobalOnlyErrorMessage(getMessage(messageKey, parameters));
  }

  public void addGlobalOnlyInfoMessageWithKey(String messageKey, Object... parameters) {
    addGlobalOnlyInfoMessage(getMessage(messageKey, parameters));
  }

  public String getMessage(String messageKey, Object... parameters) {
    String bundleName = getFacesContext().getApplication().getMessageBundle();
    if (bundleName != null) {
      Locale locale = getFacesContext().getViewRoot().getLocale();
      ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getWebappClassLoader());
      // MissingResourceException will be thrown if not found
      String message = bundle.getString(messageKey);
      if (parameters != null) {
        MessageFormat messageFormat = new MessageFormat(message, locale);
        message = messageFormat.format(parameters, new StringBuffer(), null).toString();
      }
      return message;
    }
    throw new SystemException("Cannot find message bundle for the webapp");

  }

  private ClassLoader getWebappClassLoader() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      // Use the subclass's loader
      classLoader = getClass().getClassLoader();
    }
    return classLoader;
  }

}
