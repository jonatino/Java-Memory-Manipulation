package com.beaudoin.jmm.misc;

import com.sun.jna.Pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class Cacheable {

	private static final Map<Integer, MemoryBuffer> bufferCache = new HashMap<>();
	private static final Function<Integer, MemoryBuffer> cachedFunction = MemoryBuffer::new;
	private static final Pointer cachedPointer = new Pointer(0);

	public static MemoryBuffer buffer(int size) {
		return bufferCache.computeIfAbsent(size, cachedFunction);
	}

	public static Pointer pointer(long address) {
		Pointer.nativeValue(cachedPointer, address);
		return cachedPointer;
	}

}
