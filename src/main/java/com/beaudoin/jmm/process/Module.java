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

package com.beaudoin.jmm.process;


import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.win32.Kernel32;
import com.beaudoin.jmm.process.impl.win32.Win32Process;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Win32Exception;

public final class Module implements ReadableRegion {

    private final NativeProcess process;
    private final String name;
    private final long address;
    private final int size;
    private final Pointer pointer;
    private MemoryBuffer data;

    public Module(NativeProcess process, String name, Pointer pointer, long size) {
        this.process = process;
        this.name = name;
        this.address = Pointer.nativeValue(pointer);
        this.size = (int) size;
        this.pointer = pointer;
    }

    public NativeProcess process() {
        return process;
    }

    public Pointer pointer() {
        return pointer;
    }

    public String name() {
        return name;
    }

    public int size() {
        return size;
    }

    public long address() {
        return address;
    }

    public MemoryBuffer data() {
        return data(false);
    }

    public MemoryBuffer data(boolean forceNew) {
        if (forceNew || data == null) {
            data = process().read(pointer(), size());
        }
        return data;
    }

    @Override
    public MemoryBuffer read(Pointer offset, int size) {
        MemoryBuffer buffer = Cacheable.buffer(size);
        if (Kernel32.ReadProcessMemory(((Win32Process) process()).pointer(), Cacheable.pointer(address() + Pointer.nativeValue(offset)), buffer, size, 0) == 0) {
            throw new Win32Exception(Native.getLastError());
        }
        return buffer;
    }

    @Override
    public NativeProcess write(Pointer offset, MemoryBuffer buffer) {
        if (Kernel32.WriteProcessMemory(((Win32Process) process()).pointer(), Cacheable.pointer(address() + Pointer.nativeValue(offset)), buffer, buffer.size(), 0) == 0) {
            throw new Win32Exception(Native.getLastError());
        }
        return process();
    }

    @Override
    public boolean canRead(Pointer offset, int size) {
        return Kernel32.ReadProcessMemory(((Win32Process) process()).pointer(), Cacheable.pointer(address() + Pointer.nativeValue(offset)), Cacheable.buffer(size), size, 0) != 0;
    }

}
