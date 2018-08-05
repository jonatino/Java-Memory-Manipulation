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

import com.github.jonatino.misc.Utils;
import com.github.jonatino.natives.mac.mac;
import com.github.jonatino.natives.unix.libc;
import com.github.jonatino.natives.win32.Kernel32;
import com.github.jonatino.process.impl.mac.MacProcess;
import com.github.jonatino.process.impl.unix.UnixProcess;
import com.github.jonatino.process.impl.win32.Win32Process;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.ptr.IntByReference;

/**
 * Created by Jonathan on 7/19/2016.
 */
public final class Processes {
	
	public static Process byName(String name) {
		if (Platform.isWindows()) {
			Tlhelp32.PROCESSENTRY32.ByReference entry = new Tlhelp32.PROCESSENTRY32.ByReference();
			Pointer snapshot = Kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL.intValue(), 0);
			try {
				while (Kernel32.Process32NextW(snapshot, entry)) {
					String processName = Native.toString(entry.szExeFile);
					if (name.equals(processName)) {
						return byId(entry.th32ProcessID.intValue());
					}
				}
			} finally {
				Kernel32.CloseHandle(snapshot);
			}
		} else if (Platform.isMac() || Platform.isLinux()) {
			return byId(Utils.exec("bash", "-c", "ps -A | grep -m1 \"" + name + "\" | awk '{print $1}'"));
		} else {
			throw new UnsupportedOperationException("Unknown operating system! (" + System.getProperty("os.name") + ")");
		}
		throw new IllegalStateException("Process '" + name + "' was not found. Are you sure its running?");
	}
	
	public static Process byId(int id) {
		if ((Platform.isMac() || Platform.isLinux()) && !isSudo()) {
			throw new RuntimeException("You need to run as root/sudo for unix/osx based environments.");
		}
		
		if (Platform.isWindows()) {
			return new Win32Process(id, Kernel32.OpenProcess(0x438, true, id));
		} else if (Platform.isLinux()) {
			return new UnixProcess(id);
		} else if (Platform.isMac()) {
			IntByReference out = new IntByReference();
			if (mac.task_for_pid(mac.mach_task_self(), id, out) != 0) {
				throw new IllegalStateException("Failed to find mach task port for process, ensure you are running as sudo.");
			}
			return new MacProcess(id, out.getValue());
		}
		throw new IllegalStateException("Process " + id + " was not found. Are you sure its running?");
	}
	
	private static boolean isSudo() {
		return libc.getuid() == 0;
	}
	
}
