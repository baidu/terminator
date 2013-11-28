/**
 * CipherUtil.java
 *
 * Copyright 2012 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.baidu.terminator.plugin.signer.customized.app;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2012-4-9$
 * 
 */
public abstract class CipherUtil {
    private static final int OUTPUT_SIZE = 8 * 1024;
    
    public static byte[] process(Cipher cipher, int blockSize, byte[] input) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        if (input.length <= blockSize) {
            return cipher.doFinal(input);
        }
        byte[] output = new byte[OUTPUT_SIZE];
        int outputSize = 0;
        for (int i = 0; ;i += blockSize) {
            if (i + blockSize < input.length)
                outputSize += cipher.doFinal(input, i, blockSize, output, outputSize);
            else {
                outputSize += cipher.doFinal(input, i, input.length - i, output, outputSize);
                break;
            }
        }
        return ArrayUtils.subarray(output, 0, outputSize);
    }

}
