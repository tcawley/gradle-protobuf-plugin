gradle-protobuf-plugin
======================

Gradle plugin for using Google Protocol Buffers in your Gradle project.

Quick Start
-----
**build.gradle**

    // Apply the plugin
    buildscript {
        repositories {
            flatDir (dirs: file('../protobufGradlePluginGroovy/build/libs'))
        }
        
        dependencies {
            classpath ':protobufGradlePlugin:0.1'
        }
    }
    
    apply plugin: 'protobufGradlePlugin'
    
    // Point to your protobuf library for compilation of the generated files
    dependencies {
        compile files('/Users/user/Downloads/protobuf-2.4.1/java/target/protobuf-java-2.4.1.jar')    
    }
    
    // And finally the important stuff
    protoBuf {
        protoc {
            'Mac OS X' {
                path = "/Users/user/Downloads/protobuf-2.4.1/src/protoc"
            }
        }
        
        // Optional, defaults to 'src/main/proto'
        sourceSets {
            proto {
                srcDir = 'src/main/proto'
            }
        }
    
        lang {
            java {
                genDir = 'src/main/java'
            }
            cpp {
                genDir = 'src/main/c++'
            }
        }
    }
    

Features
-----
Programming languages supported:

1. Java

        lang {
            java
        }
    
2. C++

        lang {
            cpp
        }

3. Python

        lang {
            python
        }

Tasks
-----

* **<tt>compileProto</tt>** - Compiles the .proto files found in <tt>protoBuf.sourceSets.proto.srcDir</tt> for each programming language specified.
* **<tt>cleanProto</tt>** - Deletes the generated files for each programming language compiled.

Conventions
-----
* **<tt>protoc</tt>** - Platform dependent.  Specify the name of your platform as determined by
 Java's <tt>System.getProperty("os.name");</tt>
 
 **<tt>path</tt>** specifies the path to the <tt>protoc</tt> executable for your platform.
    
    Examples:
    
        protoc {
            'Mac OS X' {
                path = '/path/to/protoc'
            }
            'Windows 7' {
                path = 'C:\\Path\\To\\protoc.exe'
            }
            `Windows XP' {
                path = 'C:\\Path\\To\\protoc.exe'
            }
        }
        
* **<tt>sourceSets</tt>** - Optional; if unspecified configures **<tt>proto.srcDir=src/main/proto</tt>**
* **<tt>lang</tt>** - configures the programming language(s) you want your .proto files compiled to.
 The default **<tt>genDir</tt>** is **<tt>src/main/$name</tt>**, where <tt>$name</tt> is the name of the programming language
 (e.g., java, cpp, python).
 
        lang {
            java   // Defaults genDir to 'src/main/java'
            cpp    // Defaults genDir to 'src/main/cpp'
            python // Defaults genDir to 'src/main/python'
        }
