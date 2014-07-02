package com.licensius.sandbox.ordl.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jsonldjava.jena.JenaJSONLD;
import com.hp.hpl.jena.rdf.model.Model;
import com.licensius.sandbox.ordl.data.DataStore;
import com.licensius.sandbox.ordl.data.Person;
import com.licensius.sandbox.ordl.rdf.RDFUtils;

@Path("/{path:.+}")
public class RequestDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);

	static {
		JenaJSONLD.init();
	}

	@Context
	private ServletContext servletContext;

	@GET
	@Produces({ "text/turtle", "application/rdf+xml", "application/ld+json" })
	public Response get(@Context UriInfo uriInfo,
			@javax.ws.rs.HeaderParam(HttpHeaders.ACCEPT) MediaType type) {

		// Retrieve the absolute URI
		String uri = uriInfo.getRequestUriBuilder().build().toString();
		logger.info("Request URI: " + uri);
		logger.info("Operation  : GET ");

		// Extract the id
		// Simple logic for the moment
		// A better way would be to remove the prefix to avoid cases like
		// http://example.org/nandana, http://example.org/a/nandana, http://example.org/a/b/nandana
		String id = uri.substring(uri.lastIndexOf('/') + 1, uri.length());

		// Retrieve data
		Person person = DataStore.getMember(id);

		if (person == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Model rdfModel = RDFUtils.convertToRDF(uri, person);

		return buildResponse(Response.ok(), rdfModel, type).entity(rdfModel).build();
	}

	@PUT
	@Consumes({"text/turtle", "application/rdf+xml", "application/ld+json" })
	public Response update(@Context UriInfo uriInfo, Model model) throws IOException {

		String absoluteURI = uriInfo.getRequestUriBuilder().build().toString();
		logger.info("Request URI: " + absoluteURI);
		logger.info("Operation  : PUT ");
		
		return Response.noContent().build();
	}

	@POST
	@Consumes("text/turtle")
	public Response create(@Context UriInfo uriInfo, InputStream requestBody,
			@HeaderParam("Slug") String id) throws URISyntaxException {

		String absoluteURI = uriInfo.getRequestUriBuilder().build().toString();
		logger.info("Request URI: " + absoluteURI);
		logger.info("Operation  : POST ");

		return Response.created(new URI(absoluteURI + id)).build();

	}

	@DELETE
	@Consumes("*/*")
	public Response delete(@Context UriInfo uriInfo) {

		String absoluteURI = uriInfo.getRequestUriBuilder().build().toString();
		logger.info("Request URI: " + absoluteURI);
		logger.info("Operation  : DELETE ");

		return Response.status(Status.FORBIDDEN).build();

	}

	@OPTIONS
	@Consumes("*/*")
	public Response options(@Context UriInfo uriInfo) {

		String uri = uriInfo.getRequestUriBuilder().build().toString();
		logger.info("Request URI: " + uri);
		logger.info("Operation  : OPTIONS ");

		// Extract the id
		String id = uri.substring(uri.lastIndexOf('/') + 1, uri.length());

		// Retrieve data
		boolean isMember = DataStore.isMember(id);

		if (!isMember) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Response.ResponseBuilder builder = Response.noContent();
		builder.header("Allow", "GET, OPTIONS, HEAD");
		builder.link("http://www.w3.org/ns/ldp#Resource", "type");

		return builder.build();

	}

	/**
	 * Not used at the moment, serialization is done by the ModelWriter class
	 */
	private Response.ResponseBuilder buildResponse(Response.ResponseBuilder responseBuilder,
			final Model model, final MediaType type) {

		StreamingOutput entity = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws WebApplicationException {

				String mediaType = type.getType() + "/" + type.getSubtype();
				
				if (type.isWildcardType() | ("text".equals(type.getType()) & type.isWildcardSubtype())) {
					model.write(output, "TURTLE");
					
				} else if ("text/turtle".equals(mediaType)) {
					model.write(output, "TURTLE");
				} else if ("application/rdf+xml".equals(mediaType)) {
					model.write(output, "RDF/XML");
				} else if ("application/ld+json".equals(mediaType)) {
					model.write(output, "JSON-LD");
				} else {
					throw new IllegalStateException("Media-type is not recognized");
				}
			}
		};

		responseBuilder.entity(entity);

		return responseBuilder;

	}

}
