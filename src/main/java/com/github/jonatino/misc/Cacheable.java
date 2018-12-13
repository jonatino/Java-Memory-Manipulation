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
import com.sun.jna.ptr.IntByReference;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

import java.util.function.IntFunction;

public final class Cacheable {

	private static final Int2ObjectArrayMap<MemoryBuffer> bufferCache = new Int2ObjectArrayMap<>();
	private static final IntFunction<MemoryBuffer> bufferCreate = MemoryBuffer::new;

	private static final Int2ObjectArrayMap<byte[]> arrayCache = new Int2ObjectArrayMap<>();
	private static final IntFunction<byte[]> arrayCreate = byte[]::new;

	private static final Pointer cachedPointer = new Pointer(0);
	public static final IntByReference INT_BY_REF = new IntByReference();

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
