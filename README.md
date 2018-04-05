# Gradle Integration Test Plugin

[![Build Status](https://travis-ci.org/Scalified/gradle-it-plugin.svg)](https://travis-ci.org/Scalified/gradle-it-plugin)

## Description

[Gradle Integration Test Plugin](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.it) provides an easy set up for integration tests

## Requirements

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Gradle 4+](https://gradle.org/)

## Changelog

[Changelog](CHANGELOG.md)

## Applying

Build script snippet for plugins DSL for Gradle 2.1 and later:

```gradle
plugins {
  id "com.scalified.plugins.gradle.it" version "0.1.6"
}
```

Build script snippet for use in older Gradle versions or where dynamic configuration is required:

```gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.scalified.plugins.gradle:it:0.1.6"
  }
}

apply plugin: "com.scalified.plugins.gradle.it"
```

## Usage

After applying the plugin, the following takes place:

1. JavaPlugin applied if not yet applied
2. New directories are created (if missing):
   * Source set directory for integration tests (the value of **it.srcDir** property, which is **src/it/java** by default)
   * Resources directory for integration tests (the value of **it.resourcesDir** property, which is **src/it/resources** by default)
3. Source set directory for integration tests marked as test source directory (IntelliJ IDEA only)
4. Gradle configurations added:
   * **itCompile** - configuration for integration test compile scope (depends on **testCompile**)
   * **itRuntime** - configuration for integration test runtime scope (depends on **testRuntime**)
5. Gradle **it** task created, which runs integration tests located in integration test source set

## Configuration

```gradle
it {
  srcDir = 'src/it/java' // integration test source set directory
  resourcesDir = "src/it/resources" // integration test resources directory
  markAsTestSources = true // marks integration test source directory as test sources (IntelliJ IDEA only)

  options {
    maxParallelForks = 4
    maxHeapSize = '256m'
    useJUnitPlatform()

    // any other test options (all options inherited from test ones)
  }
}

tasks.it.finalizedBy(cleanDb) // assuming there is some cleanDb task
```

> **markAsTestSources** parameter allows to control whether integration test directory will be marked as test sources in IntelliJ IDEA. In some cases it may prevent incorrect module and dependency handling in IntelliJ IDEA

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
