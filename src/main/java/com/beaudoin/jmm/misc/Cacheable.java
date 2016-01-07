package com.beaudoin.jmm.misc;

import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;

public final class Cacheable {

	private static final Map<Integer, ByteBuffer> bufferCache = new HashMap<>();
	private static final Function<Integer, ByteBuffer> cachedFunction = k -> allocateDirect(k).order(nativeOrder());
	private static final Pointer cachedPointer = new Pointer(0);

	public static ByteBuffer buffer(int size) {
		return (ByteBuffer) bufferCache.computeIfAbsent(size, cachedFunction).clear();
	}

	public static Pointer pointer(long address) {
		Pointer.nativeValue(cachedPointer, address);
		return cachedPointer;
	}

}
