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

package com.github.jonatino.natives.mac;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Created by Jonathan on 1/11/16.
 */
public final class mac {

    static {
        Native.register(NativeLibrary.getInstance("c"));
    }

    public static native int task_for_pid(int taskid, int pid, IntByReference out);

    public static native int mach_task_self();

    public static native int vm_write(int taskId, Pointer address, Pointer buffer, int size);

    public static native int vm_read(int taskId, Pointer address, int size, Pointer buffer, IntByReference ref);

    public static native int vm_read(int taskId, Pointer address, int size, PointerByReference buffer, IntByReference ref);

    public static native String mach_error_string(int result) throws LastErrorException;

}