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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin

import com.tomcawley.domain.LangConfig;
import com.tomcawley.domain.ProtoBufSourceSet;
import com.tomcawley.domain.ProtocConfig;
import com.tomcawley.tasks.ProtoBufCleanTask;
import com.tomcawley.tasks.ProtoBufCompileTask;

class ProtoBufPlugin implements Plugin<Project> {
	
	public static final String COMPILE_PROTO_TASK = 'compileProto'
	public static final String CLEAN_PROTO_TASK = 'cleanProto'

	@Override
	public void apply(Project project) {

		// apply plugin: 'java'
		project.plugins.apply(JavaPlugin)
		
		// apply plugin: 'cpp'
//		project.plugins.apply(CppPlugin)
		
		project.task(COMPILE_PROTO_TASK, type: ProtoBufCompileTask) {
			group = 'ProtoBuf'
			description = 'Compile .proto files into the language(s) configured.'
		}

		project.task(CLEAN_PROTO_TASK, type: ProtoBufCleanTask) {
			group = 'ProtoBuf'
			description = 'Delete the output directories for each compiled language.'
		}
		
		// compileJava.dependsOn compileProto
		project.getTasksByName(JavaPlugin.COMPILE_JAVA_TASK_NAME, true).each {
			it.dependsOn(COMPILE_PROTO_TASK)
		}

		// clean.dependsOn cleanProto
		project.tasks.findByName(BasePlugin.CLEAN_TASK_NAME).dependsOn(CLEAN_PROTO_TASK)
	
		def sourceSets = project.container(ProtoBufSourceSet)
		def protoc = project.container(ProtocConfig)
		def lang = project.container(LangConfig)
		
		project.configure(project) {
			extensions.create(ProtoBufExtension.NAME, ProtoBufExtension, sourceSets, protoc, lang)
		}
		
	}

	
}
