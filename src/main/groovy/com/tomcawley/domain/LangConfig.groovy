package com.tomcawley.domain

import org.gradle.api.Named;

class LangConfig implements Named {

	final String name
	String genDir

	public LangConfig(String name) {
		this.name = name
		
		// Default, e.g., src/main/java, src/main/cpp, etc.
		genDir = "src/main/$name"
	}
}
