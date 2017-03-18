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

import com.github.jonatino.misc.Cacheable;
import com.github.jonatino.misc.MemoryBuffer;
import com.github.jonatino.misc.Strings;
import com.sun.jna.Pointer;

/**
 * Created by Jonathan on 3/24/2016.
 */
public interface DataSource {
	
	MemoryBuffer read(Pointer address, int size, MemoryBuffer toBuffer);
	
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
		return read(Cacheable.pointer(address), size, Cacheable.buffer(size));
	}
	
	default MemoryBuffer read(long address, int size, MemoryBuffer toBuffer) {
		return read(Cacheable.pointer(address), size, toBuffer);
	}
	
	default Process writeBoolean(long address, boolean value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(1).writeBoolean(value));
	}
	
	default Process writeByte(long address, int value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(1).writeByte(value));
	}
	
	default Process writeShort(long address, int value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(2).writeShort(value));
	}
	
	default Process writeInt(long address, int value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(4).writeInt(value));
	}
	
	default Process writeLong(long address, long value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(8).writeLong(value));
	}
	
	default Process writeFloat(long address, float value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(4).writeFloat(value));
	}
	
	default Process writeDouble(long address, double value) {
		return write(Cacheable.pointer(address), Cacheable.buffer(8).writeDouble(value));
	}
	
	default boolean canRead(long address, int size) {
		return canRead(Cacheable.pointer(address), size);
	}
	
}
