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
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

import java.util.Arrays;
import java.util.List;

public final class Kernel32 {

	static {
		Native.register(NativeLibrary.getInstance("Kernel32", W32APIOptions.UNICODE_OPTIONS));
	}

	public static native Pointer CreateToolhelp32Snapshot(int flags, int pid);

	public static native boolean CloseHandle(Pointer pointer);

	public static native Pointer OpenProcess(int desired, boolean inherit, int pid);

	public static native boolean Process32Next(Pointer pointer, Tlhelp32.PROCESSENTRY32 entry);

	public static native boolean Module32NextW(Pointer pointer, Kernel32.MODULEENTRY32W entry);

	public static native long ReadProcessMemory(Pointer process, Pointer address, MemoryBuffer memory, int size, int written);

	public static native long WriteProcessMemory(Pointer process, Pointer address, MemoryBuffer memory, int size, int written);

	/**
	 * Describes an entry from a list of the modules belonging to the specified
	 * process.
	 *
	 * @see <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684225(v=vs.85).aspx">MSDN</a>
	 */
	public static class MODULEENTRY32W extends Structure {

		public static final int MAX_MODULE_NAME32 = 255;

		/**
		 * A representation of a MODULEENTRY32 structure as a reference
		 */
		public static class ByReference extends MODULEENTRY32W implements Structure.ByReference {
			public ByReference() {
			}

			public ByReference(Pointer memory) {
				super(memory);
			}
		}

		/**
		 * The size of the structure, in bytes. Before calling the Module32First
		 * function, set this member to sizeof(MODULEENTRY32). If you do not
		 * initialize dwSize, Module32First fails.
		 */
		public WinDef.DWORD dwSize;

		/**
		 * This member is no longer used, and is always set to one.
		 */
		public WinDef.DWORD th32ModuleID;

		/**
		 * The identifier of the process whose modules are to be examined.
		 */
		public WinDef.DWORD th32ProcessID;

		/**
		 * The load count of the module, which is not generally meaningful, and
		 * usually equal to 0xFFFF.
		 */
		public WinDef.DWORD GlblcntUsage;

		/**
		 * The load count of the module (same as GlblcntUsage), which is not
		 * generally meaningful, and usually equal to 0xFFFF.
		 */
		public WinDef.DWORD ProccntUsage;

		/**
		 * The base address of the module in the context of the owning process.
		 */
		public Pointer modBaseAddr;

		/**
		 * The size of the module, in bytes.
		 */
		public WinDef.DWORD modBaseSize;

		/**
		 * A handle to the module in the context of the owning process.
		 */
		public WinDef.HMODULE hModule;

		/**
		 * The module name.
		 */
		public char[] szModule = new char[MAX_MODULE_NAME32 + 1];

		/**
		 * The module path.
		 */
		public char[] szExePath = new char[com.sun.jna.platform.win32.Kernel32.MAX_PATH];

		public MODULEENTRY32W() {
			dwSize = new WinDef.DWORD(size());
		}

		public MODULEENTRY32W(Pointer memory) {
			super(memory);
			read();
		}

		/**
		 * @return The module name.
		 */
		public String szModule() {
			return Native.toString(this.szModule);
		}

		/**
		 * @return The module path.
		 */
		public String szExePath() {
			return Native.toString(this.szExePath);
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("dwSize", "th32ModuleID", "th32ProcessID", "GlblcntUsage", "ProccntUsage", "modBaseAddr", "modBaseSize", "hModule", "szModule", "szExePath");
		}
	}

}
