package com.example.api;

import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;
public class Main {
    public static void main(String[] args) {
        URI baseUri = URI.create("http://localhost:8080/");
        ResourceConfig config = new ResourceConfig()
                .packages("com.example.api");
        JettyHttpContainerFactory.createServer(baseUri, config);
        System.out.println("Server running at " + baseUri);
        System.out.println("http://localhost:8080/api/hello");
    }
}