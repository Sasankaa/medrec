package com.oracle.medrec.util;

import java.net.URI;
import java.awt.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.naming.InitialContext;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

/**
 * <p>Example demonstrates the use of ServletContext listeners.</p>
 *
 * @author Copyright (c) 2003,2017, Oracle and/or its affiliates. All rights reserved.
 */
public class OpenBrowserServletContextListener implements ServletContextListener {

  private final static String RUNNING_STATE = "RUNNING";
  private final static String STATE_ATTRIBUTE = "State";
  private String httpURL = "http://localhost:7001";
  private String contextPath = "/medrec";
  private String page = "";

  public void contextInitialized(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    // determine servlet context   
    String tempPath = context.getInitParameter("url");
    contextPath = (tempPath != null && !tempPath.equals("") ? tempPath : contextPath);

    // get page to popup
    String tempPage = context.getInitParameter("page");
    page = (tempPage != null && !tempPage.equals("") ? tempPage : page);

    regesterServerStartUpListener();

    log(getInfoString(contextPath, page));
  }

  public void contextDestroyed(ServletContextEvent sce) {
  }

  private String getInfoString(String contextPath, String url) {
    StringBuffer str = new StringBuffer();
    str.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    str.append("\nAfter the server has booted, your browser should");
    str.append("\nautomatically launch and point to the ");
    str.append("\nAvitek Medical Records Sample Application Introduction Page ");
    str.append("\nrunning on this server. If your browser fails to launch, ");
    str.append("\npoint your browser to the following URL:");
    str.append("\n\"" + this.httpURL + contextPath + "/" + url + "\"");
    str.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    return str.toString();
  }

  private void regesterServerStartUpListener() {
    MBeanServer mbeanServer;
    ObjectName serverRuntime;
    try {
      mbeanServer = (MBeanServer) new InitialContext().lookup("java:comp/env/jmx/runtime");

      // Create Objectname for the runtime service
      ObjectName runtimeService = new ObjectName("com.bea:Name=RuntimeService," +
          "Type=" + "weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");

      // Get the ObjectName for the ServerRuntimeMBean
      serverRuntime = (ObjectName) mbeanServer.getAttribute(runtimeService, "ServerRuntime");
    } catch (Exception e) {
      log("Failed to get ServerRuntime Mbean for openning browser: " + e.getMessage());
      return;
    }

    try {
      // Get Server listen URL for http protocol
      httpURL = (String) mbeanServer.invoke(serverRuntime, "getURL", new Object[]{"http"},
          new String[]{"java.lang.String"});
    } catch (Exception e) {
      log("Failed to get listen URL for http protocol, so use the default one " + httpURL + ". The exception is: " +
          e.getMessage());
    }

    try {
      String serverState = (String) mbeanServer.getAttribute(serverRuntime, "State");
      if (RUNNING_STATE.equalsIgnoreCase(serverState)) {
        // Server is already running
        browseTo(httpURL + contextPath + "/" + page);
      } else {
        // Server is starting up
        AttributeChangeNotificationFilter filter = new AttributeChangeNotificationFilter();
        filter.enableAttribute(STATE_ATTRIBUTE);
        // Register attribute-change-listener to "State" attribute of ServerRuntime Mbean
        mbeanServer.addNotificationListener(serverRuntime, new NotificationListener() {
              @Override
              public void handleNotification(Notification notification, Object handback) {
                if (notification instanceof AttributeChangeNotification) {
                  AttributeChangeNotification attributeChange = (AttributeChangeNotification) notification;
                  if (RUNNING_STATE.equalsIgnoreCase((String) attributeChange.getNewValue())) {
                    browseTo(httpURL + contextPath + "/" + page);
                  }
                }
              }
            }, filter, null);
      }
    } catch (InstanceNotFoundException e) {
      log("Failed to register listener to ServerRuntime Mbean for openning browser: " + e.getMessage());
    } catch (Exception e) {
      log("Failed to browser to " + httpURL + contextPath + "/" + page + ". Error message is:" + e.getMessage());
    }

  }

  private void browseTo(String url) {
    try {
      Desktop.getDesktop().browse(new URI(url));
    } catch (Exception e) {
      System.err.println("Could not invoke browser: " + e.getMessage());
    }
  }

  private void log(String str) {
    System.out.println(str);
  }
}
