package com.example.leesangyoon.washerinhands;

/**
 * Created by Administrator on 2016-09-05.
 */
public class Machine {

    static public double alpha=0;
    static public double beta=0;
    private double centerX=0;
    private double centerY=0;
    private String module=null;
    private int size = 15;
    private boolean isTrouble;
    private boolean movingMode;
    private boolean isWorking;
    private int runtTime;
    static private int MACHINE_SIZE;
    static private int CANVAS_WIDTH;
    static private int CANVAS_HEIGHT;


    public Machine(){

    }

    public Machine(String sensorID, double centerX, double centerY, int runtTime, boolean isTrouble, boolean isWorking){

        this.module = sensorID;
        this.centerX = centerX;
        this.centerY = centerY;
        this.runtTime = runtTime;
        this.isTrouble = isTrouble;
        this.isWorking = isWorking;
        this.movingMode = false;
    }


    public Machine(String sensorID, double centerX, double centerY){

        this.module = sensorID;
        this.centerX = centerX;
        this.centerY = centerY;
        this.movingMode = false;
    }

    public void setMACHINE_SIZE(int size){
        this.MACHINE_SIZE = size;
    }

    public void setCenter(double x, double y){
        this.centerX = x;
        this.centerY = y;
        if(centerX>1000-MACHINE_SIZE){
            centerX=1000-MACHINE_SIZE;
        }else if(centerX<=-1000){
            centerX=-1000;
        }
        if(centerY>1000-MACHINE_SIZE){
            centerY=1000-MACHINE_SIZE;
        }else if(centerY<=-1000){
            centerY=-1000;
        }
    }

    public void setSensorID(String sensorID){
        this.module = sensorID;
    }

    public void setSize(int size){
        this.size = size;
    }

    public void setTrouble(boolean trouble){
        this.isTrouble = trouble;
    }

    public void setMovingMode(boolean movingMode){
        this.movingMode = movingMode;
    }

    public void setWorking(boolean isWorking){
        this.isWorking = isWorking;
    }

    public void initAlphaBeta(){
        this.alpha = 0;
        this.beta = 0;
    }

    public void addAlpha(double alpha){
        this.alpha += alpha;

        if(this.alpha<-1000+CANVAS_WIDTH) {
            this.alpha = -1000 + CANVAS_WIDTH;
        }else if(this.alpha>1000){
            this.alpha = 1000;
        }
    }

    public void addBeta(double beta){
        this.beta += beta;

        if(this.beta<-1000+CANVAS_HEIGHT) {
            this.beta = -1000+CANVAS_HEIGHT;
        } else if(this.beta>1000){
            this.beta=1000;
        }
    }



    public double getX(){
        return this.centerX;
    }

    public double getY(){
        return this.centerY;
    }

    public void addX(double x){
        this.centerX += x;

    }

    public void addY(double y){
        this.centerY += y;

    }

    public String getModule(){
        return this.module;
    }

    public int getSize(){
        return this.size;
    }

    public boolean getTrouble(){
        return this.isTrouble;
    }

    public boolean getMovingMode(){
        return this.movingMode;
    }

    public boolean getWorking(){
        return this.isWorking;
    }

    public int getRuntTime(){ return this.runtTime;}

    public double getAlpha(){
        return this.alpha;
    }

    public double getBeta(){
        return this.beta;
    }

    public void setCanvasSize(int width, int height){
        this.CANVAS_WIDTH=width;
        this.CANVAS_HEIGHT=height;
    }

}
