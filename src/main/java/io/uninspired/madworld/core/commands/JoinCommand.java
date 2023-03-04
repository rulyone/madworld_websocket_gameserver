/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core.commands;

import io.uninspired.madworld.core.GameCommand;
import lombok.ToString;

/**
 * ToDo: add to lib so that we can use it in clients, and add some magic number to make sure we process only game packets.
 * @author rulyone
 */
@ToString
public class JoinCommand implements GameCommand<String> {
    
    private long playerId;
    private String joinMessage;
    private long timestamp;

    public JoinCommand(long timestamp, long playerId, String joinMessage) {
        this.timestamp = timestamp;
        this.playerId = playerId;
        this.joinMessage = joinMessage;
    }
    
    @Override
    public String getPayload() {
        return joinMessage;
    }

    @Override
    public void setPayload(String payload) {
        this.joinMessage = payload;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }


    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
}
