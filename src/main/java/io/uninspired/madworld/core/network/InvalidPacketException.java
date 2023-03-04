/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core.network;

/**
 *
 * @author rulyone
 */
public class InvalidPacketException extends RuntimeException {

    public InvalidPacketException() {
        super();
    }
    
    public InvalidPacketException(String msg) {
        super(msg);
    }
}
