package com.licensius.sandbox.ordl.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.collect.Sets;
import com.hp.hpl.jena.rdf.model.Model;
import com.licensius.sandbox.ordl.rdf.RDFUtils;

@Provider
@Produces({ "text/turtle", "application/rdf+xml", "application/ld+json" })
public class ModelWriter implements MessageBodyWriter<Model> {

	private static final Set<String> SUPPORTED_MEDIA_TYPES = Sets.newHashSet("text/turtle",
			"application/rdf+xml", "application/ld+json");

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		
		return SUPPORTED_MEDIA_TYPES.contains(mediaType.toString())
				&& Model.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(Model t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		// As of JAX-RS 2.0, the method has been deprecated and the value
		// returned by the method is ignored by a JAX-RS runtime. All
		// MessageBodyWriter implementations are advised to return -1 from the
		// method. Responsibility to compute the actual Content-Length header
		// value has been delegated to JAX-RS runtime.
		return -1;
	}

	@Override
	public void writeTo(Model model, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		
		String mediaTypeString = mediaType.getType() + "/" + mediaType.getSubtype();
		
		RDFUtils.writeTo(model, null, mediaType, entityStream);


	}

}
