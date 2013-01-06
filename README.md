gradle-protobuf-plugin
======================

[Gradle](http://gradle.org) plugin for using [Google Protocol Buffers](https://developers.google.com/protocol-buffers/) in your Gradle project.

Quick Start
-----
**build.gradle**

    // Apply the plugin
    buildscript {
        repositories {
            flatDir (dirs: file('/Users/user/git/gradle-protobuf-plugin/build/libs'))
        }
        
        dependencies {
            classpath ':gradle-protobuf-plugin:0.1'
        }
    }
    
    apply plugin: 'gradleProtobufPlugin'
    
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
    
    Compiles the <tt>.proto</tt> files into <tt>.java</tt> files (output to <tt>lang.java.genDir</tt>).
    
    Implies:
    
        apply plugin: 'java'
        compileJava.dependsOn compileProto
        clean.dependsOn cleanProto
    
2. C++

        lang {
            cpp
        }

    <tt>.proto</tt> generation only at this time.
    
3. Python

        lang {
            python
        }

    <tt>.proto</tt> generation only at this time.
    
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
  
  Supports only local files.  Remote folders coming soon (e.g., http://your/protos).
* **<tt>lang</tt>** - configures the programming language(s) you want your .proto files compiled to.
 The default **<tt>genDir</tt>** is **<tt>src/main/$name</tt>**, where <tt>$name</tt> is the name of the programming language
 (e.g., java, cpp, python).
 
        lang {
            java   // Defaults genDir to 'src/main/java'
            cpp    // Defaults genDir to 'src/main/cpp'
            python // Defaults genDir to 'src/main/python'
        }
