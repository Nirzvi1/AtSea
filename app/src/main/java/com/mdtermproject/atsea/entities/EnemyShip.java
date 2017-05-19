package com.mdtermproject.atsea.entities;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.graphics.Graphics;

/**
 * Created by Alec Krawciw on 2017-05-18.
 */

public class EnemyShip extends Ship {

    public EnemyShip(){
        super();

        this.imgId = Graphics.ENEMY_ID;

        setTranslate(Game.getMap().getSpawn());
        translate(-500, 500);
    }
}
