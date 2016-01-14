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

import com.beaudoin.jmm.process.Module;
import com.beaudoin.jmm.process.impl.win32.Win32Process;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Psapi {

	private static PsapiStdCall psapi = (PsapiStdCall) Native.loadLibrary("Psapi", PsapiStdCall.class);

	static {
		Native.register(NativeLibrary.getInstance("Psapi"));
	}

	public static Map<String, Module> getModules(Win32Process process) {
		Map<String, Module> modules = new HashMap<>();

		WinDef.HMODULE[] lphModules = new WinDef.HMODULE[1024];
		IntByReference lpcbNeededs = new IntByReference();
		PsapiStdCall.LPMODULEINFO moduleInfo = new PsapiStdCall.LPMODULEINFO();

		EnumProcessModulesEx(process.pointer(), lphModules, lphModules.length, lpcbNeededs, 0x03);
		for (int i = 0; i < lpcbNeededs.getValue() / 4; i++) {
			WinDef.HMODULE hModule = lphModules[i];
			if (GetModuleInformation(process.pointer(), hModule, moduleInfo, moduleInfo.size())) {
				if (moduleInfo.lpBaseOfDll != null) {
					String moduleName = GetModuleBaseNameA(process.pointer(), hModule);
					modules.put(moduleName, new Module(process, moduleName, hModule.getPointer(), moduleInfo.SizeOfImage));
				}
			}
		}
		return modules;
	}

	public static native boolean GetModuleInformation(Pointer hProcess, WinDef.HMODULE hModule, PsapiStdCall.LPMODULEINFO lpmodinfo, int cb);

	public static native int GetModuleBaseNameA(Pointer hProcess, WinDef.HMODULE hModule, byte[] lpImageFileName, int nSize);

	private static String GetModuleBaseNameA(Pointer hProcess, WinDef.HMODULE hModule) {
		byte[] lpImageFileName = new byte[128];
		GetModuleBaseNameA(hProcess, hModule, lpImageFileName, lpImageFileName.length);
		return Native.toString(lpImageFileName);
	}

	public static boolean EnumProcessModulesEx(Pointer hProcess, WinDef.HMODULE[] lphModule, int cb, IntByReference lpcbNeededs, int flags) {
		return psapi.EnumProcessModulesEx(hProcess, lphModule, cb, lpcbNeededs, flags);
	}

	private interface PsapiStdCall extends StdCallLibrary {

		boolean EnumProcessModulesEx(Pointer hProcess, WinDef.HMODULE[] lphModule, int cb, IntByReference lpcbNeededs, int flags);

		class LPMODULEINFO extends Structure {

			public WinNT.HANDLE lpBaseOfDll;
			public int SizeOfImage;
			public WinNT.HANDLE EntryPoint;

			@Override
			protected List<String> getFieldOrder() {
				return Arrays.asList("lpBaseOfDll", "SizeOfImage", "EntryPoint");
			}
		}

	}
}
