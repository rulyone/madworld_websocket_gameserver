/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vladmihalcea.hibernate.type.array.LongArrayType;

/**
 * Spatial partition based on http://gameprogrammingpatterns.com/spatial-partition.html#when-to-use-it
 * 
 * @author rulyone
 */
@Entity
@TypeDefs({
    @TypeDef(name = "long-array", typeClass = LongArrayType.class)
})

public class Grid {
    
    private static final Logger log = LoggerFactory.getLogger(Grid.class);
    
    @Id
    private Long id;

    private static final int NUM_CELLS = 8;
    private static final int CELL_SIZE = 8;
    private static final int ATTACK_DISTANCE = 1;
    
    @Type(type = "long-array")
    @Column(
        name = "cells_grid" , 
        columnDefinition = "bigint[][]")
    private BasicEntity[][] cells = new BasicEntity[NUM_CELLS][NUM_CELLS];
    
    public Grid() {
    }
    
    public void teleport(BasicEntity entity, float x, float y) {
        // See which cell it was in.
        int oldCellX = (int) (entity.x / CELL_SIZE);
        int oldCellY = (int) (entity.y / CELL_SIZE);
        
        // See which cell it's moving to.
        int cellX = (int) (x / CELL_SIZE);
        int cellY = (int) (y / CELL_SIZE);
        
        entity.x = x;
        entity.y = y;
        
        // If it didn't change cells, we're done.
        if (oldCellX == cellX && oldCellY == cellY)
            return;

        // Unlink it from the list of its old cell.
        if (entity.previous != null) {
            entity.previous.next = entity.next;
        }

        if (entity.next != null) {
            entity.next.previous = entity.previous;
        }

        // If it's the head of a list, remove it.
        if (cells[oldCellX][oldCellY] == entity) {
            cells[oldCellX][oldCellY] = entity.next;
        }

        // Add it back to the grid at its new cell.
        add(entity);
        
    }

    public void add(BasicEntity entity) {
        // Determine which grid cell it's in.
        int cellX = (int) (entity.x / CELL_SIZE);
        int cellY = (int) (entity.y / CELL_SIZE);
        // Add to the front of list for the cell it's in.
        entity.previous = null;
        entity.next = cells[cellX][cellY];
        cells[cellX][cellY] = entity;
        
        if (entity.next != null) {
            entity.next.previous = entity;
        }
        entity.setGrid(this);
    }
    
    public void remove(BasicEntity entity) {
        //Determine which grid cell it's in
        int cellX = (int) (entity.x / CELL_SIZE);
        int cellY = (int) (entity.y / CELL_SIZE);
        // Unlink it from the list of its old cell
        if (entity.previous != null) {
            entity.previous.next = entity.next;
        }
        if (entity.next != null) {
            entity.next.previous = entity.previous;
        }
        //If it's the head of a list, remove it.
        if (cells[cellX][cellY] == entity) {
            cells[cellX][cellY] = entity.next;
        }
        entity.setGrid(null);
    }
    
    public void handleMelee() {
        for (int x = 0; x < NUM_CELLS; x++) {
            for (int y = 0; y < NUM_CELLS; y++) {
                handleCell(x, y);
            }
        }
    }
    
    private void handleCell(int x, int y) {        
        BasicEntity entity = cells[x][y];
        while (entity != null) {
            // Handle other units in this cell.
            handleUnit(entity, entity.next);
            
            // Also try the neighboring cells.
            if (x > 0 && y > 0) {
                handleUnit(entity, cells[x - 1][y - 1]);
            }
            if (x > 0) {
                handleUnit(entity, cells[x - 1][y]);
            }
            if (y > 0) {
                handleUnit(entity, cells[x][y - 1]);
            }  
            if (x > 0 && y < NUM_CELLS - 1) {
                handleUnit(entity, cells[x - 1][y + 1]);
            }

            entity = entity.next;
        }
    }
    
    private void handleUnit(BasicEntity entity, BasicEntity other) {
        while (other != null) {
            if (distance(entity, other) < ATTACK_DISTANCE) {
                handleAttack(entity, other);
            }
            other = other.next;
        }
    
    }
    
    private int distance(BasicEntity entity, BasicEntity other) {
        return (int) Math.hypot(entity.x - other.x, entity.y - other.y);
    }
    
    private void handleAttack(BasicEntity entity, BasicEntity other) {
        //for now we print current health  and attack strenght of each entity
        log.debug("Entity " +entity.id + " hp-attack: " + entity.currentHealth + "-" + entity.attackStrength + ", Entity " + other.id + " hp-attack: " + other.currentHealth + "-" + other.attackStrength);
        entity.currentHealth -= other.attackStrength;
        other.currentHealth -= entity.attackStrength;
        log.debug("Entity " + entity.id + " received " + other.attackStrength + " damage from Entity " + other.id);
        log.debug("Entity " + other.id + " received " + entity.attackStrength + " damage from Entity " + entity.id);
        if (entity.currentHealth <= 0) {
            //DIED
            log.debug("Entity " + entity.id + " just died.");
            //remove from grid
            remove(entity);
        }
        if (other.currentHealth <= 0) {
            //DIED
            log.debug("Entity " + other.id + " just died.");
            //remove from grid
            remove(other);
        }
    }
    
    //GETTERS & SETTERS
    public BasicEntity[][] getCells() {
        return cells;
    }

    public void setCells(BasicEntity[][] cells) {
        this.cells = cells;
    }
    
}
