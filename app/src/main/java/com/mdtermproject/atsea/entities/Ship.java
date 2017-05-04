package com.mdtermproject.atsea.entities;

import com.mdtermproject.atsea.graphics.GraphicsBase;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Ship extends Entity {

    public Ship() {
        super(GraphicsBase.PLAYER_ID, 66, 113);
        rotate.translate(200, 200);
        rotate.postRotate(-90, rotate.getX() + w / 2, rotate.getY() + h / 2);
    }//Ship

    public void update(int latency) {
        double dx = v * 0.001 * Math.cos(Math.toRadians(rotate.getAngle())) * latency;
        double dy = v * 0.001 * Math.sin(Math.toRadians(rotate.getAngle())) * latency;

        translate((float) -dx, (float) -dy);

    }//update

}
