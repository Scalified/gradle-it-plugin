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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.testing.Test

/**
 * @author shell
 * @since 2019-10-08
 */
internal const val IT_TASK_NAME = "it"

internal const val MAX_HEAP_SIZE = "256m"

internal const val MAX_PARALLEL_FORKS = 4

open class ItTask : Test() {

	init {
		group = IT_PLUGIN_GROUP
		description = IT_PLUGIN_DESCRIPTION
	}

	@Input
	var srcDir = "src/$IT_PLUGIN_NAME/java"

	@Input
	var resourcesDir = "src/$IT_PLUGIN_NAME/resources"

}
