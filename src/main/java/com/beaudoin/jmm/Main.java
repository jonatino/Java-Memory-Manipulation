package com.beaudoin.jmm;

import com.beaudoin.jmm.process.NativeProcess;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

	public static void main(String[] args) {
		NativeProcess process = NativeProcess.byName("csgo.exe");

		while (true) {
			//process.readInt(176881852);
		}
	}

}
