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

package com.github.jonatino.process.impl.win32;

import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.natives.win32.Kernel32;
import com.github.jonatino.process.AbstractProcess;
import com.github.jonatino.process.Module;
import com.github.jonatino.process.Process;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.Win32Exception;

/**
 * Created by Jonathan on 11/13/2015.
 */
public final class Win32Process extends AbstractProcess {

    private final Pointer handle;

    public Win32Process(int id, Pointer handle) {
        super(id);
        this.handle = handle;
    }

    public Pointer pointer() {
        return handle;
    }

    @Override
    public void initModules() {
        Pointer snapshot = Kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE32.intValue() | Tlhelp32.TH32CS_SNAPMODULE.intValue(), id());
        Tlhelp32.MODULEENTRY32W entry = new Tlhelp32.MODULEENTRY32W.ByReference();
        try {
            while (Kernel32.Module32NextW(snapshot, entry)) {
                String name = entry.szModule();
                modules.put(name, new Module(this, name, entry.hModule.getPointer(), entry.modBaseSize.intValue()));
            }
        } finally {
            Kernel32.CloseHandle(snapshot);
        }
    }

    @Override
    public MemoryBuffer read(Pointer address, int size, MemoryBuffer buffer) {
        if (Kernel32.ReadProcessMemory(pointer(), address, buffer, size, 0) == 0) {
            throw new Win32Exception(Native.getLastError());
        }
        return buffer;
    }

    @Override
    public Process write(Pointer address, MemoryBuffer buffer) {
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
