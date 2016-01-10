package com.beaudoin.jmm.process;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.natives.windows.Kernel32;
import com.beaudoin.jmm.process.impl.WindowsProcess;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;

import java.nio.ByteBuffer;
import java.util.Locale;

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
						int pid = entry.th32ProcessID.intValue();
						return new WindowsProcess(pid, Kernel32.OpenProcess(0x438, true, pid));
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

	int id();

	Pointer pointer();

	Module findModule(String moduleName);

	ByteBuffer read(Pointer address, int bytesToRead);

	NativeProcess write(Pointer address, ByteBuffer buffer);

	boolean canRead(Pointer address, int bytesToRead);

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

	default NativeProcess writeBoolean(long address, boolean value) {
		return write(Cacheable.pointer(address), buffer(1).put((byte) (value ? 1 : 0)));
	}
	
	default NativeProcess writeByte(long address, int value) {
		return write(Cacheable.pointer(address), buffer(1).put((byte) value));
	}
	
	default NativeProcess writeShort(long address, int value) {
		return write(Cacheable.pointer(address), buffer(2).putShort((short) value));
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

	default boolean canRead(long address, int bytesToRead) {
		return canRead(Cacheable.pointer(address), bytesToRead);
	}
}
