package com.licensius.sandbox.ordl.jaxrs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.licensius.sandbox.ordl.rdf.RDFUtils;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class ODRLFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String uri = requestContext.getUriInfo().getRequestUriBuilder().build().toString();
		String method = requestContext.getMethod();

		System.out
				.println("user(request): " + requestContext.getProperty(BasicAuthFilter.USERNAME));
		System.out.println("url(request): " + uri);
		System.out.println("method(request): " + method);

		// READ operations
		if (method.equals(HttpMethod.GET)
				|| method.equals(HttpMethod.HEAD) || method.equals(HttpMethod.OPTIONS)) {
			return;
		}

		// We need a better solution than this
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(requestContext.getEntityStream(), baos);
		Model model = RDFUtils.readFrom(requestContext.getMediaType(), uri,
				new ByteArrayInputStream(baos.toByteArray()));

		// TODO to the ORDL logic

		requestContext.setEntityStream(new ByteArrayInputStream(baos.toByteArray()));

	}

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {

		String uri = requestContext.getUriInfo().getRequestUriBuilder().build().toString();

		System.out.println("user(response): "
				+ requestContext.getProperty(BasicAuthFilter.USERNAME));
		
		if (responseContext.getStatus() ==  Response.Status.OK.getStatusCode()) {
			
			Model model = (Model) responseContext.getEntity();
			System.out.println(model);
		}



	}

}
