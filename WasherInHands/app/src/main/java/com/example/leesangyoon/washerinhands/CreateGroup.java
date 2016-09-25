package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateGroup extends AppCompatActivity {

    Button createButton;
    EditText roomName, roomAddress;

    String URL = "http://52.41.19.232/createGroup";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_creategroup);

        createButton = (Button) findViewById(R.id.submit_createRoom);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomName = (EditText) findViewById(R.id.input_createRoomName);
                roomAddress = (EditText) findViewById(R.id.input_createRoomAddress);

                String name = roomName.getText().toString();
                String address = roomAddress.getText().toString();

                if (name.length() != 0 && address.length() != 0) {
                    try {
                        createRoomToServer(name, address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(CreateGroup.this, "이름과 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createRoomToServer(final String roomName, final String address) throws Exception {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("roomName", roomName);
        postParam.put("address", address);
        postParam.put("userId", User.getInstance().getUserId());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.toString().contains("result")) {
                        if (response.getString("result").equals("fail")) {
                            Toast.makeText(CreateGroup.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (response.getString("result").equals("overlap")) {
                            Toast.makeText(CreateGroup.this, "같은 이름의 세탁방이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        WasherRoom.getInstance().setRoomName(response.getString("roomName"));
                        WasherRoom.getInstance().setAddress(response.getString("address"));

                        if(User.getInstance().getMainRoomName().isEmpty()){
                            User.getInstance().setMainRoomName(response.getString("roomName"));
                        }

                        Intent intent = new Intent(CreateGroup.this, EditGroup.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateGroup.this, MainActivity.class);
        intent.putExtra("fragNum",1);
        startActivity(intent);
        super.onBackPressed();
    }
}