/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.uninspired.madworld.core.GameMap;

@Component
public class GameMapLauncher {

    private Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private GameMap gameMap;

    public GameMapLauncher() {
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("APPLICATION STARTED...");
        Executors.newSingleThreadExecutor().submit(gameMap);
    }
    
}
