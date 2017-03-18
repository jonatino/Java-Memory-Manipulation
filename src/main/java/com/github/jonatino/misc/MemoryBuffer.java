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

package com.github.jonatino.misc;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Created by Jonathan on 1/10/2016.
 */
public final class MemoryBuffer extends Pointer {

	private int size;

	public MemoryBuffer(int size) {
		super(Native.malloc(size));
		this.size = size;
	}

	public MemoryBuffer writeBoolean(boolean value) {
		setByte(0, (byte) (value ? 1 : 0));
		return this;
	}
	
	public MemoryBuffer writeByte(int value) {
		setByte(0, (byte) value);
		return this;
	}
	
	public MemoryBuffer writeShort(int value) {
		setShort(0, (short) value);
		return this;
	}
	
	public MemoryBuffer writeInt(int value) {
		setInt(0, value);
		return this;
	}
	
	public MemoryBuffer writeLong(long value) {
		setLong(0, value);
		return this;
	}
	
	public MemoryBuffer writeFloat(float value) {
		setFloat(0, value);
		return this;
	}
	
	public MemoryBuffer writeDouble(double value) {
		setDouble(0, value);
		return this;
	}

	public void get(byte[] dest) {
		read(0, dest, 0, dest.length);
	}

	public boolean getBoolean() {
		return getByte() == 1;
	}

	public int getByte() {
		return getByte(0);
	}

	public int getShort() {
		return getShort(0);
	}

	public int getInt() {
		return getInt(0);
	}

	public long getLong() {
		return getLong(0);
	}

	public float getFloat() {
		return getFloat(0);
	}

	public double getDouble() {
		return getDouble(0);
	}

	public int size() {
		return size;
	}

	public byte[] getArray() {
		byte[] data = Cacheable.array(size);
		get(data);
		return data;
	}

}
