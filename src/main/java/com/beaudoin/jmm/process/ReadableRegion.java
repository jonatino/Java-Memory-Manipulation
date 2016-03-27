package com.beaudoin.jmm.process;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.misc.Strings;
import com.sun.jna.Pointer;

import static com.beaudoin.jmm.misc.Cacheable.buffer;

/**
 * Created by Jonathan on 3/24/2016.
 */
public interface ReadableRegion {

    MemoryBuffer read(Pointer address, int size);

    NativeProcess write(Pointer address, MemoryBuffer buffer);

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
