package com.tomcawley

import org.gradle.api.NamedDomainObjectContainer;

import com.tomcawley.domain.LangConfig
import com.tomcawley.domain.ProtoBufSourceSet
import com.tomcawley.domain.ProtocConfig

class ProtoBufExtension {

	static final PROTO = 'proto' 
	static final NAME = 'protoBuf'
			
	final NamedDomainObjectContainer<ProtoBufSourceSet> sourceSets
	final NamedDomainObjectContainer<ProtocConfig> protoc
	final NamedDomainObjectContainer<LangConfig> lang
	
	ProtoBufExtension(NamedDomainObjectContainer<ProtoBufSourceSet> sourceSets,
		NamedDomainObjectContainer<ProtocConfig> protocConfig,
		NamedDomainObjectContainer<LangConfig> lang) {
		this.sourceSets = sourceSets
		this.protoc = protocConfig
		this.lang = lang
	}
	
	def sourceSets(Closure closure) {
		sourceSets.configure(closure)
	}
	
	def protoc(Closure closure) {
		protoc.configure(closure)
	}

	def lang(Closure closure) {
		lang.configure(closure)
	}
}
