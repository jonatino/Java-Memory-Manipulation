package com.beaudoin.jmm;

import com.beaudoin.jmm.process.NativeProcess;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

	public static void main(String[] args) {
		long address = 0x7ffbcc429664L;

        int processid = 52018;

        NativeProcess process = NativeProcess.byId(processid);

		System.out.println(process.readInt(address));
		System.out.println(process.writeInt(address, 100));
		System.out.println(process.readInt(address));

        System.out.println(process.writeInt(address, 1000));
        System.out.println(process.readInt(address));
	}

}
