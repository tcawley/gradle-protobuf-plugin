/**
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

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

import com.tomcawley.tasks.ProtoBufCleanTask;
import com.tomcawley.tasks.ProtoBufCompileTask;

class ProtoBufPluginTest {
	@Test
	public void addCompileTaskToProject() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		assertTrue(project.tasks.compileProto instanceof ProtoBufCompileTask)
	}

	@Test
	public void addCleanTaskToProject() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		assertTrue(project.tasks.cleanProto instanceof ProtoBufCleanTask)
	}

	@Test
	public void addExtensionToProject() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		assertThat(project.protoBuf, notNullValue())
	}

	@Test
	public void checkSourceSetDefault() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		// Configure sourceSets.proto, check default srcDir
		project.protoBuf.sourceSets { proto }

		assertThat(project.protoBuf.sourceSets.proto.srcDir, is('src/main/proto'))
	}

	@Test
	public void checkLangJavaDefaultConfig() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		// Configure lang.java, check default genDir
		project.protoBuf.lang { java }

		assertThat(project.protoBuf.lang.java.genDir, is('src/main/java'))
	}

	@Test
	public void checkLangJavaConfig() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		// Configure lang.java, check genDir
		project.protoBuf.lang { java.genDir='build/generated' }

		assertThat(project.protoBuf.lang.java.genDir, is('build/generated'))
	}

	@Test
	public void checkProtocConfigNoPath() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		def os = System.getProperty('os.name')

		// Configure os-specific protoc executable, no path
		project.protoBuf.protoc { "$os" {  } }

		assertThat(project.protoBuf.protoc."$os".path, nullValue())
	}

	@Test
	public void checkProtocConfig() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'gradleProtoBufPlugin'

		def os = System.getProperty('os.name')

		// Configure os-specific protoc executable
		project.protoBuf.protoc { "$os" { path='/dev/poo' } }

		assertThat(project.protoBuf.protoc."$os".path, is('/dev/poo'))
	}
}