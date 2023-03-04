/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core.network.websocket;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.*;

import com.google.flatbuffers.FlatBufferBuilder;

import io.uninspired.madworld.core.GameMap;
import io.uninspired.madworld.core.GameMapStateListener;
import io.uninspired.madworld.core.Player;
import io.uninspired.madworld.core.network.protocol.PEntity;
import io.uninspired.madworld.core.network.protocol.PGamePacket;
import io.uninspired.madworld.core.network.protocol.PGrid;
import io.uninspired.madworld.core.network.protocol.PacketType;
import io.uninspired.madworld.core.network.protocol.Vec2;

@Component
public class GameMapWebSocketHandler extends BinaryWebSocketHandler implements GameMapStateListener{
    
    Logger logger = LoggerFactory.getLogger(getClass());

    private MessageDecoder messageDecoder = new MessageDecoder();

    Map<String, WebSocketSession> activeSessionMap = new HashMap<>();
    Map<String, WebSocketSession> joinedSessionMap = new HashMap<>();

    private GameMap gameMap;

    @Autowired
    public GameMapWebSocketHandler(GameMap gameMap) {
        this.gameMap = gameMap;
        this.gameMap.addListener(this);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        activeSessionMap.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        activeSessionMap.remove(session.getId());

    }

    

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) throws Exception  {
        super.handleBinaryMessage(session, binaryMessage);
        
        try {
            var gameCommand = messageDecoder.decode(binaryMessage);
            logger.debug("binary message received, queueing for command execution.");
            gameMap.queueCommandForExecution(gameCommand);
            logger.info(gameCommand.toString());
        } catch (Exception e) {
            logger.warn("Exception thrown", e);
            e.printStackTrace();
        }
        
    }

    @Override
    public void stateChanged(List<Player> players) {
        //generate data to be sent to each client
        logger.debug("calling stateChanged from listener...");
        byte[] payload = generatePGamePacketGrid(players);
        logger.debug("payload: " + new String(payload, StandardCharsets.UTF_8));
        //send it to each client :D
        this.activeSessionMap
            .values()
            .parallelStream()
            .forEach(session -> {
                try {
                    session.sendMessage(new BinaryMessage(payload));
                } catch (IOException e) {
                    logger.warn("Network issues when sending message to client", e);
                }
            });

        logger.debug("exiting stateChanged from listener...");
    }

    private byte[] generatePGamePacketGrid(List<Player> players) {
        logger.debug("starting to generate pGamePacketGrid for " + players.size() + " players");
        int size = players.size();
        FlatBufferBuilder builder = new FlatBufferBuilder(2048);
        
        int[] entitiesOffsets = new int[size];
        logger.debug("starting vector of entitites");
        IntStream.range(0, size)
                .parallel() //DESCOMENTAR LUEGO DE VER SI FUNCIONA PRIMERO...
                .forEach(idx -> {
                    
                    Player p = players.get(idx);
                    logger.debug("starting pentity index: " + idx + ", playerid: " + p.getId());
                    int name = builder.createString(p.getNickname());
                    PEntity.startPEntity(builder);
                    PEntity.addHp(builder, p.getCurrentHealth());
                    PEntity.addMana(builder, p.getMaxHealth());
                    PEntity.addName(builder, name);
                    PEntity.addPos(builder, Vec2.createVec2(builder, p.getX(), p.getY()));
                    int entity = PEntity.endPEntity(builder);
                    entitiesOffsets[idx] = entity;
                });
        int entitiesVectorOffset = PGrid.createEntitiesVector(builder, entitiesOffsets);
        logger.debug("finished vector of entities, offset: "+ entitiesVectorOffset);
        int gridOffset = PGrid.createPGrid(builder, entitiesVectorOffset);
        PGamePacket.startPGamePacket(builder);
        PGamePacket.addType(builder, PacketType.GRID);
        PGamePacket.addPGrid(builder, gridOffset);
        int gamePacketOffset = PGamePacket.endPGamePacket(builder);
        
        builder.finish(gamePacketOffset);
        logger.debug("finished creating pgamepacket. Returning...");
        return builder.sizedByteArray();
    }
    
}
