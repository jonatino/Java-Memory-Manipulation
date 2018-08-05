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

package com.github.jonatino.natives.win32;

import com.github.jonatino.misc.MemoryBuffer;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.win32.W32APIOptions;

public final class Kernel32 {

	static {
		Native.register(NativeLibrary.getInstance("Kernel32", W32APIOptions.UNICODE_OPTIONS));
	}

	public static native Pointer CreateToolhelp32Snapshot(int flags, int pid);

	public static native boolean CloseHandle(Pointer pointer);

	public static native Pointer OpenProcess(int desired, boolean inherit, int pid);
	
	public static native boolean Process32NextW(Pointer pointer, Tlhelp32.PROCESSENTRY32 entry);

	public static native boolean Module32NextW(Pointer pointer, Tlhelp32.MODULEENTRY32W entry);

	public static native long ReadProcessMemory(Pointer process, Pointer address, MemoryBuffer memory, int size, int written);

	public static native long WriteProcessMemory(Pointer process, Pointer address, MemoryBuffer memory, int size, int written);

}
