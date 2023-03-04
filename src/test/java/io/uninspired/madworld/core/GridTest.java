/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author rulyone
 */
@TestInstance(Lifecycle.PER_METHOD)
public class GridTest {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    Grid grid;
    private BasicEntity entity1;
    private BasicEntity entity2;
    
    private static final short MAX_HEALTH = 100;
    
    private static final short ENTITY_1_INITIAL_HEALTH = 100;
    private static final int ENTITY_1_INITIAL_ATTACK_STRENGTH = 50;
    private static final float ENTITY_1_INITIAL_X = 1f;
    private static final float ENTITY_1_INITIAL_Y = 1f;
    
    private static final short ENTITY_2_INITIAL_HEALTH = 50;
    private static final int ENTITY_2_INITIAL_ATTACK_STRENGTH = 50;
    private static final float ENTITY_2_INITIAL_X = 5f;
    private static final float ENTITY_2_INITIAL_Y = 5f;
    
    @BeforeEach
    public void before() {
        grid = new Grid();
        Long id = 1l;
        entity1 = new Player("faker", id++, grid, ENTITY_1_INITIAL_X, ENTITY_1_INITIAL_Y, MAX_HEALTH, ENTITY_1_INITIAL_HEALTH, ENTITY_1_INITIAL_ATTACK_STRENGTH);
        entity2 = new Player("mata", id++, grid, ENTITY_2_INITIAL_X, ENTITY_2_INITIAL_Y, MAX_HEALTH, ENTITY_2_INITIAL_HEALTH, ENTITY_2_INITIAL_ATTACK_STRENGTH);
        
        grid.add(entity1);
        grid.add(entity2);
    }
    
    @AfterEach
    public void after() {
    }
    
    @Test
    public void testMeleeAttack() {

        assertThat(entity1.grid, notNullValue());
        assertThat(entity2.grid, notNullValue());

        logger.info(entity1.toString());
        logger.info(entity2.toString());
        
        assertThat(entity1.x, not(entity2.x));
        assertThat(entity1.y, not(entity2.y));
        
        grid.handleMelee(); //they're not on distance to attack each other, so current healths should be the same.
        
        assertThat(entity1.currentHealth, is(ENTITY_1_INITIAL_HEALTH));
        assertThat(entity2.currentHealth, is(ENTITY_2_INITIAL_HEALTH));
        
        entity1.teleport(entity2.x, entity2.y); //now they're on attack distance.
        
        assertThat(entity1.x, is(entity2.x));
        assertThat(entity1.y, is(entity2.y));
                
        grid.handleMelee(); //entity 2 should have died (entity1AttackStrength is equals to entity2CurrentHealth).
        
        assertThat(entity1.grid, notNullValue());
        assertThat(entity2.grid, nullValue());
        
    }
    
    @Test
    public void testMoveOnGrid() {
        //this is the initial grid cells (x,y)
        //0,0  0,1  0,2  0,3  0,4  0,5  0,6  0,7
        //1,0  1,1  1,2  1,3  1,4  1,5  1,6  1,7
        //2,0  2,1  2,2  2,3  2,4  2,5  2,6  2,7
        //3,0  3,1  3,2  3,3  3,4  3,5  3,6  3,7
        //4,0  4,1  4,2  4,3  4,4  4,5  4,6  4,7
        //5,0  5,1  5,2  5,3  5,4  5,5  5,6  5,7
        //6,0  6,1  6,2  6,3  6,4  6,5  6,6  6,7
        //7,0  7,1  7,2  7,3  7,4  7,5  7,6  7,7
        
        //each cell size is 8x8, so if we move 9 steps to the right (only x) the entity should have changed to the cell at [1][0].
        
        //both entities are at 0,0
        
        assertThat(grid.getCells()[0][0], is(entity2));
        assertThat(grid.getCells()[0][0].next, is(entity1));
        
        entity1.teleport(5f, 5f); //still in same cell
        
        assertThat(grid.getCells()[0][0], is(entity2));
        assertThat(grid.getCells()[0][0].next, is(entity1));
        
        entity1.teleport(9f, 0f); //We moved to position 9,0 so entity1 should be in cell [1][0] by now, and entity2 should be the only one on cell [0][0]
        
        assertThat(grid.getCells()[0][0], is(entity2));
        assertThat(grid.getCells()[0][0].next, nullValue());
        assertThat(grid.getCells()[1][0], is(entity1));
        
        entity1.teleport(0, 0); //move back to initial cell [0][0], and now entity1 is in the front of the linked list.
        
        assertThat(grid.getCells()[0][0], is(entity1));
        assertThat(grid.getCells()[0][0].next, is(entity2));
        assertThat(grid.getCells()[1][0], nullValue());
        
        entity2.teleport(0, 9f); //We moved to position 0,9, so entity2 should be in cell [0][1] by now, and entity1 should be the only one on cell [0][0]
        
        assertThat(grid.getCells()[0][0], is(entity1));
        assertThat(grid.getCells()[0][0].next, nullValue());
        assertThat(grid.getCells()[0][1], is(entity2));
        
    }
    
}
