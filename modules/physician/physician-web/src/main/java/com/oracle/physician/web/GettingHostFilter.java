package com.oracle.physician.web;

import com.oracle.medrec.common.util.ServerPropertiesUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * This Filter hereby is just for demo itself. Filter catches host, port, context path.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@WebFilter(urlPatterns = {"/*"})
public class GettingHostFilter implements Filter {

  private String partition;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // see if webapp is being in the MT env with uri-based routing
    partition = filterConfig.getServletContext().getContextPath();
    int tag = partition.indexOf("/medrec/physician");
    if (tag == 0) {
      partition = "/";
    } else {
      partition = partition.substring(0, tag + 1);
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    // preserve host:port/ or host:port/partition/ (if MT uri-based routing) for web service client
    if (ServerPropertiesUtils.getRegion() == null || ServerPropertiesUtils.getRegion().equals("")) {
      StringBuilder builder = new StringBuilder();
      builder.append(request.getServerName());
      builder.append(":");
      builder.append(String.valueOf(request.getServerPort()));
      builder.append(partition);
      ServerPropertiesUtils.setRegion(builder.toString());
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }
}
