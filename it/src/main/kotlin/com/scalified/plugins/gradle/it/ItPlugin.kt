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
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.slf4j.LoggerFactory

/**
 * @author shell
 * @since 2019-10-08
 */
internal const val IT_PLUGIN_NAME = "it"

internal const val IT_PLUGIN_GROUP = "verification"

internal const val IT_PLUGIN_DESCRIPTION = "Runs the integration tests"

internal const val SOURCE_SET_NAME = "it"

open class ItPlugin : Plugin<Project> {

	private val logger = LoggerFactory.getLogger(ItPlugin::class.java)

	override fun apply(project: Project) {

		if (!project.plugins.hasPlugin(JavaPlugin::class.java)) {
			project.plugins.apply(JavaPlugin::class.java)
			logger.debug("Applied Java Plugin")
		}

		project.tasks.create(IT_TASK_NAME, ItTask::class.java)
		logger.debug("Created $IT_TASK_NAME task")

		project.extensions.create(IT_PLUGIN_EXTENSION_NAME, ItPluginExtension::class.java, project)
		logger.debug("Created $IT_PLUGIN_EXTENSION_NAME extension")

		project.sourceSets.create(SOURCE_SET_NAME)
		logger.debug("Created $SOURCE_SET_NAME source set")

		project.afterEvaluate {
			val extension = it.extensions.getByType(ItPluginExtension::class.java)
			createDirectories(it, extension)
			configureSourceSet(it, extension)
			configureConfiguration(it)
			configureTask(it)
			markTestDirectories(it, extension)
		}
	}

	private fun createDirectories(project: Project, extension: ItPluginExtension) {
		fun create(path: String) {
			val dir = project.file(path)
			if (!dir.exists()) {
				dir.mkdirs()
				logger.debug("Created ${dir.absolutePath} directory")
			}
		}
		create(extension.srcDir)
		create(extension.resourcesDir)
	}

	private fun configureSourceSet(project: Project, extension: ItPluginExtension) {
		val sourceSet = project.sourceSet(SOURCE_SET_NAME)

		val mainSourceSet = project.sourceSet(SourceSet.MAIN_SOURCE_SET_NAME)
		val testSourceSet = project.sourceSet(SourceSet.TEST_SOURCE_SET_NAME)

		sourceSet.compileClasspath = sourceSet.compileClasspath.plus(mainSourceSet.output)
				.plus(testSourceSet.compileClasspath)
				.plus(testSourceSet.output)
		sourceSet.runtimeClasspath = sourceSet.runtimeClasspath.plus(mainSourceSet.output)
				.plus(testSourceSet.runtimeClasspath)
				.plus(testSourceSet.output)

		sourceSet.java.setSrcDirs(project.files(extension.srcDir))
		sourceSet.resources.setSrcDirs(project.files(extension.resourcesDir))
		logger.debug("Configured $SOURCE_SET_NAME source set")
	}

	private fun configureConfiguration(project: Project) {
		val sourceSet = project.sourceSet(SOURCE_SET_NAME)
		val testSourceSet = project.sourceSet(SourceSet.TEST_SOURCE_SET_NAME)

		project.configurations.getByName(sourceSet.implementationConfigurationName)
				.extendsFrom(project.configurations.getByName(testSourceSet.implementationConfigurationName))
		logger.debug("Configured ${sourceSet.implementationConfigurationName} configuration")

		project.configurations.getByName(sourceSet.runtimeOnlyConfigurationName)
				.extendsFrom(project.configurations.getByName(testSourceSet.runtimeOnlyConfigurationName))
		logger.debug("Configured ${sourceSet.runtimeOnlyConfigurationName} configuration")
	}

	private fun configureTask(project: Project) {
		val task = project.tasks.getByName(IT_TASK_NAME) as ItTask

		task.dependsOn(project.tasks.findByName("itClasses"))

		val sourceSet = project.sourceSet(SOURCE_SET_NAME)
		task.classpath = sourceSet.runtimeClasspath
		task.testClassesDirs = sourceSet.output.classesDirs
		logger.debug("Configured $IT_TASK_NAME task")
	}

	private fun markTestDirectories(project: Project, extension: ItPluginExtension) {
		if (extension.markAsTestSources) {
			if (!project.plugins.hasPlugin(IdeaPlugin::class.java)) {
				project.plugins.apply(IdeaPlugin::class.java)
				logger.debug("Applied Idea Plugin")
			}

			project.plugins.getPlugin(IdeaPlugin::class.java).model.module.apply {
				testSourceDirs = testSourceDirs.plus(project.file(extension.srcDir))
				logger.debug("Configured ${extension.srcDir} as test sources directory")
			}
		}
	}

}
