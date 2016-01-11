package com.beaudoin.jmm.natives.unix;

import com.sun.jna.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jonathan on 06/01/16.
 */
public final class uio {

	static {
		Native.register(NativeLibrary.getInstance("c"));
	}

	public static native long process_vm_readv(int pid, iovec local, long liovcnt, iovec remote, long riovcnt, long flags) throws LastErrorException;

	public static native long process_vm_writev(int pid, iovec local, long liovcnt, iovec remote, long riovcnt, long flags) throws LastErrorException;

	public static class iovec extends Structure {

		public Pointer iov_base;
		public int iov_len;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("iov_base", "iov_len");
		}

	}

}