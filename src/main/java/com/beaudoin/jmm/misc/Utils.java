package com.beaudoin.jmm.misc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Jonathan on 1/11/16.
 */
public final class Utils {

	public static int exec(String... command) {
		try {
			return Integer.parseInt(new Scanner(Runtime.getRuntime().exec(command).getInputStream()).next());
		} catch (IOException e) {
			throw new RuntimeException("Failed to read output from " + Arrays.toString(command));
		}
	}

}
