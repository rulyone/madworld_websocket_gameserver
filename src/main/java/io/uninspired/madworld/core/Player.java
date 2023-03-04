/**
 * Copyright (c) 2023, Pablo Martinez
 * All rights reserved.
 * 
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.  
 */

package io.uninspired.madworld.core;

import javax.persistence.Entity;

/**
 *
 * @author rulyone
 */
@Entity
public class Player extends BasicEntity {
    
    private String nickname;
    
    private String session;

    public Player(String nickname, Long id, Grid grid, float x, float y, short maxHealth, short currentHealth, int attackStrength) {
        super(id, grid, x, y, maxHealth, currentHealth, attackStrength);
    }
    
    public Player(Long id, String nickname, float x, float y, short maxHealth, short currentHealth, int attackStrength) {
        super(id, null, x, y, maxHealth, currentHealth, attackStrength);
        this.nickname = nickname;
    }
    
    public Player() {
        super();
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSession() {
        return session;
    }
    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Player [nickname=" + nickname + ", session=" + session + "]";
    }
}
