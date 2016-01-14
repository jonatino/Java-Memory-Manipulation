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
import com.beaudoin.jmm.misc.Strings;
import com.beaudoin.jmm.misc.Utils;
import com.beaudoin.jmm.natives.mac.mac;
import com.beaudoin.jmm.natives.unix.libc;
import com.beaudoin.jmm.natives.unix.unix;
import com.beaudoin.jmm.natives.win32.Kernel32;
import com.beaudoin.jmm.process.impl.mac.MacProcess;
import com.beaudoin.jmm.process.impl.unix.UnixProcess;
import com.beaudoin.jmm.process.impl.win32.Win32Process;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.ptr.IntByReference;

import static com.beaudoin.jmm.misc.Cacheable.buffer;

/**
 * Created by Jonathan on 12/12/15.
 */
public interface NativeProcess {

    static NativeProcess byName(String name) {
        if (Platform.isWindows()) {
            Tlhelp32.PROCESSENTRY32.ByReference entry = new Tlhelp32.PROCESSENTRY32.ByReference();
            Pointer snapshot = Kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL, 0);
            try {
                while (Kernel32.Process32Next(snapshot, entry)) {
                    String processName = Native.toString(entry.szExeFile);
                    if (name.equals(processName)) {
                        return byId(entry.th32ProcessID.intValue());
                    }
                }
            } finally {
                Kernel32.CloseHandle(snapshot);
            }
        } else if (Platform.isMac() || Platform.isLinux()) {
            return byId(Utils.exec("bash", "-c", "ps -A | grep -m1 \""+name+"\" | awk '{print $1}'"));
        } else {
            throw new UnsupportedOperationException("Unknown operating system! (" + System.getProperty("os.name") + ")");
        }
        throw new IllegalStateException("Process " + name + " was not found. Are you sure its running?");
    }

    static NativeProcess byId(int id) {
        if ((Platform.isMac() || Platform.isLinux()) && !checkSudo()) {
            throw new RuntimeException("You need to run as root/sudo in order for functionality.");
        }
        if (Platform.isWindows()) {
            return new Win32Process(id, Kernel32.OpenProcess(0x438, true, id));
        } else if (Platform.isMac()) {
            IntByReference out = new IntByReference();
            if (mac.task_for_pid(mac.mach_task_self(), id, out) != 0) {
                throw new IllegalStateException("Failed to find mach task port for process, ensure you are running as sudo.");
            }
            return new MacProcess(id, out.getValue());
        } else if (Platform.isLinux()) {
            return new UnixProcess(id);
        } else {
            throw new IllegalStateException("Process " + id + " was not found. Are you sure its running?");
        }
    }

    static boolean checkSudo() {
        return libc.getuid() == 0;
    }

    int id();

    void initModules();

    Module findModule(String moduleName);

    MemoryBuffer read(Pointer address, int size);

    NativeProcess write(Pointer address, MemoryBuffer buffer);

    boolean canRead(Pointer address, int size);

    default int readByte(long address) {
        return read(address, 1).getByte();
    }

    default int readShort(long address) {
        return read(address, 2).getShort();
    }

    default int readInt(long address) {
        return read(address, 4).getInt();
    }

    default long readLong(long address) {
        return read(address, 8).getLong();
    }

    default float readFloat(long address) {
        return read(address, 4).getFloat();
    }

    default double readDouble(long address) {
        return read(address, 8).getDouble();
    }

    default String readString(long address, int length) {
        byte[] bytes = new byte[length];
        read(address, bytes.length).get(bytes);
        return Strings.transform(bytes);
    }

    default MemoryBuffer read(long address, int size) {
        return read(Cacheable.pointer(address), size);
    }

    default NativeProcess writeBoolean(long address, boolean value) {
        return write(Cacheable.pointer(address), buffer(1).putBoolean(value));
    }

    default NativeProcess writeByte(long address, int value) {
        return write(Cacheable.pointer(address), buffer(1).putByte(value));
    }

    default NativeProcess writeShort(long address, int value) {
        return write(Cacheable.pointer(address), buffer(2).putShort(value));
    }

    default NativeProcess writeInt(long address, int value) {
        return write(Cacheable.pointer(address), buffer(4).putInt(value));
    }

    default NativeProcess writeLong(long address, long value) {
        return write(Cacheable.pointer(address), buffer(8).putLong(value));
    }

    default NativeProcess writeFloat(long address, float value) {
        return write(Cacheable.pointer(address), buffer(4).putFloat(value));
    }

    default NativeProcess writeDouble(long address, double value) {
        return write(Cacheable.pointer(address), buffer(8).putDouble(value));
    }

    default boolean canRead(long address, int size) {
        return canRead(Cacheable.pointer(address), size);
    }
}
