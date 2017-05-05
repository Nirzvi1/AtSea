package com.mdtermproject.atsea.utils;

import android.graphics.Matrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class NewMatrix extends Matrix {

    private float angle;
    private float x;
    private float y;
    private float w;
    private float h;

    public NewMatrix() {
        angle = 0;
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }

    public boolean rotate(float turn, float pX, float pY) {
        angle -= turn;

        angle = (angle % 360 + 360) % 360;

        return super.postRotate(turn, pX, pY);
    }//rotate

    public boolean translate(float dx, float dy) {
        x += dx;
        y += dy;

        return super.postTranslate(dx, dy);
    }//translate

    public void setDimensions(float w, float h) {
        this.w = w;
        this.h = h;
    }//setDimensions

    public boolean scale(float scale, float pX, float pY) {
        w *= scale;
        h *= scale;

        return super.postScale(scale, scale, pX, pY);
    }

    public boolean scale(float xScale, float yScale, float pX, float pY) {
        w *= xScale;
        h *= yScale;

        return super.postScale(xScale, yScale, pX, pY);
    }

    @Override
    public void reset() {
        angle = 0;
        x = 0;
        y = 0;
        w = 0;
        h = 0;

        super.reset();
    }

    public float getAngle() {
        return angle;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public String toString() {
        return "Angle: " + angle + ", X: " + x + ", Y: " + y + ", W: " + w + ", H: " + h;
    }

}
