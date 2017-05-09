package com.mdtermproject.atsea.entities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.graphics.Graphics;
import com.mdtermproject.atsea.utils.NewMatrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Ship {

    private int imgId = 0;

    private volatile float targetV = 0;
    private volatile float v = 0;

    private volatile float targetAngle = 0;

    private NewMatrix internal;
    private NewMatrix external;

    private double accel = 0.3;
    private double angleVel = 0.3;

    public Ship() {
        internal = new NewMatrix();
        internal.setDimensions(88, 151);
        internal.translate(Graphics.DRAW_PLAYER.getX(), Graphics.DRAW_PLAYER.getY());
        internal.postRotate(-90, internal.getX() + internal.getW() / 2, internal.getY() + internal.getH() / 2);

        external = new NewMatrix();

        this.imgId = Graphics.PLAYER_ID;

    }//Ship

    public void setPosition(float x, float y) {
        external.setTranslate(x, -y);
    }//setPosition

    public void setMotion(float v, float angle) {

        this.v = v;

        if (v > 0) {
            if (internal.getAngle() > 180 + angle) {
                angle += 360;
            } else if (internal.getAngle() < angle - 180) {
                angle -= 360;
            }//elseif

            rotate(internal.getAngle() - angle);
        }//if
    }//setMotion

    public void slowlySetMotion(float v, float angle) {

        targetV = v;
        targetAngle = angle;
    }//void

    public float filterAngle(float angle) {
        return (float) (internal.getAngle() + v / 100 * angleVel * Math.signum(angle - internal.getAngle()) * Math.pow(Math.abs(angle - internal.getAngle()), 0.5));
    }

    public float filterVelocity(float v) {
        return (float) (this.v + accel * (v - this.v));
    }

    public Bitmap getImage() {
        return Graphics.getImageFromId(imgId);
    }//Bitmap

    public Matrix getFullMatrix() {
        Matrix result = new Matrix();
        result.setRotate(internal.getAngle(), external.getX() + internal.getW() / 2, external.getY() + internal.getH() / 2);
        return internal;
    }

    public NewMatrix getShipInternal() {
        return internal;
    }

    public NewMatrix getShipExternal() {
        return external;
    }

    public void rotate(float angdeg) {
        internal.rotate(angdeg, internal.getX() + internal.getW() / 2, internal.getY() + internal.getH() / 2);
    }

    public void translate(float x, float y) {
        external.translate(x, -y);
    }

    public void update(int latency) {
        double dx = v * 0.001 * Math.cos(Math.toRadians(internal.getAngle())) * latency;
        double dy = v * 0.001 * Math.sin(Math.toRadians(internal.getAngle())) * latency;

        this.v = filterVelocity(this.targetV);

        if (v > 0) {

            if (internal.getAngle() - targetAngle > 180) {
                targetAngle += 360;
            } else if (internal.getAngle() - targetAngle < -180) {
                targetAngle -= 360;
            }//elseif

//            Log.i("Info", internal.getAngle() + ", " + dx + ", " + dy + ", " + v + ", " + targetAngle);

            float turn = internal.getAngle() - filterAngle(targetAngle);

            rotate(turn);

            if (Math.abs(turn) > 0) {
                Game.refreshForeground();
            }//if

            translate((float) dx, (float) dy);

            if (Math.hypot(dx, dy) > 0.01) {
                Game.refreshBackground();
            }//if


        }//if

    }//update

}
