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

package com.beaudoin.jmm.process.impl.mac;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.mac.mac;
import com.beaudoin.jmm.natives.unix.unix;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Pointer;

import java.util.HashMap;
import java.util.Map;

import static com.beaudoin.jmm.misc.Cacheable.INT_BY_REF;

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
        MemoryBuffer buffer = Cacheable.buffer(size);
        if (mac.vm_read(task(), address, size, buffer, INT_BY_REF) != 0 || INT_BY_REF.getValue() != size) {
            throw new RuntimeException("Read memory failed at address " + Pointer.nativeValue(address) + " size " + size);
        }
        Pointer.nativeValue(buffer, Pointer.nativeValue(buffer.getPointer(0)));
        return buffer;
    }

    @Override
    public NativeProcess write(Pointer address, MemoryBuffer buffer) {
        if (mac.vm_write(task(), address, buffer, buffer.size()) != 0) {
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