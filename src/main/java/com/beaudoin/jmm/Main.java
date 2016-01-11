package com.beaudoin.jmm;

import com.beaudoin.jmm.process.NativeProcess;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

	public static void main(String[] args) {
		NativeProcess process = NativeProcess.byId(57480);

		System.out.println(process.readInt(0x7f0364049664L));
	}

}
