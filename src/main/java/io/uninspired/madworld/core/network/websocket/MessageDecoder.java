/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core.network.websocket;

import java.awt.Point;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;

import io.uninspired.madworld.core.GameCommand;
import io.uninspired.madworld.core.commands.JoinCommand;
import io.uninspired.madworld.core.commands.TeleportCommand;
import io.uninspired.madworld.core.network.protocol.PGamePacket;
import io.uninspired.madworld.core.network.protocol.PJoin;
import io.uninspired.madworld.core.network.protocol.PTeleport;
import io.uninspired.madworld.core.network.protocol.PacketType;


public class MessageDecoder {
    
    private Logger log = LoggerFactory.getLogger(getClass());

    protected GameCommand decode(BinaryMessage binaryMessage) throws Exception {
        ByteBuffer data = binaryMessage.getPayload();
        PGamePacket gamePacket = PGamePacket.getRootAsPGamePacket(data);
        GameCommand gameCommand = decodePacket(gamePacket.type(), gamePacket);
        return gameCommand;
    }

    private GameCommand decodePacket(int packetType, PGamePacket gamePacket) {
        log.debug("Received packetType " + packetType);
        if (packetType == PacketType.MOVE) {
            log.debug("Not yet implemented move packet.");
            return null;
        } else if(packetType == PacketType.JOIN) {
            return decodeJoinPacket(gamePacket.pJoin());
        } else if(packetType == PacketType.TELEPORT) {
            return decodeTeleportPacket(gamePacket.pTeleport());
        } else{
            log.warn("Unknown packet");
            return null;
        }
    }
    
    private TeleportCommand decodeTeleportPacket(PTeleport pTeleport) {
        //timestamp - playerId - payload
        long timestamp = System.currentTimeMillis();
        return new TeleportCommand(timestamp, pTeleport.playerId(), pTeleport.to());
    }
    
    private JoinCommand decodeJoinPacket(PJoin pJoin) {
        long timestamp = System.currentTimeMillis();
        return new JoinCommand(timestamp, pJoin.playerId(), pJoin.joinMessage());
    }
}
