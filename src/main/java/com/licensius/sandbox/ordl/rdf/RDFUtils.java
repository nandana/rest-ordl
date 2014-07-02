package com.licensius.sandbox.ordl.rdf;

import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.licensius.sandbox.ordl.data.Person;

public class RDFUtils {

	public static Model convertToRDF(String uri, Person person) {

		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("foaf", FOAF.NS);

		Resource personRDF = model.createResource(uri);
		personRDF.addProperty(RDF.type, FOAF.Person);
		personRDF.addProperty(FOAF.name, person.getName());
		personRDF.addProperty(model.createProperty("http://xmlns.com/foaf/0.1/age"),
				String.valueOf(person.getAge()));
		personRDF.addProperty(FOAF.homepage, person.getHomepage());
		
		for (String email : person.getEmails()) {
			personRDF.addProperty(FOAF.mbox, email);
		}
		
		for (String currentProject : person.getCurrentProjects()){
			personRDF.addProperty(FOAF.currentProject, currentProject);
		}

		return model;

	}
	
	public static Model readFrom(MediaType mediaType, String base, InputStream entityStream){
		
		String mediaTypeString = mediaType.toString();
		
		Model model = ModelFactory.createDefaultModel();
		
        if ("text/turtle".equals(mediaTypeString)) {
			model.read(entityStream, base, "TURTLE");
		} else if ("application/rdf+xml".equals(mediaTypeString)) {
			model.read(entityStream, base, "RDF/XML");
		} else if ("application/ld+json".equals(mediaTypeString)) {
			model.read(entityStream, base,"JSON-LD");
		} else {
			throw new IllegalStateException("Media-type is not recognized");
		}
		
		return model;
		
	}
	
	
	public static void writeTo(Model model, String base, MediaType mediaType, OutputStream entityStream){
		
		String mediaTypeString = mediaType.toString();
		
		if (mediaType.isWildcardType() | ("text".equals(mediaType.getType()) & mediaType.isWildcardSubtype())) {
			model.write(entityStream, "TURTLE");
		} else if (mediaType.isWildcardType() | ("text".equals(mediaType.getType()) & mediaType.isWildcardSubtype())) {
			model.write(entityStream, "TURTLE");	
		} else if ("text/turtle".equals(mediaTypeString)) {
			model.write(entityStream, "TURTLE");
		} else if ("application/rdf+xml".equals(mediaTypeString)) {
			model.write(entityStream, "RDF/XML");
		} else if ("application/ld+json".equals(mediaTypeString)) {
			model.write(entityStream, "JSON-LD");
		} else {
			throw new IllegalStateException("Media-type is not recognized");
		}
		
	}

}
