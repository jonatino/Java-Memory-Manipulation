package com.beaudoin.jmm;

import com.beaudoin.jmm.natives.linux.ptrace;
import com.sun.jna.LastErrorException;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

    private static final int PTRACE_PEEKDATA = 2;
    private static final int PTRACE_ATTACH = 16;
    private static final int PTRACE_DETACH = 17;

    public static void main(String[] args) {
        int pid = 1291;

        try {
            ptrace.ptrace(PTRACE_ATTACH, pid, null, null);
            Memory buffer = new Memory(4);
            ptrace.ptrace(PTRACE_PEEKDATA, pid, new Pointer(0x7f22465700L), buffer);
            System.out.println(buffer.getInt(0));
        } catch (LastErrorException ex) {
            ex.printStackTrace();
        } finally {
            ptrace.ptrace(PTRACE_DETACH, pid, null, null);
        }
    }

}
