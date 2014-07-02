package com.licensius.sandbox.ordl.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/oeg")
public class RESTApplication extends Application {
	
	public static final String MODEL = "model";

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(RequestDispatcher.class);
		classes.add(ODRLFilter.class);
		classes.add(BasicAuthFilter.class);
		classes.add(ModelWriter.class);
		classes.add(ModelReader.class);
		return classes;
	}

}
