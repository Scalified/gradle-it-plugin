# Gradle Integration Test Plugin

[![Build Status](https://travis-ci.org/Scalified/gradle-it-plugin.svg)](https://travis-ci.org/Scalified/gradle-it-plugin)

## Description

[Gradle Integration Test Plugin](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.it) provides an easy set up for integration tests

## Requirements

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Gradle](https://gradle.org/)

## Changelog

[Changelog](CHANGELOG.md)

## Applying

Build script snippet for use in all Gradle versions:

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.scalified.plugins.gradle:it:0.1"
  }
}

apply plugin: "com.scalified.plugins.gradle.it"
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

```
plugins {
  id "com.scalified.plugins.gradle.it" version "0.1"
}
```

## Usage

After applying the plugin, the following takes place:

1. New directories are created (if missing):
  * **src/it/java** - source set for integration tests (additionally marked as test source root in IntelliJ IDEA)
  * **src/it/resources** - resources for integration tests
2. Gradle configurations are added:
  * **itCompile** - configuration for integration test compile scope (depends on **testCompile**)
  * **itRuntime** - configuration for integration test runtime scope (depends on **testRuntime**)
3. Gradle **it** task created, which runs integration tests located in integration test source set

## Configuration

Currently the following configuration supported

```
it {
    srcDir = 'src/it/java' // custom integration test source set directory
    resourcesDir = "src/it/resources" // custom integration test resources directory

    options {
        maxParallelForks = 4 // maximum number of forks for integration tests execution
        maxHeapSize = '256m' // maximum size of a heap for integration tests execution
    }
}
```

If you need more configuration options, you may <a href="mailto:info@scalified.com?subject=[Gradle Integration Test Plugin]: Proposals And Suggestions">send a request</a> with description

## License

```
MIT License

Copyright (c) 2018 Scalified

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Scalified Links

* [Scalified](http://www.scalified.com)
* [Scalified Official Facebook Page](https://www.facebook.com/scalified)
* <a href="mailto:info@scalified.com?subject=[Gradle Integration Test Plugin]: Proposals And Suggestions">Scalified Support</a>