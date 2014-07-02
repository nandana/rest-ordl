package com.licensius.sandbox.ordl.jaxrs;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.codec.binary.Base64;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {
	
	public static final String USERNAME = "username";

	private static String BASIC_AUTH_PREFIX = "Basic ";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		//No authorization header
		if(authorization == null){
	        String response = "User needs to be authenticated to access this resource.";
	        ResponseBuilder builder = Response.status(Response.Status.UNAUTHORIZED).entity(response);
	        builder.header(HttpHeaders.WWW_AUTHENTICATE, BASIC_AUTH_PREFIX + "realm=\"OEG\"");
	        throw new WebApplicationException(builder.build());
		}
		
		//Check if value is a basic auth value 
		if (!authorization.contains(BASIC_AUTH_PREFIX)) {
	        String response = "User needs to user HTTP Basic Authentication";
	        ResponseBuilder builder = Response.status(Response.Status.UNAUTHORIZED).entity(response);
	        builder.header(HttpHeaders.WWW_AUTHENTICATE, BASIC_AUTH_PREFIX + "realm=\"OEG\"");
	        throw new WebApplicationException(builder.build());
		}
		
		// Remove the basic auth prefix
	    String basicAuth = authorization.replaceFirst(BASIC_AUTH_PREFIX, "");
		
	    //Decode Base64
	    String usernamePassword = new String(Base64.decodeBase64(basicAuth));
		
		//Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernamePassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
		
		System.out.println("Username :" + username);
		System.out.println("Passowrd :" + password);
		
		
		requestContext.setProperty(USERNAME, username);
	
		
	}

}
