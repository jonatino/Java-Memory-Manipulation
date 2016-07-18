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

package com.beaudoin.jmm.process.impl.win32;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.win32.Kernel32;
import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.NativeProcess;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.Win32Exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 11/13/2015.
 */
public final class Win32Process implements NativeProcess {

    private final int id;
    private final Pointer handle;
    private Map<String, Module> modules = new HashMap<>();

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
        Pointer snapshot = Kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE32.intValue() | Tlhelp32.TH32CS_SNAPMODULE.intValue(), id());
        Kernel32.MODULEENTRY32W entry = new Kernel32.MODULEENTRY32W.ByReference();
        try {
            while (Kernel32.Module32NextW(snapshot, entry)) {
                String name = entry.szModule();
                if (modules.containsKey(name)) {
                    continue;
                }
                modules.put(name, new Module(this, name, entry.getPointer(), entry.modBaseSize.intValue()));
            }
        } finally {
            Kernel32.CloseHandle(snapshot);
        }
    }

    @Override
    public Module findModule(String moduleName) {
        Module module = modules.get(moduleName);
        if (module == null) {
            initModules();
        }
        return module;
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
