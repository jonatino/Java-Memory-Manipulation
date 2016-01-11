package com.beaudoin.jmm.misc;

/**
 * Created by Jonathan on 12/21/2015.
 */
public final class Strings {

	public static String transform(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == 0) {
				bytes[i] = 32;
			}
		}
		return new String(bytes).split(" ")[0].trim();
	}

	public static String hex(int value) {
		return "0x" + Integer.toHexString(value).toUpperCase();
	}

}
