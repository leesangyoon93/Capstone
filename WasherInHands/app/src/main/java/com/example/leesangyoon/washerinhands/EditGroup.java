package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class EditGroup extends AppCompatActivity {

    String SENSOR_ID = "sensor111";
    List<Machine> machines = new ArrayList<Machine>();
    Washer washer;
    List<Washer> washers = new ArrayList<Washer>();
    SharedPreferences currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgroup);

        final Canvas_view canvasview = new Canvas_view(this);
        LinearLayout canvasLayout = (LinearLayout)findViewById(R.id.canvas_layout);


        if(!washers.isEmpty()) {
            for(int i=0;i<washers.size();i++){
                Machine machine = new Machine(washers.get(i).getModule(), washers.get(i).getX(), washers.get(i).getY());
                machines.add(machine);
                canvasview.setMachines(machines);
            }
        }
        Button btn_addMachine = (Button)findViewById(R.id.add_Machine);
        Button btn_saveWasherRoom = (Button)findViewById(R.id.save_washerRoom);

        canvasLayout.addView(canvasview);

        btn_addMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasview.addMachine(SENSOR_ID);
            }
        });

        btn_saveWasherRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                machines = canvasview.getMachines();

                Intent intent = new Intent(EditGroup.this, ShowGroup.class);
                startActivity(intent);
            }
        });
    }


}
