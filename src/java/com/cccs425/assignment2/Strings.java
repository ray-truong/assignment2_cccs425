/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.cccs425.assignment2;


import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


/**
 * REST Web Service
 *
 * @author raymond.truong
 */
@Path("string")
public class Strings {

    private static final List<String> stringCollection = new ArrayList<>();
    
    @GET
    @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getStringCollection(@HeaderParam("Accept") String acceptHeader) {
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_TYPE);
        supportedMediaTypes.add(MediaType.TEXT_XML_TYPE);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN_TYPE);

        MediaType bestMatch = MediaType.valueOf(acceptHeader);
        if (!supportedMediaTypes.contains(bestMatch)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unsupported format").build();
        }
        String format = bestMatch.toString();
        switch (format) {
            case MediaType.APPLICATION_JSON:
                return Response.ok(toJson(stringCollection), MediaType.APPLICATION_JSON_TYPE).build();
            case MediaType.TEXT_XML:
                return Response.ok(toXml(stringCollection), MediaType.TEXT_XML_TYPE).build();
            default:
                return Response.ok(toPlainText(stringCollection), MediaType.TEXT_PLAIN_TYPE).build();
        }
    }
    
    @GET
    @Path("/xml")
    @Produces(MediaType.TEXT_XML)
    public Response getXml() {
        return Response.ok(toXml(stringCollection), MediaType.TEXT_XML_TYPE).build();
    }
    
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        return Response.ok(toJson(stringCollection), MediaType.APPLICATION_JSON_TYPE).build();
    }
    
    @GET
    @Path("/text")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getText() {
        return Response.ok(toPlainText(stringCollection), MediaType.TEXT_PLAIN_TYPE).build();
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addString(String newString) {
        if (stringCollection.contains(newString)) {
            return Response.status(Status.BAD_REQUEST).entity("String already exists in the collection").build();
            }   

        stringCollection.add(newString);
        return Response.status(Status.CREATED).entity("String added").build();
}
   

    @DELETE
    @Path("/{stringToDelete}")
    public Response deleteString(@PathParam("stringToDelete") String stringToDelete) {
        if (!stringCollection.contains(stringToDelete)) {
            throw new BadRequestException("String not found in the collection");
        }

        stringCollection.remove(stringToDelete);
        return Response.status(Status.NO_CONTENT).build();
    }
    private String toJson(List<String> strings) {
        StringBuilder json = new StringBuilder("[");
        for (String str : strings) {
            json.append("\"").append(str).append("\",");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("]");
        return json.toString();
    }

    private String toXml(List<String> strings) {
        StringBuilder xml = new StringBuilder("<strings>");
        for (String str : strings) {
            xml.append("<string>").append(str).append("</string>");
        }
        xml.append("</strings>");
        return xml.toString();
    }

    private String toPlainText(List<String> strings) {
        return String.join("\n", strings);
    }
}