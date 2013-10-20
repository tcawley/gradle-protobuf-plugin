/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Tom Cawley IV
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
