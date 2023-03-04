/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import io.uninspired.madworld.core.network.websocket.GameMapWebSocketHandler;;


@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private GameMapWebSocketHandler gameMapSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //ToDo: check allowed origins.
        registry.addHandler(gameMapSocketHandler, "/gp").setAllowedOrigins("*");
        
    }

}
