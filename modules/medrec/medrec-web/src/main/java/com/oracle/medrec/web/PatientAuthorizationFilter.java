package com.oracle.medrec.web;

import com.oracle.medrec.model.Patient;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A Servlet Filter for authenticate if the user can request certain URI.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@WebFilter(urlPatterns = {"/patient/*"})
public class PatientAuthorizationFilter implements Filter {

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
      ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    Object user = httpRequest.getSession().getAttribute(Constants.AUTHENTICATED_USER_SESSION_KEY);

    if (httpRequest.getRequestURI().contains(Constants.PATIENT_BASE_PATH) && (user == null || !(user instanceof
        Patient))) {
      httpResponse.sendRedirect(httpRequest.getContextPath() + Constants.INDEX_PATH);
      return;
    }

    filterChain.doFilter(request, response);
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }
}