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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
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

	public static final String COMPILE_CONFIGURATION_NAME = 'itCompile'

	public static final String RUNTIME_CONFIGURATION_NAME = 'itRuntime'

	public static final String SOURCE_SET_NAME = 'it'

	private static final Logger LOGGER = LoggerFactory.getLogger(ItPlugin)

	private Project project

	private ItPluginExtension extension

	@Override
	void apply(Project project) {
		this.project = project
		if (project.plugins.hasPlugin(JavaPlugin)) {
			this.extension = project.extensions.create(ItPluginExtension.NAME, ItPluginExtension, project)
			project.afterEvaluate { p ->
				createMissingDirectories()
				createItCompileConfiguration()
				createItRuntimeConfiguration()
				def sourceSet = createItSourceSet()
				createItTask(sourceSet)
			}
		} else {
			LOGGER.warn("Failed to apply integration plugin. Java plugin must be applied first")
		}
	}

	def createMissingDirectories() {
		def create = { path ->
			def dir = project.file(path)
			if (!dir.exists()) {
				dir.mkdirs()
				LOGGER.debug("Directory ${dir.absolutePath} created")
			}
		}
		create(extension.srcDir)
		create(extension.resourcesDir)
	}

	def createItCompileConfiguration() {
		def compileConfiguration = project.configurations.create(COMPILE_CONFIGURATION_NAME)
		compileConfiguration.extendsFrom(project.configurations.getByName('testCompile'))
		LOGGER.trace("Integration test compile configuration '$COMPILE_CONFIGURATION_NAME' created")
		compileConfiguration
	}

	def createItRuntimeConfiguration() {
		def runtimeConfiguration = project.configurations.create(RUNTIME_CONFIGURATION_NAME)
		runtimeConfiguration.extendsFrom(project.configurations.getByName('testRuntime'))
		LOGGER.trace("Integration test runtime configuration '$RUNTIME_CONFIGURATION_NAME' created")
		runtimeConfiguration
	}

	def createItSourceSet() {
		def sourceSets = project.sourceSets as SourceSetContainer
		def sourceSet = sourceSets.create(SOURCE_SET_NAME)
		def mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
		def testSourceSet = sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME)
		sourceSet.compileClasspath += mainSourceSet.output + testSourceSet.output
		sourceSet.runtimeClasspath += mainSourceSet.output + testSourceSet.output
		sourceSet.java.setSrcDirs(project.files(extension.srcDir))
		sourceSet.resources.setSrcDirs(project.files(extension.resourcesDir))

		project.getPluginManager().apply(IdeaPlugin)
		def ideaModule = project.idea.module as IdeaModule
		ideaModule.testSourceDirs += project.file(extension.srcDir)

		LOGGER.trace("Integration test source set '$SOURCE_SET_NAME' created")
		sourceSet
	}

	def createItTask(SourceSet sourceSet) {
		def task = project.tasks.create(ItTask.NAME, ItTask)
		task.group = 'verification'
		task.description = 'Executes integration tests'
		populateTaskProperties(task, extension.optionsExtension)
		task.dependsOn(project.tasks.findByName('itClasses'))
		task.classpath = sourceSet.runtimeClasspath
		task.testClassesDirs = sourceSet.output.classesDirs
		project.tasks.add(task)
		LOGGER.trace("Integration test '${ItTask.NAME}' task created")
	}

	static def populateTaskProperties(Task task, OptionsExtension extension) {
		OptionsExtension.declaredFields.findAll { !it.synthetic } collect { it.name } each { key ->
			if (task.hasProperty(key)) {
				def value = extension.properties[key]
				task[key] = value
				LOGGER.trace("Set '$value' value to '$key' property")
			}
		}
	}

}
