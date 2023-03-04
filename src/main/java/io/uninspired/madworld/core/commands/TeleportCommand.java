/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core.commands;

import io.uninspired.madworld.core.GameCommand;
import io.uninspired.madworld.core.network.protocol.Vec2;
import lombok.ToString;

/**
 *
 * @author rulyone
 */
@ToString
public class TeleportCommand implements GameCommand<Vec2>{
    
    private long playerId;
    private Vec2 teleportTo;
    private long timestamp;
    
    public TeleportCommand(long timestamp, long playerId, Vec2 teleportTo) {
        this.teleportTo = teleportTo;
        this.playerId = playerId;
        this.timestamp = timestamp;
    }

    @Override
    public Vec2 getPayload() {
        return teleportTo;
    }

    @Override
    public void setPayload(Vec2 payload) {
        this.teleportTo = payload;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    
}
