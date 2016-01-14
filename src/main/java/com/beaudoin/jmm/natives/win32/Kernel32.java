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

package com.beaudoin.jmm.natives.win32;

import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

public final class Kernel32 {

	static {
		Native.register(NativeLibrary.getInstance("Kernel32", W32APIOptions.UNICODE_OPTIONS));
	}

	public static native Pointer CreateToolhelp32Snapshot(WinDef.DWORD dword, int junk);

	public static native boolean CloseHandle(Pointer pointer);

	public static native Pointer OpenProcess(int desired, boolean inherit, int pid);

	public static native boolean Process32Next(Pointer pointer, Tlhelp32.PROCESSENTRY32 entry);

	public static native long ReadProcessMemory(Pointer process, Pointer address, MemoryBuffer memory, int size, int written);

	public static native long WriteProcessMemory(Pointer process, Pointer address, MemoryBuffer memory, int size, int written);

}
