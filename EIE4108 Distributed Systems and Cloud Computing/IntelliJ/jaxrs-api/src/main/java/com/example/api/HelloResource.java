package com.example.api;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
@Path("/api")
public class HelloResource {
    @GET
    @Path("/hello")
    public Response hello(@QueryParam("name") String name) {
        if (name == null || name.isBlank()) {
            name = "world";
        }
        return Response.ok("Hello, " + name + "!").build();
    }
}
