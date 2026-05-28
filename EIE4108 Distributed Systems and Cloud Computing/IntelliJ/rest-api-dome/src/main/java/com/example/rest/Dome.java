package com.example.rest;

import java.util.ArrayList;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// add api('com.google.code.gson:gson:2.9.1') to the dependencies section in build.gradle file
import com.google.gson.Gson;

//add implementation 'implementation 'javax.inject:javax.inject:1'' to the dependencies section in build.gradle file
import jakarta.inject.Singleton;


@Singleton
@Path("/dome")
/*
 * Endpoint: http://localhost:8080/<WAR folder name>/api/dome
 * dome: path to this class
 */
public class Dome {
	private ArrayList<CD> database;
	public Dome() {
		database = new ArrayList<CD>();
	}

	@POST
	@Path("{title},{artist}")
	public Response addCD(@PathParam("title")String title, @PathParam("artist")String artist) {
		database.add(new CD(title,artist));
		System.out.println("Title: " + title);
		System.out.println("Artist: " + artist);
		System.out.println("size: "+database.size());
		System.out.println("this obj: "+this.toString());
		return Response.status(200).build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getCD(@QueryParam("title")String title) {
		for (CD cd : database) {
			System.out.println("1. " + cd.getTitle());
			if (cd.getTitle().equals(title)) {
				System.out.println(cd.getTitle() + " " + cd.getArtist());
				return cd.getTitle() + " " + cd.getArtist();
			}
		}
		System.out.println("CD not exists");
		System.out.println("this obj: "+this.toString());
		return "";
	}
	@GET
	@Path("/xml")
	@Produces("application/xml")	
	public CD getXmlCD(@QueryParam("title")String title) {
		for (CD cd : database) {
			if (cd.getTitle().equals(title)) {
				System.out.println(cd.getTitle() + " " + cd.getArtist());
				return cd; 
			}
		}
		System.out.println("CD not exists");
		return new CD("","");
	}
	@GET
	@Path("/json")
	@Produces("application/json")	
	public String getJsonCD(@QueryParam("title")String title) {
		Gson gson = new Gson();
		for (CD cd : database) {
			if (cd.getTitle().equals(title)) {
				System.out.println(cd.getTitle() + " " + cd.getArtist());				
				return gson.toJson(cd);				 
			}
		}
		System.out.println("CD not exists");
		return gson.toJson(new CD("",""));
	}	

}



