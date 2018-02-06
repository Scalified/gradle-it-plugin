/*
 *
 * Copyright 2015 Shell Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File created: 2018-02-04 10:14:22
 */

package com.scalified.plugins.gradle.it

import org.gradle.api.Project

/**
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
class ItPluginExtension {

	public static final String NAME = 'it'

	String srcDir = 'src/it/java'
	String resourcesDir = "src/it/resources"

	OptionsExtension optionsExtension = new OptionsExtension()

	private Project project

	ItPluginExtension(Project project) {
		this.project = project
	}

	OptionsExtension options(Closure closure) {
		optionsExtension = project.configure(new OptionsExtension(), closure) as OptionsExtension
	}

	OptionsExtension getOptions() {
		optionsExtension
	}

}
