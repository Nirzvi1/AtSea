package com.mdtermproject.atsea.utils;

import android.graphics.Matrix;

/**
 * Created by FIXIT on 2017-05-08.
 */

public class TransformationMatrix extends Matrix {

    private float w = 0;
    private float h = 0;

    public void setDimensions(float w, float h) {
        this.w = w;
        this.h = h;

    }//setDimensions

    public void rotate(float turn) {
        postRotate(turn, getW() / 2, getH() / 2);
    }

    public void translate(float dx, float dy) {
        postTranslate(dx, dy);
    }

    //angle in degrees: -180° ≤ angle ≤ 180°
    public float getAngle() {
        float[] values = new float[9];
        getValues(values);

        return (float) Math.toDegrees(Math.atan2(values[3], values[0]));
    }

    public float getX() {
        float[] values = new float[9];
        getValues(values);

        return values[2];
    }//getX

    public float getY() {
        float[] values = new float[9];
        getValues(values);

        return values[5];
    }//getY

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public TransformationMatrix clone() {
        float[] values = new float[9];
        getValues(values);

        TransformationMatrix result = new TransformationMatrix();
        result.setValues(values);

        return result;
    }

    public TransformationMatrix getTranslation() {
        float[] values = getValues();

        TransformationMatrix result = new TransformationMatrix();
        result.setDimensions(getW(), getH());

        values[0] = 0;
        values[1] = 0;
        values[3] = 0;
        values[4] = 0;
        values[6] = 0;
        values[7] = 0;

        result.setValues(values);

        return result;
    }

    public TransformationMatrix getRotation() {
        float[] values = getValues();

        TransformationMatrix result = new TransformationMatrix();
        result.setDimensions(getW(), getH());

        values[2] = 0;
        values[5] = 0;
        values[8] = 1;

        result.setValues(values);

        return result;
    }

    public TransformationMatrix subtract(TransformationMatrix other) {
        float x = getX() - other.getX();
        float y = getY() - other.getY();

        TransformationMatrix result = new TransformationMatrix();
        result.rotate(getAngle());
        result.translate(x, y);

        return result;
    }

    public float[] getValues() {
        float[] values = new float[9];
        getValues(values);

        return values;
    }

    public String toString() {
        return "Angle: " + getAngle() + ", X: " + getX() + ", Y: " + getY();
    }

}
