package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-23.
 */
public class canvasView_onlyShow extends View {

    List<Machine> machines = new ArrayList<Machine>();
    List<Bitmap> bitmaps = new ArrayList<Bitmap>();

    float sx, mx, ex;
    float sy, my, ey;

    int MACHINE_SIZE;
    int canvasWidth = 0;
    int canvasHeight = 0;

    boolean isFirst;

    Machine settingMachine = new Machine();

    public canvasView_onlyShow(Context c) {
        super(c);

        isFirst=true;
        settingMachine.initAlphaBeta();

        invalidate();

    }

    public void onDraw(Canvas canvas) {

        Bitmap bitmap_map = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        Canvas mapCanvas = new Canvas(bitmap_map);
        mapCanvas.translate(1000, 1000);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        p.setColor(Color.RED);
        mapCanvas.drawColor(Color.argb(180, 200, 200, 200));
        mapCanvas.drawRect(0 - (int) settingMachine.getAlpha(), 0 - (int) settingMachine.getBeta(), canvas.getWidth() - (int) settingMachine.getAlpha(), canvas.getHeight() - (int) settingMachine.getBeta(), p);

        canvas.drawColor(Color.rgb(230, 230, 230));
        canvas.translate((float) settingMachine.getAlpha(), (float) settingMachine.getBeta());

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        settingMachine.setCanvasSize(canvasWidth, canvasHeight);

        MACHINE_SIZE = canvas.getWidth() / 4;
        if (MACHINE_SIZE > 300) {
            MACHINE_SIZE = 300;
        }
        settingMachine.setMACHINE_SIZE(MACHINE_SIZE);

        if (!machines.isEmpty()) {
            for (int i = 0; i < machines.size(); i++) {

                if (machines.get(i).getTrouble()) {
                    Bitmap machineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_broken);
                    bitmaps.add(machineBitmap);
                    canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE + 20, MACHINE_SIZE + 20, false), (float) machines.get(i).getX(), (float) machines.get(i).getY(), null);
                    mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE + 20, MACHINE_SIZE + 20, false), (float) machines.get(i).getX(), (float) machines.get(i).getY(), null);
                } else if (machines.get(i).getWorking()) {
                    Bitmap machineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_working);
                    bitmaps.add(machineBitmap);
                    canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE + 20, MACHINE_SIZE + 20, false), (float) machines.get(i).getX(), (float) machines.get(i).getY(), null);
                    mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE + 20, MACHINE_SIZE + 20, false), (float) machines.get(i).getX(), (float) machines.get(i).getY(), null);
                } else if (!machines.get(i).getWorking()) {
                    Bitmap machineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.washer_machine_notworking);
                    bitmaps.add(machineBitmap);
                    canvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE + 20, MACHINE_SIZE + 20, false), (float) machines.get(i).getX(), (float) machines.get(i).getY(), null);
                    mapCanvas.drawBitmap(bitmaps.get(i).createScaledBitmap(bitmaps.get(i), MACHINE_SIZE + 20, MACHINE_SIZE + 20, false), (float) machines.get(i).getX(), (float) machines.get(i).getY(), null);
                }
            }
        }

        bitmap_map = bitmap_map.createScaledBitmap(bitmap_map, canvas.getWidth()/5, canvas.getHeight()/5, false);
        canvas.drawBitmap(bitmap_map,canvas.getWidth()-bitmap_map.getWidth()-(int)settingMachine.getAlpha(),0-(int)settingMachine.getBeta(),null);


        bitmaps.clear();
    }

    int machineNum=1000;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            sx = event.getX();
            sy = event.getY();

            for(int i=machines.size()-1;i>=0;i--){
                if(sx - settingMachine.getAlpha()>machines.get(i).getX() && sx - settingMachine.getAlpha()<machines.get(i).getX()+MACHINE_SIZE
                        && sy - settingMachine.getBeta() >machines.get(i).getY() && sy - settingMachine.getBeta() < machines.get(i).getY()+MACHINE_SIZE){
                    machineNum=i;
                    break;
                }
            }

            invalidate();
        } else if (action == MotionEvent.ACTION_MOVE) {
            mx = event.getX();
            my = event.getY();

            settingMachine.addAlpha(mx-sx);
            settingMachine.addBeta(my-sy);
            sx=mx;
            sy=my;

            invalidate();
        } else if (action == MotionEvent.ACTION_UP) {
            ex = event.getX();
            ey = event.getY();
            if(machineNum!=1000){
                if(ex - settingMachine.getAlpha()>machines.get(machineNum).getX() && ex - settingMachine.getAlpha()<machines.get(machineNum).getX()+MACHINE_SIZE
                    && ey - settingMachine.getBeta() >machines.get(machineNum).getY() && ey - settingMachine.getBeta() < machines.get(machineNum).getY()+MACHINE_SIZE){

                    String working;
                    if(machines.get(machineNum).getWorking()){
                        working = "작동중";
                    } else{
                        working = "사용 가능";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("세탁기 상세정보")
                            .setMessage("세탁방: " + WasherRoom.getInstance().getRoomName() + "\n" +
                                    "세탁기 번호: " + machineNum+1 + "번 세탁기\n\n" +
                                    "모듈ID: " + machines.get(machineNum).getModule() + "\n" +
                                    "경과시간: " + machines.get(machineNum).getRuntTime() + "\n" +
                                    "작동: " + working + "\n")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // yes 버튼 누르면
                                }
                            });
                    AlertDialog dialog = builder.show();
                    TextView dialogMessage = (TextView)dialog.findViewById(android.R.id.message);
//                    TextView dialogTitle = (TextView)dialog.findViewById(android.R.id.title);
//                    dialogTitle.setGravity(Gravity.CENTER);
//                    dialogTitle.setTextSize(30);
//                    dialogTitle.setTextColor(Color.GREEN);
                    dialogMessage.setGravity(Gravity.CENTER);
                    dialogMessage.setTextColor(Color.BLUE);

                }
            }
        }

        return true;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
        double minX=-1500;
        double minY=-1500;

        if(isFirst) {

            settingMachine.initAlphaBeta();

            for (int i = 0; i < machines.size(); i++) {

                if (i == 0) {
                    minX = machines.get(i).getX();
                    minY = machines.get(i).getY();
                } else {
                    if (minX >= machines.get(i).getX()) {
                        minX = machines.get(i).getX();
                    }
                    if (minY >= machines.get(i).getY()) {
                        minY = machines.get(i).getY();
                    }
                }
            }

            settingMachine.addAlpha(-minX);
            settingMachine.addBeta(-minY);
            isFirst=false;
        }

        invalidate();
    }

}

