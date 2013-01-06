package com.tomcawley.tasks

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import com.tomcawley.ProtoBufExtension;
import com.tomcawley.domain.LangConfig;
import com.tomcawley.domain.ProtoBufSourceSet;
import com.tomcawley.domain.ProtocConfig;

class ProtoBufCompileTask extends DefaultTask {
	
	@TaskAction
	def compileProto() {
		checkConfigureDefaultSourceSet()
		
		def srcDir = getSrcDir()
		
		if (!srcDir.exists()) {
			throw new GradleException("Directory '$srcDir' not found.")
		}

		def fileCount = 0
		
		// Make the directories
		project.protoBuf.lang.each { LangConfig lang ->
			project.file(lang.genDir).mkdirs()
		}
		
		// Generate protos for each lang (java, cpp, python, etc.)
		srcDir.eachFile { File protoFile ->
			fileCount++
			
			def os = System.getProperty('os.name')

			println "Compiling $protoFile.name for $os"

			def protocConfig = project.protoBuf.protoc.findByName(os)
			
			checkProtocConfig(protocConfig, os)
			
			project.protoBuf.lang.each { LangConfig lang ->
				executeProtoc(protocConfig.path, srcDir.absolutePath, lang, protoFile.absolutePath)
			}
		}
		
		if (fileCount == 0) {
			println "No proto files found!"
		}
	}

	def executeProtoc(String protocPath, srcDirPath, LangConfig lang, String protoFilePath) {
		def command = "$protocPath"
		command += " -I=${srcDirPath}"
		command += " --${lang.name}_out=${lang.genDir}"
		command += " ${protoFilePath}"
		println command

		def p = command.execute(null, project.projectDir)

		p.waitFor()

		if (p.exitValue() != 0) {
			throw new GradleException(p.err.text)
		}
	}

	def checkProtocConfig(ProtocConfig protocConfig, String os) {
		if (protocConfig == null) {
			def example = """protoBuf { 
   protoc { 
      '$os' {
         path = '/path/to/protoc.sh'
      }
   }
}"""
			throw new GradleException("No protoc path defined for platform '$os'.  Missing configuration?\n$example")
		}
	}
	
	/**
	 * Check if the user specified configuration, if not, supply the default
	 * convention.
	 * <p>
	 * Default/Convention:
	 * <pre>
	 * <code>
	 * protoBuf {
	 *     ...
     *     sourceSets {
     *         proto {
     *             srcDir = 'src/main/proto'
     *         }
     *     }
     *     ...
     * }
	 * </code> 
	 *</pre>
	 * @return N/A
	 */
	def checkConfigureDefaultSourceSet() {
		// Convention defaults if not configured in user's build.gradle
		if (!project.protoBuf.sourceSets.findByName(ProtoBufExtension.PROTO)) {

			project.protoBuf.sourceSets {
				// Create default sourceSet 'proto', which will default
				// srcDir to 'src/main/proto'
				proto
			}
		}
	}

	def getSrcDir() {
		// For now, there is only one sourceSet in the Collection
		def sourceSet;
		project.protoBuf.sourceSets.each { ProtoBufSourceSet ii -> sourceSet = ii }
		project.file(sourceSet.srcDir)
	}
}
