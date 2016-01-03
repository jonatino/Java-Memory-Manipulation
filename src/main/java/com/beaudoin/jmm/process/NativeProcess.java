package com.beaudoin.jmm.process;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.natives.windows.Kernel32;
import com.beaudoin.jmm.process.impl.WindowsProcess;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Created by Jonathan on 12/12/15.
 */
public interface NativeProcess {

	static NativeProcess byName(String name) {
		String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if (os.contains("win")) {
			Tlhelp32.PROCESSENTRY32.ByReference entry = new Tlhelp32.PROCESSENTRY32.ByReference();
			Pointer snapshot = Kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL, 0);
			try {
				while (Kernel32.Process32Next(snapshot, entry)) {
					String processName = Native.toString(entry.szExeFile);
					if (name.equals(processName)) {
						int pid = entry.th32ProcessID.intValue();
						return new WindowsProcess(pid, Kernel32.OpenProcess(0x438, true, pid));
					}
				}
			} finally {
				Kernel32.CloseHandle(snapshot);
			}
		} else if ((os.contains("mac")) || (os.contains("darwin"))) {
			//MAC
		} else if (os.contains("nux")) {
			//Linux
		} else {
			throw new UnsupportedOperationException("Unknown operating system! (" + os + ")");
		}
		throw new IllegalStateException("Process " + name + " was not found. Are you sure its running?");
	}

	int id();

	Pointer pointer();

	Module findModule(String moduleName);

	default byte readByte(long address) {
		return read(address, 1).get();
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
		return new String(bytes);
	}

	default ByteBuffer read(long address, int bytesToRead) {
		return read(Cacheable.pointer(address), bytesToRead);
	}

	default boolean canRead(long address, int bytesToRead) {
		return canRead(Cacheable.pointer(address), bytesToRead);
	}

	ByteBuffer read(Pointer address, int bytesToRead);

	boolean canRead(Pointer address, int bytesToRead);

}
