# Gradle Integration Test Plugin

[![Build Status](https://github.com/Scalified/gradle-proguard-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/Scalified/gradle-proguard-plugin/actions)
[![Build Status](https://github.com/Scalified/gradle-it-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/Scalified/gradle-it-plugin/actions)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fscalified%2Fplugins%2Fgradle%2Fit%2Fcom.scalified.plugins.gradle.it.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.it)

## Description

[Gradle Integration Test Plugin](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.it) provides an easy set up for integration tests

## Requirements

* [Gradle 8+](https://gradle.org/)

## Changelog

[Changelog](CHANGELOG.md)

## Applying

Using the plugins DSL (Kotlin):

```kotlin
plugins {
    id("com.scalified.plugins.gradle.it") version "$VERSION"
}
```

## Usage

After applying the plugin, the following takes place:

1. JavaPlugin applied if not yet applied
2. IdeaPlugin applied if not yet applied
3. New directories are created (if missing):
   * Source set directory for integration tests (`src/it/java` by default)
   * Resources directory for integration tests (`src/it/resources` by default)
4. Gradle configurations added:
   * `itImplementation` - configuration for integration test compile scope (contains `implementation` classpath)
   * `itRuntimeOnly` - configuration for integration test runtime scope (contains `testRuntimeOnly` classpath)
   * `it*` - other configurations inherited from `Test` (e.g. `itAnnotationProcessor`)
5. Gradle `it` task created, which runs integration tests located within the integration test source set
6. Integration test sources directory marked as test sources in **IntelliJ IDEA**

## Configuration

```kotlin
tasks.it {
    srcDir = "src/it/kotlin"
    resourcesDir = "src/it/resources"
    useJUnitPlatform()
    maxParallelForks = 4

    finalizedBy(tasks.cleanDb) // assuming there is some cleanDb task
}
```

## License

```
MIT License

Copyright (c) 2019 Scalified

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
