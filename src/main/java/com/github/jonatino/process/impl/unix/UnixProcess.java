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

package com.github.jonatino.process.impl.unix;

import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.unix.unix;
import com.github.jonatino.process.AbstractProcess;
import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Jonathan on 1/10/2016.
 */
public final class UnixProcess extends AbstractProcess {
	
	private unix.iovec local = new unix.iovec();
	private unix.iovec remote = new unix.iovec();
	
	public UnixProcess(int id) {
		super(id);
		local.setAutoSynch(false);
		remote.setAutoSynch(false);
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
	public MemoryBuffer read(Pointer address, int size, MemoryBuffer buffer) {
		populate(address, buffer);
		if (unix.process_vm_readv(id(), local, 1, remote, 1, 0) != size) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
		}
		return buffer;
	}
	
	@Override
	public Process write(Pointer address, MemoryBuffer buffer) {
		populate(address, buffer);
		if (unix.process_vm_writev(id(), local, 1, remote, 1, 0) != buffer.size()) {
			throw new RuntimeException("Write memory failed at address " + Pointer.nativeValue(address) + " size " + buffer.size());
		}
		return this;
	}
	
	@Override
	public boolean canRead(Pointer address, int size) {
		populate(address, Cacheable.buffer(size));
		return unix.process_vm_readv(id(), local, 1, remote, 1, 0) == size;
	}
	
	private void populate(Pointer address, MemoryBuffer buffer) {
		local.writeField("iov_base", buffer);
		local.writeField("iov_len", buffer.size());
		remote.writeField("iov_base", address);
		remote.writeField("iov_len", buffer.size());
	}
	
}