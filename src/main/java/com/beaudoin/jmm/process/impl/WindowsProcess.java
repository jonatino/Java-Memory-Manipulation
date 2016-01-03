package com.beaudoin.jmm.process.impl;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.natives.windows.Kernel32;
import com.beaudoin.jmm.natives.windows.Psapi;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Win32Exception;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Jonathan on 11/13/2015.
 */
public final class WindowsProcess implements NativeProcess {

	private final int id;
	private final Pointer handle;
	private Map<String, Module> modules;

	public WindowsProcess(int id, Pointer handle) {
		this.id = id;
		this.handle = handle;
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public Pointer pointer() {
		return handle;
	}

	@Override
	public Module findModule(String moduleName) {
		if (modules == null) {
			modules = Psapi.getModules(this);
		}
		return modules.get(moduleName);
	}

	@Override
	public ByteBuffer read(Pointer address, int bytesToRead) {
		ByteBuffer buffer = Cacheable.buffer(bytesToRead);
		if (!Kernel32.ReadProcessMemory(pointer(), address, buffer, bytesToRead, 0)) {
			throw new Win32Exception(Native.getLastError());
		}
		return buffer;
	}

	@Override
	public boolean canRead(Pointer address, int bytesToRead) {
		return Kernel32.ReadProcessMemory(pointer(), address, Cacheable.buffer(bytesToRead), bytesToRead, 0);
	}

}
