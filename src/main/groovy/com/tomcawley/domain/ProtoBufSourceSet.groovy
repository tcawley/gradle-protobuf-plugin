package com.tomcawley.domain

class ProtoBufSourceSet {
	
	final String name
	String srcDir
	
	public ProtoBufSourceSet(String name) {
		this.name = name
		
		// Default
		srcDir = "src/main/$name"
	}
	
}
