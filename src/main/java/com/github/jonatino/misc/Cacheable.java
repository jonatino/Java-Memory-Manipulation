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

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class Cacheable {

	private static final Map<Integer, MemoryBuffer> bufferCache = new HashMap<>();
	private static final Function<Integer, MemoryBuffer> bufferCreate = MemoryBuffer::new;

	private static final Map<Integer, byte[]> arrayCache = new HashMap<>();
	private static final Function<Integer, byte[]> arrayCreate = byte[]::new;

	private static final Pointer cachedPointer = new Pointer(0);
	public static final IntByReference INT_BY_REF = new IntByReference();
	public static final WinDef.DWORD DWORD_ZERO = new WinDef.DWORD();

	public static MemoryBuffer buffer(int size) {
		return bufferCache.computeIfAbsent(size, bufferCreate);
	}

	public static byte[] array(int size) {
		return arrayCache.computeIfAbsent(size, arrayCreate);
	}

	public static Pointer pointer(long address) {
		Pointer.nativeValue(cachedPointer, address);
		return cachedPointer;
	}

}
