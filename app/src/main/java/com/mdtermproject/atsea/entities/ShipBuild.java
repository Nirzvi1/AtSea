package com.mdtermproject.atsea.entities;

/**
 * Created by FIXIT on 2017-05-09.
 */

public class ShipBuild {

    private float accel;
    private float maxVel;
    private float angleVel;

    private float numCannons;
    private float storage;

    private String modelName;

    public enum Model {
        RAFT,
        DINGHY,
        ROWBOAT,
        SLOOP,
        SAILBOAT,
        CORVETTE,
        CARAVEL,
        CARRACK,
        BARQUE,
        BRIGANTINE,
        BARQUENTINE,
        ARGOSY
    }

    public ShipBuild(float accel, float maxVel, float angleVel, float numCannons, float storage, String modelName) {
        this.accel = accel;
        this.maxVel = maxVel;
        this.angleVel = angleVel;
        this.numCannons = numCannons;
        this.storage = storage;
        this.modelName = this.modelName;
    }//ShipBuild

    public void setTo(ShipBuild copy) {
        this.accel = copy.accel;
        this.maxVel = copy.maxVel;
        this.angleVel = copy.angleVel;
        this.numCannons = copy.numCannons;
        this.storage = copy.storage;
    }//setTo

    public float getAccel() {
        return accel;
    }

    public void setAccel(float accel) {
        this.accel = accel;
    }

    public float getMaxVel() {
        return maxVel;
    }

    public void setMaxVel(float maxVel) {
        this.maxVel = maxVel;
    }

    public float getAngleVel() {
        return angleVel;
    }

    public void setAngleVel(float angleVel) {
        this.angleVel = angleVel;
    }

    public float getNumCannons() {
        return numCannons;
    }

    public void setNumCannons(float numCannons) {
        this.numCannons = numCannons;
    }

    public float getStorage() {
        return storage;
    }

    public void setStorage(float storage) {
        this.storage = storage;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
