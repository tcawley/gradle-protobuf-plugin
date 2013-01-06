package com.tomcawley

import org.gradle.api.NamedDomainObjectContainer;

import com.tcs.domain.LangConfig
import com.tcs.domain.ProtoBufSourceSet
import com.tcs.domain.ProtocConfig

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
