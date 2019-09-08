[![](https://jitci.com/gh/AtomIsHere/atomutils/svg)](https://jitci.com/gh/AtomIsHere/atomutils) [![Build Status](https://travis-ci.org/AtomIsHere/atomutils.svg?branch=master)](https://travis-ci.org/AtomIsHere/atomutils) [![Nightly Build](https://ci.appveyor.com/api/projects/status/lix3r1pv1fo2ypyt/branch/master?svg=true)](https://ci.appveyor.com/project/AtomIsHere/atomutils/branch/master)


# Atom Utils
   <p>Atom Utils is a collection of useful tools to help making plugins. It include tools for helping make services and
   commands, it also includes bits of code I've used when making plugins.</p>

## Maven
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    
    <dependency>
        <groupId>com.github.AtomIsHere</groupId>
        <artifactId>atomutils</artifactId>
        <version>1.0.1</version>
    </dependency>
    
## Gradle
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    
    dependencies {
        compileOnly 'com.github.AtomIsHere:atomutils:1.0.1'
    }
