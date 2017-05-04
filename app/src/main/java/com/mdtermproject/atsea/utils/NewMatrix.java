package com.mdtermproject.atsea.utils;

import android.graphics.Matrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class NewMatrix extends Matrix {

    private float angle;
    private float x;
    private float y;

    public NewMatrix() {
        angle = 0;
        x = 0;
        y = 0;
    }

    public boolean rotate(float turn, float pX, float pY) {
        angle -= turn;

        return super.postRotate(turn, pX, pY);
    }//rotate


    public boolean translate(float dx, float dy) {
        x += dx;
        y += dy;

        return super.postTranslate(dx, dy);
    }//translate

    @Override
    public void reset() {
        angle = 0;
        x = 0;
        y = 0;
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

    public String toString() {
        return "Angle: " + angle + ", X: " + x + ", Y: " + y;
    }

}
