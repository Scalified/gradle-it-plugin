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
import org.gradle.api.Project

/**
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
class ItPluginExtension {

	@PackageScope
	static final String NAME = 'it'

	String srcDir = 'src/it/java'
	String resourcesDir = "src/it/resources"
	boolean markAsTestSources = true

	@PackageScope
	OptionsExtension optionsExtension = new OptionsExtension()

	private final Project project

	ItPluginExtension(Project project) {
		this.project = project
	}

	OptionsExtension options(Closure closure) {
		optionsExtension = project.configure(new OptionsExtension(), closure) as OptionsExtension
	}

}
