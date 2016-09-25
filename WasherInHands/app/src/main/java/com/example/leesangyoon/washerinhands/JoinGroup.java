package com.example.leesangyoon.washerinhands;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class JoinGroup extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText input_searchRoom = null;
    Button searchButton = null;
    ListView roomList;
    AdapterRoomList adapterRoomList;
    ArrayList<JSONObject> washerRooms = new ArrayList<JSONObject>();

    String roomName;
    String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_joingroup);

        searchButton = (Button) findViewById(R.id.btn_search);
        input_searchRoom = (EditText) findViewById(R.id.input_searchGroup);

        roomList = (ListView) findViewById(R.id.listView_search);
        roomList.setOnItemClickListener(this);

        adapterRoomList = new AdapterRoomList(JoinGroup.this, washerRooms,2);
        adapterRoomList.notifyDataSetChanged();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchRoom = input_searchRoom.getText().toString();
                washerRooms.clear();

                searchRoom = searchRoom.trim();

                if (searchRoom.isEmpty()) {
                    Toast.makeText(JoinGroup.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        searchGroupToServer(searchRoom);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        roomList.setAdapter(adapterRoomList);

    }

    private void createListView() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            roomName = adapterRoomList.getItem(position).getString("roomName");
            address = adapterRoomList.getItem(position).getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //확인 팝업
        new AlertDialog.Builder(JoinGroup.this)
                .setTitle("그룹 가입 확인")
                .setMessage(roomName + " 그룹에 가입하시겠습니까?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // yes 버튼 누르면
                        try {
                            joinGroupToServer(roomName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no 버튼 누르면

                    }
                })
                .show();

    }


    private void searchGroupToServer(String roomName) throws Exception {

        String URL = String.format("http://52.41.19.232/searchGroup?searchName=%s",
                URLEncoder.encode(roomName, "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if (response.length() == 0) {
                    Toast.makeText(JoinGroup.this, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        washerRooms.add(response.optJSONObject(i));
                        adapterRoomList.notifyDataSetChanged();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }

    private void joinGroupToServer(final String roomName) throws Exception{

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("roomName", roomName);
        postParam.put("userId", User.getInstance().getUserId());

        String URL = "http://52.41.19.232/joinGroup";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    if(response.toString().contains("result")){
                        if(response.getString("result").equals("fail")){
                            Toast.makeText(JoinGroup.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        } else if(response.getString("result").equals("overlap")){
                            Toast.makeText(JoinGroup.this, "이미 가입된 세탁방입니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        WasherRoom.getInstance().setRoomName(response.getString("roomName"));
                        WasherRoom.getInstance().setAddress(response.getString("address"));

                        if(User.getInstance().getMainRoomName().isEmpty()){
                            User.getInstance().setMainRoomName(response.getString("roomName"));
                        }

                        Intent intent = new Intent(JoinGroup.this, ShowGroup.class);
                        startActivity(intent);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }
}
