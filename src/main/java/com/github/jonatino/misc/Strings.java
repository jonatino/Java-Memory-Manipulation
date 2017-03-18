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

package com.github.jonatino.misc;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import net.openhft.hashing.LongHashFunction;

/**
 * Created by Jonathan on 12/21/2015.
 */
public final class Strings {

    private static Long2ObjectArrayMap<String> stringCache = new Long2ObjectArrayMap<>(16_982);

    public static String transform(byte[] bytes) {
        long hash = LongHashFunction.xx().hashBytes(bytes);
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
