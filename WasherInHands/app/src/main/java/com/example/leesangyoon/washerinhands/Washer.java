package com.example.leesangyoon.washerinhands;

import android.util.Log;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class Washer {

    WasherRoom washerRoom;
    String module;
    int runTime;
    boolean isTrouble;
    double x;
    double y;

    private Washer() {}

    private static class Singleton {
        private static final Washer washer = new Washer();
    }

    public static Washer getInstance () {
        Log.e("development","create singleton instance : Washer");
        return Singleton.washer;
    }

    public WasherRoom getWasherRoom() {
        return washerRoom;
    }

    public String getModule() {
        return module;
    }

    public int getRunTime() {
        return runTime;
    }

    public boolean isTrouble() {
        return isTrouble;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setWasherRoom(WasherRoom washerRoom) {
        this.washerRoom = washerRoom;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setTrouble(boolean trouble) {
        isTrouble = trouble;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
