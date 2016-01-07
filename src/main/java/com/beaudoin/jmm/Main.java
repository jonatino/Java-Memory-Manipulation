package com.beaudoin.jmm;

import com.beaudoin.jmm.process.NativeProcess;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

	public static void main(String... args) {
		NativeProcess process = NativeProcess.byName("csgo.exe");
		for (long i = 0; i < 100_000_000_000L; i++) {
			process.readInt(477742236);
			process.writeInt(477742236, 2);
			process.readInt(477742236);
		}
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
