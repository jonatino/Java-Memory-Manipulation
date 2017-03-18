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

package com.github.jonatino.process.impl.mac;

import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.mac.mac;
import com.github.jonatino.process.AbstractProcess;
import com.github.jonatino.process.Process;
import com.sun.jna.Pointer;

import static com.github.jonatino.misc.Cacheable.INT_BY_REF;

/**
 * Created by Jonathan on 1/10/2016.
 */
public final class MacProcess extends AbstractProcess {
	
	private final int task;
	
	public MacProcess(int id, int mach_task) {
		super(id);
		this.task = mach_task;
	}
	
	public int task() {
		return task;
	}
	
	@Override
	public void initModules() {
		//TODO
	}
	
	@Override
	public MemoryBuffer read(Pointer address, int size, MemoryBuffer buffer) {
		if (mac.vm_read(task(), address, size, buffer, INT_BY_REF) != 0 || INT_BY_REF.getValue() != size) {
			throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
		}
		Pointer.nativeValue(buffer, Pointer.nativeValue(buffer.getPointer(0)));
		return buffer;
	}
	
	@Override
	public Process write(Pointer address, MemoryBuffer buffer) {
		if (mac.vm_write(task(), address, buffer, buffer.size()) != 0) {
			throw new RuntimeException("Write memory failed at address " + Pointer.nativeValue(address) + " size " + buffer.size());
		}
		return this;
	}
	
	@Override
	public boolean canRead(Pointer address, int size) {
		return mac.vm_read(task(), address, size, Cacheable.buffer(size), INT_BY_REF) == 0 && INT_BY_REF.getValue() == size;
	}
	
}