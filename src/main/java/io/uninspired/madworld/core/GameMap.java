/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.uninspired.madworld.core.commands.JoinCommand;
import io.uninspired.madworld.core.commands.TeleportCommand;
import io.uninspired.madworld.core.network.protocol.Vec2;
import io.uninspired.madworld.dao.PlayerDAO;

/**
 *
 * @author rulyone
 */
@Component
public class GameMap implements Callable<Object>  {
    
    private Logger log = LoggerFactory.getLogger(GameMap.class);
    
    //the grid of the game in 2D.
    private Grid grid;
    //id - player (key - value)
    private Map<Long, Player> players;
    
    //Queue for receiving commands from clients.
    private ArrayBlockingQueue<GameCommand> commandsQueue;
    //Array list with commands currently in queue for processing.
    List<GameCommand> currentCommands;
    //listeners to notify of game state change.
    private List<GameMapStateListener> listeners = new ArrayList<>();
    
    private boolean running = false;
    private boolean gridStateChanged = false;
    
    private PlayerDAO playerDAO;
        
    public GameMap(PlayerDAO playerDAO) {
        grid = new Grid();
        //considering that this gamecommands will be processed @ 60FPS, we'll hardly have more than 100 commands in this queue. make it configurable later.
        commandsQueue = new ArrayBlockingQueue<>(100);
        currentCommands = new ArrayList<>(100);
        this.players = new HashMap<>();
        //ToDo: config FPS
        this.playerDAO = playerDAO;
    }

    public void addListener(GameMapStateListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public Object call() throws Exception {
        this.startGameLoop();
        return null;
    }
    
    public void stopGame() {
        //ToDo: do some cleanup
        this.running = false;
    }
    
    private void startGameLoop() {
        this.running = true;
        double ns = 1000000000.0 / 60.0;
        double delta = 0;
        
        long lastTime = System.nanoTime();
//        long timer = System.currentTimeMillis();
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            while (delta >= 1) {
                updateGameState();
                broadcastGameState();
                delta--;
            }
        }
    }
    
    private void updateGameState() {
        //first we process all gamecommands. ToDo: only process commands near the current game tick. 
        //log.debug("updateGameState, commandsQueue.size: " + commandsQueue.size());
        commandsQueue.drainTo(currentCommands);
        for (GameCommand command : currentCommands) {
            try {
                interpretCommand(command);
                log.debug("command interpreted");
            } catch (InvalidCommandException ex) {
                log.error("Error executing gamecommand.", ex);
            }
        }
        //now we handle melee attacks.... ToDo: add attack delay, for now it will be 1 attack per frame
        grid.handleMelee();
        currentCommands.clear();
        //log.debug("exiting updateGameState");
    }
    
    private void broadcastGameState() {
        //ToDo: broadcast current state to all players... or better yet, delta state.
        try {
            if(gridStateChanged) {
                gridStateChanged = false;
                log.debug("broadcasting...");
                List<Player> playersList = new ArrayList<Player>(this.players.values());
                this.listeners.stream().forEach(l -> {
                    l.stateChanged(playersList);
                });
            }
        } catch(Exception e) {
            log.debug("exception when broadcasting game state", e);
        }
        //log.debug("exiting broadCastGameState");
    }

    public void queueCommandForExecution(GameCommand command) {
        log.debug("queueCommandForExecution playerId:" + command.getPlayerId());
        this.commandsQueue.add(command);
        log.debug("commandsQueue.size: " + commandsQueue.size());
    }
    
    private void interpretCommand(GameCommand command) throws InvalidCommandException {
        if (command instanceof TeleportCommand) {
            TeleportCommand teleportCommand = (TeleportCommand)command;
            teleportPlayer(teleportCommand);
            this.gridStateChanged = true;
        }else if(command instanceof JoinCommand) {
            JoinCommand joinCommand = (JoinCommand) command;
            joinPlayer(joinCommand);          
            this.gridStateChanged = true;  
        }else{
            log.debug("unknown command...");
        }
    }
    
    private void teleportPlayer(TeleportCommand teleportCommand) {
        //ToDo: implement tick based server game, using player speed to move to the desired point.
        log.debug("teleportPlayer: " + teleportCommand.getPlayerId());
        try {
            Player player = players.get(teleportCommand.getPlayerId());
            Vec2 teleportTo = teleportCommand.getPayload();
            player.teleport(teleportTo.x(), teleportTo.y());
        } catch (Exception ex) {
            log.error("You were trying to teleport to an invalid location... cheaters never win in the long run.");
        }
    }
    
    private void joinPlayer(JoinCommand joinCommand) {
        Long playerId = joinCommand.getPlayerId();
        log.debug("joinCommand: " + playerId);
        try {
            Player testPlayer = new Player();
            testPlayer.setAttackStrength(100);
            testPlayer.setCurrentHealth((short)100);
            testPlayer.setGrid(this.grid);
            testPlayer.setId(1L);
            testPlayer.setMaxHealth((short)100);
            testPlayer.setNickname("eNoylur");
            testPlayer.setX(0f);
            testPlayer.setY(0f);
            
            Optional<Player> player = playerDAO.findById(playerId); //only place to call database... the other is when player disconnects. ToDo
            Player p = player.orElse(testPlayer);
            grid.add(p);
            players.put(p.getId(), p);
            log.debug("player joined");
        } catch (Exception ex) {
            log.error("Couldn't add player to the map.", ex);
        }
    }
}
