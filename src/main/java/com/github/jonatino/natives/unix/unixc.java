package com.github.jonatino.natives.unix;

import java.io.IOException;

import com.github.jonatino.misc.NativeUtils;
import com.sun.jna.*;

public final class unixc {

	static {		   
		try {
			String className = unixc.class.getName().replace('.', '/');
			String classJar = unixc.class.getResource("/" + className + ".class").toString();
			if (classJar.startsWith("jar:") && classJar.contains("!")) {
				// Running from a JAR - extracting libnative_mem.so
				NativeUtils.loadLibraryFromJar("/libnative_mem.so");
			} else {
				// Loading directly from java.library.path
				System.loadLibrary("native_mem");
			}
	    } catch (UnsatisfiedLinkError | IOException e) {
	      System.err.println("Native library (native_mem - libnative_mem.so) failed to load!\n");
	      e.printStackTrace();
	      System.exit(1);
	    }
	}

	public static native long mem_read(int pid, long localaddr, long remoteaddr, int size) throws LastErrorException;

	public static native long mem_write(int pid, long localaddr, long remoteaddr, int size) throws LastErrorException;

	public static native long mem_read(int pid, long[] localaddr, long[] remoteaddr, int[] size) throws LastErrorException;

	public static native long mem_write(int pid, long[] localaddr, long[] remoteaddr, int[] size) throws LastErrorException;

}