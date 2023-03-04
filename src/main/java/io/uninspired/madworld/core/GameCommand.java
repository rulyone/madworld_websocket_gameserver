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
 * @param <P> type of payload used by this command.
 */
public interface GameCommand<P> {
    
    public P getPayload();
    public void setPayload(P payload);
    public long getPlayerId();
    public void setPlayerId(long playerId);
    public long getTimestamp();
    public void setTimestamp(long timestamp);
    
}
