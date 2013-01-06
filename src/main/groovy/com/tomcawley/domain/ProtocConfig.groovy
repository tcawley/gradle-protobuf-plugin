package com.tomcawley.domain

import org.gradle.api.Named;

class ProtocConfig implements Named {

	final String name
	String path

	public ProtocConfig(String name) {
		this.name = name
	}
}
