package com.beaudoin.jmm.process;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.misc.Strings;
import com.sun.jna.Pointer;

import static com.beaudoin.jmm.misc.Cacheable.buffer;

/**
 * Created by Jonathan on 3/24/2016.
 */
public interface DataSource {

	MemoryBuffer read(Pointer address, int size);

	Process write(Pointer address, MemoryBuffer buffer);

	boolean canRead(Pointer address, int size);

	default boolean readBoolean(long address) {
		return read(address, 1).getBoolean();
	}

	default int readByte(long address) {
		return read(address, 1).getByte();
	}

	default int readShort(long address) {
		return read(address, 2).getShort();
	}

	default int readInt(long address) {
		return read(address, 4).getInt();
	}

	default long readUnsignedInt(long address) {
		return Integer.toUnsignedLong(read(address, 4).getInt());
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
		byte[] bytes = Cacheable.array(length);
		read(address, bytes.length).get(bytes);
		return Strings.transform(bytes);
	}

	default MemoryBuffer read(long address, int size) {
		return read(Cacheable.pointer(address), size);
	}

	default Process writeBoolean(long address, boolean value) {
		return write(Cacheable.pointer(address), buffer(1).putBoolean(value));
	}

	default Process writeByte(long address, int value) {
		return write(Cacheable.pointer(address), buffer(1).putByte(value));
	}

	default Process writeShort(long address, int value) {
		return write(Cacheable.pointer(address), buffer(2).putShort(value));
	}

	default Process writeInt(long address, int value) {
		return write(Cacheable.pointer(address), buffer(4).putInt(value));
	}

	default Process writeLong(long address, long value) {
		return write(Cacheable.pointer(address), buffer(8).putLong(value));
	}

	default Process writeFloat(long address, float value) {
		return write(Cacheable.pointer(address), buffer(4).putFloat(value));
	}

	default Process writeDouble(long address, double value) {
		return write(Cacheable.pointer(address), buffer(8).putDouble(value));
	}

	default boolean canRead(long address, int size) {
		return canRead(Cacheable.pointer(address), size);
	}

}
