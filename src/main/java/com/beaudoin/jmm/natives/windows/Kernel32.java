package com.beaudoin.jmm.natives.windows;

import com.sun.jna.Memory;
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

	public static native long ReadProcessMemory(Pointer process, Pointer address, Memory memory, int size, int written);

	public static native long WriteProcessMemory(Pointer process, Pointer address, Memory memory, int size, int written);

}
