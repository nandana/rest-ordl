package com.licensius.sandbox.ordl.data;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Sets;

public class DataStore {

	private static Map<String, Person> members = new HashMap<String, Person>();

	static {

		members.put(
				"victor",
				new Person("Víctor Rodríguez Doncel", 35, Sets.newHashSet("vrodriguez@fi.upm.es",
						"vroddon@gmail.com"), "http://licensius.com", Sets.newHashSet(
						"http://licensius.com", "http://superhub-project.eu/")));
		members.put(
				"nandana",
				new Person("Nandana Mihindukulasooriya", 31, Sets.newHashSet("nmihindu@fi.upm.es",
						"nandana.cse@gmail.com"), "http://nandana.org", Sets.newHashSet(
						"http://licensius.com", "http://ldp4j.org/")));

	}
	
	
	public static Person getMember(String id) {
		
		return members.get(id);
		
	}
	
	public static boolean isMember(String id){
		
		return members.containsKey(id);
	}

}
