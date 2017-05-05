package com.mdtermproject.atsea.entities;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.graphics.Graphics;
import com.mdtermproject.atsea.utils.NewMatrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public abstract class Entity {

    protected int imgId = 0;

    protected volatile float targetV = 0;
    protected volatile float v = 0;

    protected volatile float targetAngle = 0;

    protected NewMatrix internal;
    protected NewMatrix external;

    protected double accel = 0.5;
    protected double angleVel = 0.3;

    public Entity(int imgId, float w, float h) {
        internal = new NewMatrix();
        internal.setDimensions(w, h);

        external = new NewMatrix();

        this.imgId = imgId;
    }

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


        if (internal.getAngle() - angle > 180) {
            angle += 360;
        } else if (internal.getAngle() - angle < -180) {
            angle -= 360;
        }//elseif

        targetV = v;
        targetAngle = angle;

        if (targetV > 0) {
            float turn = internal.getAngle() - filterAngle(angle);

            rotate(turn);

            if (Math.abs(turn) > 0) {
                Game.refreshForeground();
            }//if
        }//if
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

}
