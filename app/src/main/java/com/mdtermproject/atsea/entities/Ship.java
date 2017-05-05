package com.mdtermproject.atsea.entities;

import android.util.Log;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.graphics.Graphics;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Ship extends Entity {

    public Ship() {
        super(Graphics.PLAYER_ID, 132, 226);
        internal.translate(200, 200);
        internal.postRotate(-90, internal.getX() + internal.getW() / 2, internal.getY() + internal.getW() / 2);
    }//Ship

    public void update(int latency) {
        double dx = v * 0.001 * Math.cos(Math.toRadians(internal.getAngle())) * latency;
        double dy = v * 0.001 * Math.sin(Math.toRadians(internal.getAngle())) * latency;
        Log.i("Info", internal.getAngle() + ", " + dx + ", " + dy + ", " + v);

        this.v = filterVelocity(this.targetV);

        if (v > 0) {
            float turn = internal.getAngle() - filterAngle(targetAngle);

            rotate(turn);

            if (Math.abs(turn) > 0) {
                Game.refreshForeground();
            }//if
        }//if

        if (true || Game.getMap().getTileType((int) (external.getX() + dx), (int) (external.getY() + dy)) == 0) {
            translate((float) dx, (float) dy);

            if (Math.hypot(dx, dy) > 0.01) {
                Game.refreshBackground();
            }//if
        }//if


    }//update

}
