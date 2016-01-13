package com.beaudoin.jmm.process.impl.win32;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.win32.Kernel32;
import com.beaudoin.jmm.natives.win32.Psapi;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Win32Exception;

import java.util.Map;

/**
 * Created by Jonathan on 11/13/2015.
 */
public final class Win32Process implements NativeProcess {

	private final int id;
	private final Pointer handle;
	private Map<String, Module> modules;

	public Win32Process(int id, Pointer handle) {
		this.id = id;
		this.handle = handle;
		initModules();
	}

	public Pointer pointer() {
		return handle;
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public void initModules() {
		modules = Psapi.getModules(this);
	}

	@Override
	public Module findModule(String moduleName) {
		return modules.get(moduleName);
	}

	@Override
	public MemoryBuffer read(Pointer address, int size) {
		MemoryBuffer buffer = Cacheable.buffer(size);
		if (Kernel32.ReadProcessMemory(pointer(), address, buffer, size, 0) == 0) {
			throw new Win32Exception(Native.getLastError());
		}
		return buffer;
	}

	@Override
	public NativeProcess write(Pointer address, MemoryBuffer buffer) {
		if (Kernel32.WriteProcessMemory(pointer(), address, buffer, buffer.size(), 0) == 0) {
			throw new Win32Exception(Native.getLastError());
		}
		return this;
	}

	@Override
	public boolean canRead(Pointer address, int size) {
		return Kernel32.ReadProcessMemory(pointer(), address, Cacheable.buffer(size), size, 0) != 0;
	}

}
