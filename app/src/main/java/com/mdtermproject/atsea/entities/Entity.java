package com.mdtermproject.atsea.entities;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.mdtermproject.atsea.graphics.GraphicsBase;
import com.mdtermproject.atsea.utils.NewMatrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public abstract class Entity {

    protected int imgId = 0;

    protected volatile float w = 0;
    protected volatile float h = 0;

    protected volatile float v = 0;

    protected NewMatrix rotate;
    protected NewMatrix translate;

    protected double accel = 0.8;
    protected double angleVel = 0.6;

    public Entity(int imgId, int w, int h) {
        rotate = new NewMatrix();
        translate = new NewMatrix();

        this.imgId = imgId;
        this.w = w;
        this.h = h;
    }

    public void setPosition(float x, float y) {
        translate.setTranslate(x, -y);
    }//setPosition

    public void setMotion(float v, float angle) {
        this.v = v;

        if (v > 0) {
            rotate(rotate.getAngle() - angle);
        }//if
    }//setMotion

    public void attemptSetMotion(float v, float angle) {

        if (rotate.getAngle() > 180 + angle) {
            angle += 360;
        } else if (rotate.getAngle() < angle - 180) {
            angle -= 360;
        }//elseif

        if (v > 0) {
            setMotion(filterVelocity(v), filterAngle(angle));
        } else {
            setMotion(filterVelocity(v), rotate.getAngle());
        }//else
    }

    public float filterAngle(float angle) {
        return (float) (rotate.getAngle() + v / 100 * angleVel * Math.signum(angle - rotate.getAngle()) * Math.pow(Math.abs(angle - rotate.getAngle()), 0.5));
    }

    public float filterVelocity(float v) {
        return (float) Math.round(5 * (this.v + accel * (v - this.v))) / 5;
    }

    public Bitmap getImage() {
        return GraphicsBase.getImageFromId(imgId);
    }//Bitmap

    public Matrix getFullMatrix() {
        Matrix result = new Matrix();
        result.setRotate(rotate.getAngle(), translate.getX() + w / 2, translate.getY() + h / 2);
        return rotate;
    }

    public NewMatrix getRotation() {
        return rotate;
    }

    public NewMatrix getTranslation() {
        return translate;
    }

    public void rotate(float angdeg) {
        rotate.rotate(angdeg, rotate.getX() + w / 2, rotate.getY() + h / 2);
    }

    public void translate(float x, float y) {
       translate.translate(x, -y);
    }

}
