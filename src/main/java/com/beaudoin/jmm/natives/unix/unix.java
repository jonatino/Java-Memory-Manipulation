/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jonathan Beaudoin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.beaudoin.jmm.natives.unix;

import com.sun.jna.*;

import java.util.List;

/**
 * Created by jonathan on 06/01/16.
 */
public final class unix {

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
			return createFieldsOrder("iov_base", "iov_len");
		}

	}

}