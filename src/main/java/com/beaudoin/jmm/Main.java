package com.beaudoin.jmm;

import com.beaudoin.jmm.process.NativeProcess;

import java.io.IOException;

/**
 * Created by Jonathan on 12/22/2015.
 */
public final class Main {

    public static void main(String[] args) throws IOException {
        System.out.println(NativeProcess.byName("csgo_linux").id());
    }

}
