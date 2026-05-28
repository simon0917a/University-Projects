package com.example.rest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) {
        // Create a new Jetty server on port 8080
        Server server = new Server(8080);

        // --- Handler for REST API ---
        ServletContextHandler apiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        apiContext.setContextPath("/api"); // Context path for the API
        ServletHolder servletHolder = apiContext.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.packages",
                "com.example.rest"
        );

        // --- Handler for Static Files (HTML, JS, CSS) ---
        ResourceHandler resourceHandler = new ResourceHandler();
        // Point to the 'public' folder in src/main/resources
        resourceHandler.setResourceBase(Main.class.getClassLoader().getResource("public").toExternalForm());
        resourceHandler.setWelcomeFiles(new String[]{"TestDome.html"}); // Optional: default file to serve
        resourceHandler.setDirectoriesListed(false);

        ContextHandler staticContext = new ContextHandler("/");
        staticContext.setHandler(resourceHandler);


        // --- Combine Handlers ---
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{staticContext, apiContext});
        server.setHandler(handlers);

        try {
            System.out.println(">>> Starting Jetty server on port 8080");
            server.start();
            System.out.println(">>> Server started. Access at http://localhost:8080/TestDome.html");
            System.out.println(">>> Press Ctrl+C to stop.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            server.destroy();
        }
    }
}
