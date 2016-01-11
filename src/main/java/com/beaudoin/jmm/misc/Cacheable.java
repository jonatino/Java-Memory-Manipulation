package com.beaudoin.jmm.misc;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class Cacheable {

	private static final Map<Integer, Memory> bufferCache = new HashMap<>();
	private static final Function<Integer, Memory> cachedFunction = Memory::new;
	private static final Pointer cachedPointer = new Pointer(0);

	public static Memory buffer(int size) {
		return bufferCache.computeIfAbsent(size, cachedFunction);
	}

	public static Pointer pointer(long address) {
		Pointer.nativeValue(cachedPointer, address);
		return cachedPointer;
	}

}
