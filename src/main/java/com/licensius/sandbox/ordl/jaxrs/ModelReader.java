package com.licensius.sandbox.ordl.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.google.common.collect.Sets;
import com.hp.hpl.jena.rdf.model.Model;
import com.licensius.sandbox.ordl.rdf.RDFUtils;

/**
 * JAX-RS provider that parses RDF and build a Jena model.
 */
@Provider
@Consumes({"text/turtle", "application/rdf+xml", "application/ld+json" })
public class ModelReader implements MessageBodyReader<Model> {

	
	@Context
	UriInfo uriInfo;

	public static final Set<String> SUPPORTED_MEDIA_TYPES = Sets.newHashSet("text/turtle",
			"application/rdf+xml", "application/ld+json");

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		
		return SUPPORTED_MEDIA_TYPES.contains(mediaType.toString())
				&& Model.class.isAssignableFrom(type);
	}

	@Override
	public Model readFrom(Class<Model> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		
		// Retrieve the absolute URI
		String uri = uriInfo.getRequestUriBuilder().build().toString();
		
		// Build a Jena model from the input stream
		return RDFUtils.readFrom(mediaType, uri, entityStream);
	}

}
