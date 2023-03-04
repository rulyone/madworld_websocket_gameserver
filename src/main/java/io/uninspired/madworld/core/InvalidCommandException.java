/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core;

/**
 *
 * @author rulyone
 */
public class InvalidCommandException extends Exception {

    public InvalidCommandException() {
    }

    public InvalidCommandException(String message) {
        super(message);
    }
    
}
