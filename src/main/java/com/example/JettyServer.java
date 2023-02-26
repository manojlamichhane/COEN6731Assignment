package com.example;


import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;

public class JettyServer {

    public static void main(String[] args) throws Exception
    {
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;
        int port = 9090;

        QueuedThreadPool threadPool = 
        		new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});
        
        
        /*
         * context setting the configuration properties
         */

        String contextPath = "/collection";
        WebAppContext context = new WebAppContext();
        context.setContextPath(contextPath);
        context.setResourceBase(".");
        server.setHandler(context);
 
        
        /*
         * different handlers can be added. for example the authentication. 
         */
        
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath(contextPath);
        handler.addServlet(com.example.controller.ResourceServlet.class, "/audio");
        handler.addServlet(com.example.controller.ResourceAllServlet.class, "/allaudio");
        handler.addServlet(com.example.controller.ResourcePostServlet.class, "/saveaudio");
        server.setHandler(handler);
        
        server.start();
        server.join();
        
    }
}