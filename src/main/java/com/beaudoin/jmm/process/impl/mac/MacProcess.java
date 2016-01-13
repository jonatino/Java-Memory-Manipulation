package com.beaudoin.jmm.process.impl.mac;

import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Pointer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 1/10/2016.
 */
public final class MacProcess implements NativeProcess {

	private final int id;
	private final int task;
	private Map<String, Module> modules = new HashMap<>();

	public MacProcess(int id, int mach_task) {
		this.id = id;
		this.task = mach_task;
		initModules();
	}

	public int task() {
		return task;
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public void initModules() {
		//TODO
	}

	@Override
	public Module findModule(String moduleName) {
		//TODO
		return null;
	}

	@Override
	public MemoryBuffer read(Pointer address, int size) {

		return null;
	}

	@Override
	public NativeProcess write(Pointer address, MemoryBuffer buffer) {
		//TODO
		return null;
	}

	@Override
	public boolean canRead(Pointer address, int size) {
		try {
			read(address, size);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}