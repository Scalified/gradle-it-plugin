/*
 * MIT License
 *
 * Copyright (c) 2018 Scalified
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
 *
 */

package com.scalified.plugins.gradle.it

import groovy.transform.PackageScope
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
class ItPlugin implements Plugin<Project> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItPlugin)

	@PackageScope
	static final String SOURCE_SET_NAME = 'it'

	private Project project

	@Override
	void apply(Project project) {
		this.project = project

		if (!project.plugins.hasPlugin(JavaPlugin)) {
			LOGGER.debug("Applied Java plugin")
			project.plugins.apply(JavaPlugin)
		}

		project.tasks.create(ItTask.NAME, ItTask)
		LOGGER.debug("Created $ItTask.NAME task")

		project.extensions.create(ItPluginExtension.NAME, ItPluginExtension, project)
		LOGGER.debug("Created $ItPluginExtension.NAME extension")

		project.sourceSets.create(SOURCE_SET_NAME)
		LOGGER.debug("Created $SOURCE_SET_NAME source set")

		project.afterEvaluate {
			def extension = project.extensions.getByType(ItPluginExtension)
			createDirectories(extension)
			configureSourceSet(extension)
			configureConfiguration()
			configureTask()
		}
	}

	private def createDirectories(ItPluginExtension extension) {
		def create = { path ->
			def dir = project.file(path)
			if (!dir.exists()) {
				dir.mkdirs()
				LOGGER.debug("Created ${dir.absolutePath} directory")
			}
		}
		create(extension.srcDir)
		create(extension.resourcesDir)

		if (extension.markAsTestSources) {
			if (!project.plugins.hasPlugin(IdeaPlugin)) {
				project.plugins.apply(IdeaPlugin)
				LOGGER.debug("Applied IDEA plugin")
			}
			def ideaModule = project.idea.module as IdeaModule
			ideaModule.testSourceDirs += project.file(extension.srcDir)
		}
	}

	private def configureSourceSet(ItPluginExtension extension) {
		def sourceSet = project.sourceSets[SOURCE_SET_NAME] as SourceSet

		def mainSourceSet = project.sourceSets[SourceSet.MAIN_SOURCE_SET_NAME] as SourceSet
		def testSourceSet = project.sourceSets[SourceSet.TEST_SOURCE_SET_NAME] as SourceSet

		sourceSet.compileClasspath += mainSourceSet.output + testSourceSet.output
		sourceSet.runtimeClasspath += mainSourceSet.output + testSourceSet.output

		sourceSet.java.setSrcDirs(project.files(extension.srcDir))
		sourceSet.resources.setSrcDirs(project.files(extension.resourcesDir))
		LOGGER.debug("Configured $SOURCE_SET_NAME source set")
	}

	private def configureConfiguration() {
		def sourceSet = project.sourceSets[SOURCE_SET_NAME] as SourceSet
		def testSourceSet = project.sourceSets[SourceSet.TEST_SOURCE_SET_NAME] as SourceSet

		project.configurations.getByName(sourceSet.compileConfigurationName)
				.extendsFrom(project.configurations.getByName(testSourceSet.compileConfigurationName))
		LOGGER.debug("Configured $sourceSet.compileConfigurationName configuration")

		project.configurations.getByName(sourceSet.runtimeConfigurationName)
				.extendsFrom(project.configurations.getByName(testSourceSet.runtimeConfigurationName))
		LOGGER.debug("Configured $sourceSet.runtimeConfigurationName configuration")
	}

	private def configureTask() {
		def task = project.tasks[ItTask.NAME] as ItTask

		task.dependsOn(project.tasks.findByName('itClasses'))

		def sourceSet = project.sourceSets[SOURCE_SET_NAME] as SourceSet
		task.classpath = sourceSet.runtimeClasspath
		task.testClassesDirs = sourceSet.output.classesDirs
		LOGGER.trace("Created and configured '${ItTask.NAME}' task")
	}

}
