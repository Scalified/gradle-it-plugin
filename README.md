# Gradle Integration Test Plugin

[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/Scalified/gradle-it-plugin/blob/master/LICENSE)
[![Build Status](https://github.com/Scalified/gradle-proguard-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/Scalified/gradle-proguard-plugin/actions)
[![Build Status](https://github.com/Scalified/gradle-it-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/Scalified/gradle-it-plugin/actions)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fscalified%2Fplugins%2Fgradle%2Fit%2Fcom.scalified.plugins.gradle.it.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.it)

## Description

[Gradle Integration Test Plugin](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.it) provides an easy set up for integration tests

## Requirements

* [Gradle 9+](https://gradle.org/)

## Usage

```kotlin
plugins {
    id("com.scalified.plugins.gradle.it") version "$VERSION"
}
```

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

### Configuration

```kotlin
tasks.it {
    srcDir = "src/it/kotlin"
    resourcesDir = "src/it/resources"
    useJUnitPlatform()
    maxParallelForks = 4

    finalizedBy(tasks.cleanDb) // assuming there is some cleanDb task
}
```

---

**Made with ❤️ by [Scalified](http://www.scalified.com)**
