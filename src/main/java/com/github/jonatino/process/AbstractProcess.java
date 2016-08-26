/*
 *    Copyright 2016 Jonathan Beaudoin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jonatino.process;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.Map;

/**
 * Created by Jonathan on 7/19/2016.
 */
public abstract class AbstractProcess implements Process {

	protected Map<String, Module> modules = new Object2ObjectArrayMap<>();

	private final int id;

	public AbstractProcess(int id) {
		this.id = id;
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public Module findModule(String moduleName) {
		Module module = modules.isEmpty() ? null : modules.get(moduleName);
		if (module == null) {
			initModules();
			module = modules.isEmpty() ? null : modules.get(moduleName);
		}
		return module;
	}

}
