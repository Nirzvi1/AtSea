package com.mdtermproject.atsea.entities;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.graphics.Graphics;

/**
 * Created by Alec Krawciw on 2017-05-18.
 */

public class PlayerShip extends Ship {

    public PlayerShip(){
        super();
        setTranslate(Game.getMap().getSpawn());
        this.imgId = Graphics.PLAYER_ID;
    }
}
