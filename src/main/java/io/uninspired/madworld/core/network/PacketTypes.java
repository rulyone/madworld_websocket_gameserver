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
public enum PacketTypes {
    MOVE_PACKET((short)1),
    JOIN_PACKET((short)2),
    TELEPORT_PACKET((short)3);

    
    private short packetType;
    
    PacketTypes(short packetType) {
        this.packetType = packetType;
    }
    
    public short getPacketType() {
        return this.packetType;
    }
}
