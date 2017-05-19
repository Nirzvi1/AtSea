package com.mdtermproject.atsea.entities;

import android.graphics.PointF;
import android.util.Log;

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

    private ShipBuild shipStats;

    public Ship() {

        rotate = new TransformationMatrix();
        rotate.setDimensions(Graphics.DRAW_PLAYER.getW(), Graphics.DRAW_PLAYER.getH());

        translate = new TransformationMatrix();
        translate.setDimensions(Graphics.DRAW_PLAYER.getW(), Graphics.DRAW_PLAYER.getH());

        this.imgId = Graphics.PLAYER_ID;

        shipStats = new ShipBuild(0.1f, 0.5f, 0.5f, 0, 0, "Brigantine");
    }//Ship

    public void setShipBuild(ShipBuild newShip) {
        this.shipStats = newShip;
    }

    public ShipBuild getShipBuild() {
        return shipStats;
    }

    public void setTargetMotion(float v, float angle) {

        targetV = shipStats.getMaxVel() * (v / 100);

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
        return v * shipStats.getAngleVel() * angleTurn;
    }

    public float dampenAccel(float v) {
        return this.v + shipStats.getAccel() * (v - this.v);
    }

    public TransformationMatrix getRotate() {
        return rotate;
    }

    public TransformationMatrix getTranslate() {
        return translate;
    }//getTranslate

    public void rotate(float angle) {
        rotate.rotate(angle);
    }//rotate

    public void translate(float dx, float dy) {
        translate.translate(dx, dy);
    }//translate

    public void setTranslate(float x, float y) {
        translate.setTranslate(x, y);
    }

    public void setTranslate(PointF move) {
        translate.setTranslate(move.x, move.y);
    }

    public void attemptRotate(float turn) {
        rotate.rotate(turn);

        if (Game.getMap().doesCollide(getCorners()) != -1) {
            rotate.rotate(-turn);
        }//if
    }

    public void attemptTranslate(float dx, float dy) {
        translate.translate(dx, dy);

        if (Game.getMap().doesCollide(getCorners()) != -1) {
            translate.translate(-dx, -dy);
        }//if
    }

    public void update(int latency) {

        this.v = dampenAccel(this.targetV);

        if (Math.round(100 * v) > 0) {

            float turn = dampenAngularVelocity(filterAngle(360 - targetAngle) - rotate.getAngle());

//            attemptRotate(turn);
            rotate(turn);

            float dx = (float) (v * Math.cos(Math.toRadians(rotate.getAngle())) * latency);
            float dy = (float) (v * Math.sin(Math.toRadians(rotate.getAngle())) * latency);

            if (Math.abs(turn) > 0) {
                Game.refreshForeground();
            }//if

//            attemptTranslate(dx, 0);
//            attemptTranslate(0, dy);

            translate(dx, dy);
            if (Game.getMap().collide(this)) {
                translate(-dx, -dy);
            }

            if (Math.hypot(dx, dy) > 0.01) {
                Game.refreshBackground();
                Game.refreshGUI(); //update mini map
            }//if

            String region = Game.getMap().inWhatRegion(getCentre());

            if (region != null) {
                Log.i("REGION", region);
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

    public PointF getCentre(){
        float centreX = translate.getX() + translate.getW() / 2;
        float centreY = translate.getY() + translate.getH() / 2;
        return new PointF(centreX, centreY);
    }

}
