package com.oracle.physician.web;

import com.oracle.medrec.facade.model.AuthenticatedPhysician;

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
@WebFilter(urlPatterns = {"/physician/*"})
public class AuthorizationFilter implements Filter {

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
      ServletException {
    Object user = ((HttpServletRequest) request).getSession().getAttribute(Constants.AUTHENTICATED_USER_SESSION_KEY);
    if (user == null || !(user instanceof AuthenticatedPhysician)) {
      ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + Constants
          .LOGIN_PATH);
    } else {
      filterChain.doFilter(request, response);
    }
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }
}
