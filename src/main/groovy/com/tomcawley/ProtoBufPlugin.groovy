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
		project.tasks.findByName(JavaPlugin.COMPILE_JAVA_TASK_NAME).dependsOn(COMPILE_PROTO_TASK)

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
