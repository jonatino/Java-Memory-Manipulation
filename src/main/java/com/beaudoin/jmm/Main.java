package com.beaudoin.jmm;

import com.beaudoin.jmm.misc.Cacheable;
import com.beaudoin.jmm.misc.MemoryBuffer;
import com.beaudoin.jmm.natives.mac.mac;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

	public static void main(String[] args) throws IOException {
		// System.out.println(NativeProcess.byName("clion").id());

		IntByReference out = new IntByReference();
		System.out.println(mac.getpid());
		System.out.println(mac.task_for_pid(mac.mach_task_self(), 3122, out) == 0);
		int taskId = out.getValue();


		IntByReference o = new IntByReference();
		MemoryBuffer buffer = Cacheable.buffer(4);

		System.out.println(mac.vm_read(taskId, new Pointer(0x7fff53601b58L), 4, buffer, o));
		System.out.println(o.getValue());
		System.out.println(buffer.getInt());
		System.out.println(Arrays.toString(buffer.array()));
	}

}
