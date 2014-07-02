package com.licensius.sandbox.ordl.data;

import java.util.HashSet;
import java.util.Set;

public class Person {
	
	private String name;
	
	private int age;
	
	private Set<String> emails;
	
	private String homepage;
	
	private Set<String> currentProjects;
	
	
	public Person(String name, int age, Set<String> emails, String homepage,
			Set<String> currentProjects) {
		this.name = name;
		this.age = age;
		if (emails != null ) {
			this.emails = emails;
		} else {
			this.emails = new HashSet<String>();
		}
		this.homepage = homepage;
		if (currentProjects != null ) {
			this.currentProjects = currentProjects;
		} else {
			this.currentProjects = new HashSet<String>();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String[] getEmails() {
		return emails.toArray(new String[emails.size()]);
	}

	public void addEmail(String email) {
		emails.add(email);
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String[] getCurrentProjects() {
		return currentProjects.toArray(new String[currentProjects.size()]);
	}

	public void addCurrentProject(String currentProject) {
		currentProjects.add(currentProject);
	}

}
