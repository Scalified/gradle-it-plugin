/*
 * MIT License
 *
 * Copyright (c) 2019 Scalified
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.scalified.plugins.gradle.it

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.slf4j.LoggerFactory

/**
 * @author shell
 * @since 2019-10-08
 */
open class ItPlugin : Plugin<Project> {

    private val logger = LoggerFactory.getLogger(ItPlugin::class.java)

    override fun apply(project: Project) {

        listOf(JavaPlugin::class, IdeaPlugin::class).filterNot(project.plugins::hasPlugin).forEach { type ->
            project.plugins.apply(type)
            logger.info("Applied '${type.simpleName}' plugin")
        }

        project.tasks.register<ItTask>(IT)
        project.extensions.sourceSets.register(IT)

        project.afterEvaluate {
            tasks.named<ItTask>(IT) {
                createMissingDirectories(setOf(srcDir.get(), resourcesDir.get()))
                outputs.upToDateWhen { false }

                project.extensions.sourceSets.named(IT) {
                    val mainSourceSet = project.extensions.sourceSet(SourceSet.MAIN_SOURCE_SET_NAME).get()
                    val testSourceSet = project.extensions.sourceSet(SourceSet.TEST_SOURCE_SET_NAME).get()

                    compileClasspath = compileClasspath.plus(mainSourceSet.output)
                        .plus(testSourceSet.compileClasspath)
                        .plus(testSourceSet.output)
                    runtimeClasspath = runtimeClasspath.plus(mainSourceSet.output)
                        .plus(testSourceSet.runtimeClasspath)
                        .plus(testSourceSet.output)
                    logger.debug("Configured '$IT' classpath")

                    project.configurations.named(implementationConfigurationName) {
                        extendsFrom(project.configurations.named(testSourceSet.implementationConfigurationName).get())
                    }
                    project.configurations.named(runtimeOnlyConfigurationName) {
                        extendsFrom(project.configurations.named(testSourceSet.runtimeOnlyConfigurationName).get())
                    }
                    logger.debug("Configured '$IT' configurations")

                    java.setSrcDirs(files(srcDir.get()))
                    resources.setSrcDirs(files(resourcesDir.get()))
                    classpath = runtimeClasspath
                    testClassesDirs = output.classesDirs
                    maxHeapSize = MAX_HEAP_SIZE
                    maxParallelForks = MAX_PARALLEL_FORKS

                    logger.debug("Configured '$IT' source sets")
                }

                dependsOn(tasks.itClasses)

                project.plugins.idea.model.module.testSources.from(srcDir)
                logger.debug("Marked '${srcDir.get()}' as an IDEA test sources directory")

                logger.debug("Task '$IT' configured")
            }
        }
    }

}
