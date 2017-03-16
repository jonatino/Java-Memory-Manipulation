package com.github.jonatino.natives.unix;

import com.sun.jna.*;

public final class unixc {

	static {
		try {
	    	System.loadLibrary("native_mem");
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n");
	      e.printStackTrace();
	      System.exit(1);
	    }
	}

	public static native long mem_read(int pid, long localaddr, long remoteaddr, int size) throws LastErrorException;

	public static native long mem_write(int pid, long localaddr, long remoteaddr, int size) throws LastErrorException;

	public static native long mem_read(int pid, long[] localaddr, long[] remoteaddr, int[] size) throws LastErrorException;

	public static native long mem_write(int pid, long[] localaddr, long[] remoteaddr, int[] size) throws LastErrorException;

}