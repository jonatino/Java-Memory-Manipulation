/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jonathan Beaudoin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.beaudoin.jmm.misc;

import net.openhft.hashing.LongHashFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 12/21/2015.
 */
public final class Strings {

    private static Map<Long, String> stringCache = new HashMap<>(16_982);

    public static String transform(byte[] bytes) {
        long hash = LongHashFunction.xx_r39().hashBytes(bytes);
        if (stringCache.containsKey(hash)) {
            return stringCache.get(hash);
        }
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0) {
                bytes[i] = 32;
            }
        }
        String string = new String(bytes).split(" ")[0].trim().intern();
        stringCache.put(hash, string);
        return string;
    }

    public static String hex(int value) {
        return "0x" + Integer.toHexString(value).toUpperCase();
    }

}
