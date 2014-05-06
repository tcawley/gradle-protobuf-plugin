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
package com.tomcawley.tasks

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.TaskAction;

import com.tomcawley.ProtoBufExtension;
import com.tomcawley.domain.LangConfig;
import com.tomcawley.domain.ProtoBufSourceSet;
import com.tomcawley.domain.ProtocConfig;

class ProtoBufCompileTask extends DefaultTask {
	
	@TaskAction
	def compileProto() {

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
		getProtoFiles().each { File protoFile ->
			fileCount++
			
			def os = System.getProperty('os.name')

			println "Compiling $protoFile.name for $os"

			def protocConfig = project.protoBuf.protoc.find { os.matches(it.name) }
			
			checkProtocConfig(protocConfig, os)
			
			project.protoBuf.lang.each { LangConfig lang ->
				executeProtoc(protocConfig.path, srcDir.absolutePath, lang, protoFile.absolutePath, os)
			}
		}
		
		if (fileCount == 0) {
			println "No proto files found!"
		}
	}

	@OutputDirectories
	def getLangDirs() {
		project.protoBuf.lang.collect { LangConfig lang ->
			project.file(lang.genDir).mkdirs()
			project.file(lang.genDir)
		}
	}

	@InputFiles
	List<File> getProtoFiles() {
        checkConfigureDefaultSourceSet()
		def retval = []
		getSrcDir().eachFileMatch(~/.*\.proto/) {retval += it}
		return retval
	}

	def executeProtoc(String protocPath, srcDirPath, LangConfig lang, String protoFilePath, os) {

		println lang.plugin
		def matched = lang.plugin.findAll { it.name.matches(os) }
		def plugins = matched.collect { "--plugin="+it.path }

		def command = ["$protocPath",
		"-I=${srcDirPath}"] +
		plugins +
		["--${lang.name}_out=${lang.genDir}",
                "${protoFilePath}"]

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
