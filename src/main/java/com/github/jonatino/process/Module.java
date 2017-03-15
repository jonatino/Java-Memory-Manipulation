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

package com.github.jonatino.process;


import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.misc.MemoryBuffer;
import com.sun.jna.Pointer;

public final class Module implements DataSource {

    private final Process process;
    private final String name;
    private final long address;
    private final int size;
    private final Pointer pointer;
    private final String permissions;
    private MemoryBuffer data;

    public Module(Process process, String name, Pointer pointer, long size) {
        this.process = process;
        this.name = name;
        this.address = Pointer.nativeValue(pointer);
        this.size = (int) size;
        this.pointer = pointer;
        this.permissions = "rwxp";
    }
    
    public Module(Process process, String name, Pointer pointer, long size, String permissions) {
        this.process = process;
        this.name = name;
        this.address = Pointer.nativeValue(pointer);
        this.size = (int) size;
        this.pointer = pointer;
        this.permissions = permissions;
    }

    public Process process() {
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

    public long start() {
        return address;
    }
    
    public long end() {
        return address+size;
    }
    
    public boolean isReadable() {
        return permissions.charAt(0) == 'r';
    }
    
    public boolean isWritable() {
        return permissions.charAt(1) == 'w';
    }
    
    public boolean isExecutable() {
        return permissions.charAt(2) == 'x';
    }
    
    public boolean isShared() {
        return permissions.charAt(3) != '-';
    }

    public MemoryBuffer data() {
        return data(false);
    }

    public MemoryBuffer data(boolean forceNew) {
        return data == null || forceNew ? data = process().read(pointer(), size()) : data;
    }

    @Override
    public MemoryBuffer read(Pointer address, int size) {
        return process().read(address, size);
    }

    @Override
    public MemoryBuffer read(Pointer address, int size, MemoryBuffer buffer) {
        return process().read(address, size, buffer);
    }
    
    @Override
    public Process write(Pointer address, MemoryBuffer buffer) {
        return process().write(address, buffer);
    }

    @Override
    public boolean canRead(Pointer offset, int size) {
        return process().canRead(Cacheable.pointer(Pointer.nativeValue(offset)), size);
    }

    public long GetAbsoluteAddress(long address, int offset, int size) {
        long code = (int) readPointer(address + offset);
        if(code > 0) return code + address + size;
        return 0;
    }
    
    public long GetCallAddress(long address) {
        return GetAbsoluteAddress(address, 1, 5);
    }
    
    @Override
    public String toString() {
        return "Module{" + "name='" + name + '\'' + ", address=" + address + ", size=" + size + '}';
    }

}
