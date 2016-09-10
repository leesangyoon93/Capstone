package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroup extends AppCompatActivity {

    User user;
    WasherRoom washerRoom;
    Button saveButton;
    EditText roomName, roomAddress;

    SharedPreferences currentRoom, userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_creategroup);

        saveButton = (Button)findViewById(R.id.submit_createRoom);
        roomName = (EditText) findViewById(R.id.input_createRoomName);
        roomAddress = (EditText)findViewById(R.id.input_createRoomAddress);

        currentRoom = getSharedPreferences("CurrentRoom", Context.MODE_PRIVATE);
        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        user = new User();
        user = user.getUserById(userSession.getString("userId", ""));

        washerRoom = new WasherRoom();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomName.length() != 0 && roomAddress.length() !=0) {

                    String name = roomName.getText().toString();
                    String address = roomAddress.getText().toString();

                    if(!washerRoom.is_existed(name)) {
                        SharedPreferences.Editor editor = currentRoom.edit();
                        editor.putString("roomName", name);
                        editor.commit();

                        washerRoom = new WasherRoom(user, name, address);
                        washerRoom.addMember(user);
                        washerRoom.save();
                        user.addWasherRoom(washerRoom);
                        user.save();

                        Intent intent = new Intent(CreateGroup.this, ShowGroup.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(CreateGroup.this, "이미 존재하는 이름입니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(CreateGroup.this, "이름과 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}