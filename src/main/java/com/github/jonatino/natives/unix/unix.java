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

package com.github.jonatino.natives.unix;

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
		public Integer iov_len;

		@Override
		protected List<String> getFieldOrder() {
			return createFieldsOrder("iov_base", "iov_len");
		}

	}

}