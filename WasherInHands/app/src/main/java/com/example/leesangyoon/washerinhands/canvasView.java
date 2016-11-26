package com.example.leesangyoon.washerinhands;

/**
 * Created by Administrator on 2016-09-05.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class canvasView extends View {

    float sx,mx,ex;
    float sy,my,ey;
    boolean onTrash=false;

    List<Machine> machines = new ArrayList<Machine>();
    List<Bitmap> bitmaps = new ArrayList<Bitmap>();

    int MACHINE_SIZE;
    int REMOVE_SIZE;
    final static float CENTERX = 40;
    final static float CENTERY = 40;
    final static double removeLocationX=0.75;
    final static double removeLocationY=0.8;

    int canvasWidth=0;
    int canvasHeight=0;

    boolean movingMode=false;
    Machine settingMachine = new Machine();

    public canvasView(Context c){
        super(c);

        settingMachine.initAlphaBeta();
        invalidate();

    }

    public void onDraw(Canvas canvas){

        Bitmap bitmap_map = Bitmap.createBitmap(2000, 2400, Bitmap.Config.ARGB_8888);
        Canvas mapCanvas = new Canvas(bitmap_map);
        mapCanvas.translate(1000,1200);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        p.setColor(Color.RED);
        mapCanvas.drawRect(0-(int)settingMachine.getAlpha(),0-(int)settingMachine.getBeta(),canvas.getWidth()-(int)settingMachine.getAlpha(),canvas.getHeight()-(int)settingMachine.getBeta(),p);
        mapCanvas.drawColor(Color.argb(180,230,230,230));

        canvas.drawColor(Color.rgb(255,255,255));

        canvas.translate((float)settingMachine.getAlpha(),(float)settingMachine.getBeta());

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        settingMachine.setCanvasSize(canvasWidth,canvasHeight);

        MACHINE_SIZE = canvas.getWidth()/6;
        REMOVE_SIZE = canvas.getWidth()/4;

        if(MACHINE_SIZE > 200){
            MACHINE_SIZE=200;
        }
        if(REMOVE_SIZE > 250){
            REMOVE_SIZE=250;
        }

        settingMachine.setMACHINE_SIZE(MACHINE_SIZE);

        if(!machines.isEmpty()) {
            for (int i = 0; i < machines.size(); i++) {

                if(machines.get(i).getMovingMode()){
                    if(machines.get(i).getTrouble()){
                        Bitmap machineBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_broken_moving);;
                        bitmaps.add(machineBitmap);
                        canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                        mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                    }else if(machines.get(i).getWorking()){
                        Bitmap machineBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_working_moving);;
                        bitmaps.add(machineBitmap);
                        canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float) machines.get(i).getX(), (float)machines.get(i).getY(), null);
                        mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                    }else if(!machines.get(i).getWorking()){
                        Bitmap machineBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_notworking_moving);;
                        bitmaps.add(machineBitmap);
                        canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                        mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                    }
                }else if(!machines.get(i).getMovingMode()){
                    if(machines.get(i).getTrouble()){
                        Bitmap machineBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_broken);
                        bitmaps.add(machineBitmap);
                        canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                        mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                    }else if(machines.get(i).getWorking()){
                        Bitmap machineBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_working);
                        bitmaps.add(machineBitmap);
                        canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                        mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                    }else if(!machines.get(i).getWorking()){
                        Bitmap machineBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_notworking);
                        bitmaps.add(machineBitmap);
                        canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                        mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE+20, MACHINE_SIZE+20, false), (float)machines.get(i).getX(), (float)machines.get(i).getY(), null);
                    }
                }
            }
        }
        if(onTrash){
            Bitmap removeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.onremove);
            removeBitmap = removeBitmap.createScaledBitmap(removeBitmap, REMOVE_SIZE, REMOVE_SIZE, false);
            canvas.drawBitmap(removeBitmap, (float)(canvas.getWidth()*(removeLocationX))-(float)settingMachine.getAlpha(), (float)(canvas.getHeight()*(removeLocationY))-(float)settingMachine.getBeta(),null);
        }else{
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.remove);
            bitmap2 = bitmap2.createScaledBitmap(bitmap2, REMOVE_SIZE, REMOVE_SIZE, false);
            canvas.drawBitmap(bitmap2, (float)(canvas.getWidth()*(removeLocationX)-(float)settingMachine.getAlpha()), (float)(canvas.getHeight()*(removeLocationY)-(float)settingMachine.getBeta()),null);
        }

        bitmap_map = bitmap_map.createScaledBitmap(bitmap_map, canvas.getWidth()/5, canvas.getHeight()/5, false);
        canvas.drawBitmap(bitmap_map,canvas.getWidth()-bitmap_map.getWidth()-(int)settingMachine.getAlpha(),0-(int)settingMachine.getBeta(),null);


        bitmaps.clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            sx = event.getX();
            sy = event.getY();

            for(int i=machines.size()-1;i>=0;i--){
                if(sx - settingMachine.getAlpha()>machines.get(i).getX() && sx - settingMachine.getAlpha()<machines.get(i).getX()+MACHINE_SIZE
                        && sy - settingMachine.getBeta() >machines.get(i).getY() && sy - settingMachine.getBeta() < machines.get(i).getY()+MACHINE_SIZE){
                    machines.get(i).setMovingMode(true);
                    movingMode = true;
                    break;
                }
            }
            invalidate();
        } else if (action == MotionEvent.ACTION_MOVE) {
            mx = event.getX();
            my = event.getY();

            if(!movingMode){
                settingMachine.addAlpha(mx-sx);
                settingMachine.addBeta(my-sy);
                sx=mx;
                sy=my;
            }
            else{
                for(int i=0;i<machines.size();i++){
                    if(machines.get(i).getMovingMode()){
                        machines.get(i).setCenter(mx-MACHINE_SIZE/2 - settingMachine.getAlpha(),my-MACHINE_SIZE/2- settingMachine.getBeta());
                    }
                }
                if(mx-settingMachine.getAlpha()>canvasWidth*(removeLocationX)-settingMachine.getAlpha() && mx-settingMachine.getAlpha()<canvasWidth*(removeLocationX)+REMOVE_SIZE-settingMachine.getAlpha()&&
                        my-settingMachine.getBeta()>canvasHeight*(removeLocationY)-settingMachine.getBeta() && my-settingMachine.getBeta()<canvasHeight*(removeLocationY)+REMOVE_SIZE-settingMachine.getBeta()){
                    onTrash=true;
                }else{
                    onTrash=false;
                }
            }
            invalidate();
        } else if (action == MotionEvent.ACTION_UP) {
            ex=event.getX();
            ey=event.getY();


            if(ex>canvasWidth*(removeLocationX)&& ex<canvasWidth*(removeLocationX)+REMOVE_SIZE&&
                    ey>canvasHeight*(removeLocationY)&& ey<canvasHeight*(removeLocationY)+REMOVE_SIZE) {
                for(int i=0;i<machines.size();i++){
                    if(machines.get(i).getMovingMode()){
                        machines.remove(i);
                    }
                }
            }

            if(machines.size()!=0){
                for(int i=0;i<machines.size();i++){
                    machines.get(i).setMovingMode(false);
                }
            }

            //세탁기 지우고 size 0이면 alpha,beta 0으로 만들기

            movingMode=false;
            onTrash=false;
            invalidate();
        }

        return true;
    }

    public void addMachine(String sensorID){
        Machine machine = new Machine(sensorID,CENTERX-settingMachine.getAlpha(), CENTERY-settingMachine.getBeta(),0,false,false);
        machines.add(machine);

        invalidate();
    }
/*
    public void removeMachine(Machine machine){
        for(int i=0; i<machines.size();i++){
            if(machines.get(i).getModule().equals(machine.getModule())){
                machines.remove(i);
                return;
            }
            else{
                //sensorID를 찾을 수 없습니다.
            }
        }
        invalidate();
    }*/

    public List<Machine> getMachines(){
        return machines;
    }

    public void setMachines(List<Machine> machines, int token){
        this.machines = machines;
        double minX=-1500;
        double minY=-1500;

        if(token == 0){
            settingMachine.initAlphaBeta();
        }

        for(int i=0;i<machines.size();i++){

            if(i==0){
                minX = machines.get(i).getX();
                minY = machines.get(i).getY();
            } else{
                if(minX >= machines.get(i).getX()){
                    minX = machines.get(i).getX();
                }
                if(minY >= machines.get(i).getY()){
                    minY = machines.get(i).getY();
                }
            }
        }

        settingMachine.addAlpha(-minX);
        settingMachine.addBeta(-minY);
        invalidate();
    }
}
