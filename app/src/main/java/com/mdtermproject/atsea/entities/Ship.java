package com.mdtermproject.atsea.entities;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.graphics.Graphics;
import com.mdtermproject.atsea.utils.TransformationMatrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Ship {

    private int imgId = 0;

    private volatile float targetV = 0;
    private volatile float v = 0;

    private volatile float targetAngle = 0;

    private TransformationMatrix rotate;
    private TransformationMatrix translate;

    private float accel = 0.1f;
    private float angleVel = 0.5f;
    private float maxVel = 0.06f;

    public Ship() {

        rotate = new TransformationMatrix();
        rotate.setDimensions(Graphics.DRAW_PLAYER.getW(), Graphics.DRAW_PLAYER.getH());

        translate = new TransformationMatrix();
        translate.setDimensions(Graphics.DRAW_PLAYER.getW(), Graphics.DRAW_PLAYER.getH());
        translate.translate(Graphics.DRAW_PLAYER.getX(), Graphics.DRAW_PLAYER.getY());

        this.imgId = Graphics.PLAYER_ID;

    }//Ship

    public void setTargetMotion(float v, float angle) {

        targetV = maxVel * (v / 100);

        if (v == 0) {
            angle = 360 - rotate.getAngle();
        }//if

        targetAngle = angle;
    }//setTargetMotion

    public float filterAngle(float angle) {

        angle = (angle % 360 + 360) % 360;

        if (angle > 180) {
            angle -= 360;
        }//if

        if (Math.abs(rotate.getAngle() - angle) > 180) {
            angle += Math.signum(rotate.getAngle() - angle) * 360;
        }//if

        return angle;
    }//filterAngle

    public float dampenAngularVelocity(float angleTurn) {
        return v * angleVel * angleTurn;
    }

    public float dampenAccel(float v) {
        return this.v + accel * (v - this.v);
    }

    public TransformationMatrix getRotate() {
        return rotate;
    }

    public TransformationMatrix getTranslate() {
        return translate;
    }//getTranslate

    public void update(int latency) {

        this.v = dampenAccel(this.targetV);

        if (Math.round(100 * v) > -1) {

            float turn = dampenAngularVelocity(filterAngle(360 - targetAngle) - rotate.getAngle());

            rotate.rotate(turn);
            Game.refreshForeground();

            float dx = (float) (v * Math.cos(Math.toRadians(rotate.getAngle())) * latency);
            float dy = (float) (v * Math.sin(Math.toRadians(rotate.getAngle())) * latency);

            if (Math.abs(turn) > 0) {
                Game.refreshForeground();
            }//if

            translate.postTranslate(dx, dy);

            if (Game.getMap().doesCollide(getCorners()) != -1) {
                translate.postTranslate(-dx, -dy);
            } else if (Math.hypot(dx, dy) > 0.01) {
                Game.refreshBackground();
                Game.refreshMiniMap();
            }//if

        }//if


    }//update

    public double[][] getCorners() {
        double[][] corners = new double[4][2];

        float centreX = translate.getX() + translate.getW() / 2;
        float centreY = translate.getY() + translate.getH() / 2;

        double rad = Math.toRadians(rotate.getAngle());
        double halfW = translate.getW() / 2;
        double halfH = translate.getH() / 2;

        corners[0][0] = centreX + (-halfW)*Math.cos(rad) - (-halfH)*Math.sin(rad);
        corners[0][1] = centreY + (-halfW)*Math.sin(rad) + (-halfH)*Math.cos(rad);

        corners[1][0] = centreX + (halfW)*Math.cos(rad) - (-halfH)*Math.sin(rad);
        corners[1][1] = centreY + (halfW)*Math.sin(rad) + (-halfH)*Math.cos(rad);

        corners[2][0] = centreX + (halfW)*Math.cos(rad) - (halfH)*Math.sin(rad);
        corners[2][1] = centreY + (halfW)*Math.sin(rad) + (halfH)*Math.cos(rad);

        corners[3][0] = centreX + (-halfW)*Math.cos(rad) - (halfH)*Math.sin(rad);
        corners[3][1] = centreY + (-halfW)*Math.sin(rad) + (halfH)*Math.cos(rad);

        return corners;
    }

}
