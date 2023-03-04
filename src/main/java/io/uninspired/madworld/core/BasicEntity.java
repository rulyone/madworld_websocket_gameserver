/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * Spatial Partition based on http://gameprogrammingpatterns.com/spatial-partition.html#when-to-use-it
 * 
 * @author rulyone
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Entity
public abstract class BasicEntity {
    @Id
    protected Long id;
    protected float x;
    protected float y;
    
    @Transient
    protected Grid grid;
    @Transient
    protected BasicEntity previous;
    @Transient
    protected BasicEntity next;

    protected short maxHealth;
    protected short currentHealth;
    
    protected int attackStrength;
    
    protected BasicEntity() {
    }
    
    public BasicEntity(Long id, Grid grid, float x, float y, short maxHealth, short currentHealth, int attackStrength) { 
        this.id = id;
        this.grid = grid;
        this.x = x;
        this.y = y;
        this.previous = null;
        this.next = null;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.attackStrength = attackStrength;
    }
        
    public void teleport(float x, float y) {
        this.grid.teleport(this, x, y);
    }
    
    //GETTERS & SETTERS
    public int getAttackStrength() {
        return attackStrength;
    }

    public void setAttackStrength(int attackStrength) {
        this.attackStrength = attackStrength;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public short getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(short maxHealth) {
        this.maxHealth = maxHealth;
    }

    public short getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(short currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Long getId() {
        return id;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public BasicEntity getPrevious() {
        return previous;
    }

    public void setPrevious(BasicEntity previous) {
        this.previous = previous;
    }

    public BasicEntity getNext() {
        return next;
    }

    public void setNext(BasicEntity next) {
        this.next = next;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BasicEntity [attackStrength=" + attackStrength + ", currentHealth=" + currentHealth + ", grid=" + grid
                + ", id=" + id + ", maxHealth=" + maxHealth + ", next=" + next + ", previous=" + previous + ", x=" + x
                + ", y=" + y + "]";
    }

}
