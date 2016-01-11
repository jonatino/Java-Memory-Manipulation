package com.beaudoin.jmm.process;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.misc.Strings;
import com.beaudoin.jmm.natives.windows.Kernel32;
import com.beaudoin.jmm.process.impl.LinuxProcess;
import com.beaudoin.jmm.process.impl.WindowsProcess;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;

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
		} else if (Platform.isMac()) {
			throw new UnsupportedOperationException("Unknown mac system! (" + System.getProperty("os.name") + ")");
			//MAC
		} else if (Platform.isLinux()) {
			throw new UnsupportedOperationException("Unknown linux system! (" + System.getProperty("os.name") + ")");
			//Linux
		} else {
			throw new UnsupportedOperationException("Unknown operating system! (" + System.getProperty("os.name") + ")");
		}
		throw new IllegalStateException("Process " + name + " was not found. Are you sure its running?");
	}

	static NativeProcess byId(int id) {
		if (Platform.isWindows()) {
			return new WindowsProcess(id, Kernel32.OpenProcess(0x438, true, id));
		} else if (Platform.isMac()) {
			throw new UnsupportedOperationException("Unknown mac system! (" + System.getProperty("os.name") + ")");
			//MAC
		} else if (Platform.isLinux()) {
			return new LinuxProcess(id, null);
		} else {
			throw new IllegalStateException("Process " + id + " was not found. Are you sure its running?");
		}
	}

	int id();

	Pointer pointer();

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
