/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jonathan Beaudoin
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

package com.beaudoin.jmm.process.impl.unix;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.unix.unix;
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

	private final int id;
	private unix.iovec local = new unix.iovec();
	private unix.iovec remote = new unix.iovec();
	private Map<String, Module> modules = new HashMap<>();

	public UnixProcess(int id) {
		this.id = id;
		initModules();
	}

	@Override
	public int id() {
		return id;
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
		if (unix.process_vm_readv(id, local, 1, remote, 1, 0) != size) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
		}
		return buffer;
	}

	@Override
	public NativeProcess write(Pointer address, MemoryBuffer buffer) {
		local.iov_base = buffer;
		remote.iov_base = address;
		remote.iov_len = local.iov_len = buffer.size();
		if (unix.process_vm_writev(id, local, 1, remote, 1, 0) != buffer.size()) {
			throw new RuntimeException("Write memory failed at address " + Pointer.nativeValue(address) + " size " + buffer.size());
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