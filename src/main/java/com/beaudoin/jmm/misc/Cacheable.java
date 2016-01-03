package com.beaudoin.jmm.misc;

import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Cacheable {
	
	private static final int ALLOCATION_SIZE = 8_192_000;
	private static final Pointer cachedPointer = new Pointer(0);
	private static ByteBuffer cachedBuffer = null;
	
	private static ByteBuffer allocate(int size) {
		if (size >= ALLOCATION_SIZE) {
			return ByteBuffer.allocateDirect(size);
		}
		if (cachedBuffer == null || size > cachedBuffer.remaining()) {
			cachedBuffer = ByteBuffer.allocateDirect(ALLOCATION_SIZE);
		}
		cachedBuffer.limit(cachedBuffer.position() + size);
		ByteBuffer result = cachedBuffer.slice();
		cachedBuffer.position(cachedBuffer.limit());
		cachedBuffer.limit(cachedBuffer.capacity());
		return result;
	}
	
	public static ByteBuffer buffer(int size) {
		return allocate(size).order(ByteOrder.nativeOrder());
	}
	
	public static Pointer pointer(long address) {
		Pointer.nativeValue(cachedPointer, address);
		return cachedPointer;
	}

}
