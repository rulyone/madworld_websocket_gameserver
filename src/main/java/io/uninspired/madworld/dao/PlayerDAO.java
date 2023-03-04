/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import io.uninspired.madworld.core.Player;;

public interface PlayerDAO extends JpaRepository<Player, Long> {
    
    // @SqlUpdate("create table if not exists player (id BIGINT AUTO_INCREMENT PRIMARY KEY, nickname VARCHAR_IGNORECASE, x float4, y float4, max_health INT, current_health INT, attack_strength INT)")
    // void createPlayerTable();
    
    // @SqlUpdate("insert into player (nickname, x, y, max_health, current_health, attack_strength) VALUES (:nickname, :x, :y, :max_health, :current_health, :attack_strength)")
    // void createPlayer(@Bind("nickname") String nickname, @Bind("x") float x, @Bind("y") float y, @Bind("max_health") int maxHealth, @Bind("current_health") int currentHealth, @Bind("attack_strength") int attackStrength);
    
    // @SqlQuery("select id, nickname, x, y, max_health, current_health, attack_strength FROM player")
    // @RegisterRowMapper(PlayerMapper.class)
    // List<Player> findAllPlayers();
    
    // @SqlQuery("select id, nickname, x, y, max_health, current_health, attack_strength FROM player WHERE id = :id")
    // @RegisterRowMapper(PlayerMapper.class)
    // Player findPlayerById(@Bind("id") long id);

}
