package com.example.leesangyoon.washerinhands;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class Washer {

    String module;
    int runTime;
    boolean isTrouble;
    double x;
    double y;

    public Washer(double x, double y, String module) {
        this.x = x;
        this.y = y;
        this.module = module;
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
