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

package com.beaudoin.jmm.misc;

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

	public MemoryBuffer putBoolean(boolean value) {
		setByte(0, (byte) (value ? 1 : 0));
		return this;
	}
	
	public MemoryBuffer putByte(int value) {
		setByte(0, (byte) value);
		return this;
	}
	
	public MemoryBuffer putShort(int value) {
		setShort(0, (short) value);
		return this;
	}
	
	public MemoryBuffer putInt(int value) {
		setInt(0, value);
		return this;
	}
	
	public MemoryBuffer putLong(long value) {
		setLong(0, value);
		return this;
	}
	
	public MemoryBuffer putFloat(float value) {
		setFloat(0, value);
		return this;
	}
	
	public MemoryBuffer putDouble(double value) {
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

	public byte[] array() {
		byte[] data = Cacheable.array(size);
		get(data);
		return data;
	}

}
