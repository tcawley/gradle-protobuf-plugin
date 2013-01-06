package com.tomcawley.tasks

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import com.tomcawley.domain.LangConfig;

class ProtoBufCleanTask extends DefaultTask {
	
	@TaskAction
	def cleanProto() {
		
		project.protoBuf.lang.each { LangConfig lang ->
			project.file(lang.genDir).deleteDir()
		}
	}
	
}
