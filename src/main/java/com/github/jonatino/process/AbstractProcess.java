package com.github.jonatino.process;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 7/19/2016.
 */
public abstract class AbstractProcess implements Process {

	protected Map<String, Module> modules = new HashMap<>();

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
		}
		return module;
	}

}
