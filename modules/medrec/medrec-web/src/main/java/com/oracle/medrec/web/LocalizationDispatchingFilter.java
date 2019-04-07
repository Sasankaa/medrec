package com.oracle.medrec.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@WebFilter("/start.html")
public class LocalizationDispatchingFilter implements Filter {

  // TODO try to make it generic

  private static final String REDIRECT_PARAM = "redirect";

  private static final Logger LOGGER = Logger.getLogger(LocalizationDispatchingFilter.class.getName());

  private FilterConfig filterConfig;

  /**
   * Perform "forward" by default
   */
  private boolean redirect = false;

  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
    String redirectString = filterConfig.getInitParameter(REDIRECT_PARAM);
    if (redirectString != null) {
      if ("true".equalsIgnoreCase(redirectString)) {
        redirect = true;
      } else if ("false".equalsIgnoreCase(redirectString)) {
        redirect = false;
      } else {
        throw new IllegalArgumentException("The value for the initial paramter '" + REDIRECT_PARAM + "' is invalid");
      }
    }
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
      ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    Locale locale = httpRequest.getLocale();

    LOGGER.finer("The locale of the current client: " + locale.toString());

    // ignore performance

    String pathToLocalizedResource = null;

    if (null != locale.getCountry() && !"".equals(locale.getCountry())) {
      pathToLocalizedResource = "/start_" + locale.getLanguage().toLowerCase() + "_" + locale.getCountry()
          .toUpperCase() + ".xhtml";
      if (filterConfig.getServletContext().getResource(pathToLocalizedResource) == null) {
        pathToLocalizedResource = null;
      }
    }
    if (pathToLocalizedResource == null) {
      pathToLocalizedResource = "/start_" + locale.getLanguage().toLowerCase() + ".xhtml";
      if (filterConfig.getServletContext().getResource(pathToLocalizedResource) == null) {
        pathToLocalizedResource = null;
      }
    }

    if (pathToLocalizedResource == null) {
      filterChain.doFilter(request, response);
    } else {
      if (redirect) {
        httpResponse.sendRedirect(httpRequest.getContextPath() + pathToLocalizedResource);
      } else {
        filterConfig.getServletContext().getRequestDispatcher(pathToLocalizedResource).forward(httpRequest,
            httpResponse);
      }
    }
  }

  public void destroy() {
  }
}
