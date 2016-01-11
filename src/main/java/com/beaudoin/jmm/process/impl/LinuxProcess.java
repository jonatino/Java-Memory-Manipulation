package com.beaudoin.jmm.process.impl;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.natives.linux.uio;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.util.Map;

/**
 * Created by Jonathan on 1/10/2016.
 */
public final class LinuxProcess implements NativeProcess {

	private final int id;
	private final Pointer handle;
	private Map<String, Module> modules;

	public LinuxProcess(int id, Pointer handle) {
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
		//TODO
		return null;
	}

	@Override
	public Memory read(Pointer address, int size) {
		Memory buffer = Cacheable.buffer(size);
		uio.iovec local = new uio.iovec();
		local.iov_base = buffer;
		local.iov_len = size;

		uio.iovec remote = new uio.iovec();
		remote.iov_base = address;
		remote.iov_len = size;

		if (uio.process_vm_readv(id, local, 1, remote, 1, 0) != size) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
		}
		return buffer;
	}

	@Override
	public NativeProcess write(Pointer address, Memory buffer) {
		uio.iovec local = new uio.iovec();
		local.iov_base = buffer;
		local.iov_len = (int) buffer.size();

		uio.iovec remote = new uio.iovec();
		remote.iov_base = address;
		remote.iov_len = (int) buffer.size();

		if (uio.process_vm_writev(id, local, 1, remote, 1, 0) != buffer.size()) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + buffer.size());
		}
		return this;
	}

	@Override
	public boolean canRead(Pointer address, int size) {
		return false;//Kernel32.ReadProcessMemory(pointer(), address, Cacheable.buffer(bytesToRead), bytesToRead, 0) == 0;
	}

}