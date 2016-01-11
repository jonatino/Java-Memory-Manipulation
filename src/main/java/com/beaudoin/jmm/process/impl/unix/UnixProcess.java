package com.beaudoin.jmm.process.impl.unix;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.unix.uio;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 1/10/2016.
 */
public final class UnixProcess implements NativeProcess {

	private uio.iovec local = new uio.iovec();
	private uio.iovec remote = new uio.iovec();

	private final int id;
	private final Pointer handle;
	private Map<String, Module> modules = new HashMap<>();

	public UnixProcess(int id, Pointer handle) {
		this.id = id;
		this.handle = handle;
		initModules();
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
	public void initModules() {
		try {
			for (String line : Files.readAllLines(Paths.get("/proc/" + id() + "/maps"))) {
				String[] split = line.split(" ");
				String[] regionSplit = split[0].split("-");

				long start = Long.parseLong(regionSplit[0], 16);
				long end = Long.parseLong(regionSplit[1], 16);
				long offset = Long.parseLong(split[2], 16);
				if (offset <= 0) {
					continue;
				}
				String path = "";
				for (int i = 5; i < split.length; i++) {
					String s = split[i].trim();
					if (!s.isEmpty()) {
						path += split[i];
					}
					if (s.isEmpty() && ++i > split.length) {
						break;
					} else if (s.isEmpty() && !split[i].trim().isEmpty()) {
						path += split[i];
					}
				}
				String modulename = path.substring(path.lastIndexOf("/") + 1, path.length());
				modules.put(modulename, new Module(this, modulename, Pointer.createConstant(start), end - start));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Module findModule(String moduleName) {
		return modules.get(moduleName);
	}

	@Override
	public MemoryBuffer read(Pointer address, int size) {
		MemoryBuffer buffer = Cacheable.buffer(size);
		local.iov_base = buffer;
		remote.iov_base = address;
		remote.iov_len = local.iov_len = size;
		if (uio.process_vm_readv(id, local, 1, remote, 1, 0) != size) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
		}
		return buffer;
	}

	@Override
	public NativeProcess write(Pointer address, MemoryBuffer buffer) {
		local.iov_base = buffer;
		remote.iov_base = address;
		remote.iov_len = local.iov_len = buffer.size();
		if (uio.process_vm_writev(id, local, 1, remote, 1, 0) != buffer.size()) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + buffer.size());
		}
		return this;
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