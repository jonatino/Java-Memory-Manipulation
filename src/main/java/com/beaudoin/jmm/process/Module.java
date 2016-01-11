package com.beaudoin.jmm.process;


import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.Pointer;

public final class Module {

	private final NativeProcess process;
	private final String name;
	private final long address;
	private final int size;
	private final Pointer pointer;
	private MemoryBuffer data;

	public Module(NativeProcess process, String name, Pointer pointer, int size) {
		this.process = process;
		this.name = name;
		this.address = Pointer.nativeValue(pointer);
		this.size = size;
		this.pointer = pointer;
	}

	public NativeProcess process() {
		return process;
	}

	public Pointer pointer() {
		return pointer;
	}

	public String name() {
		return name;
	}

	public int size() {
		return size;
	}

	public long address() {
		return address;
	}

	public MemoryBuffer data() {
		return data(false);
	}

	public MemoryBuffer data(boolean forceNew) {
		if (forceNew || data == null) {
			data = process().read(pointer(), size());
		}
		return data;
	}

}
