package com.beaudoin.jmm.natives.mac;

import com.beaudoin.jmm.misc.MemoryBuffer;
import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * Created by Jonathan on 1/11/16.
 */
public final class mac {

    static {
        Native.register(NativeLibrary.getInstance("c"));
    }

    public static native int task_for_pid(int taskid, int pid, IntByReference out);

    public static native int getpid();

    public static native int mach_task_self();

    public static native int vm_write(int taskId, Pointer address, MemoryBuffer buffer, int size);

    public static native int vm_read(int taskId, Pointer address, int size, MemoryBuffer buffer, IntByReference ref);

    public static native String mach_error_string(int result) throws LastErrorException;

}