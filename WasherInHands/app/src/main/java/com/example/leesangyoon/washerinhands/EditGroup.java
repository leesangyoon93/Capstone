package com.example.leesangyoon.washerinhands;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Map;

public class EditGroup extends AppCompatActivity {

    String SENSOR_ID;
    List<Machine> machines = new ArrayList<Machine>();

    canvasView canvasview;
    LinearLayout canvasLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgroup);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        canvasview = new canvasView(EditGroup.this);
        canvasLayout = (LinearLayout)findViewById(R.id.canvas_layout);
        canvasLayout.addView(canvasview);

        try {
            getWasherToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editgroup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_saveGroup:
                machines = canvasview.getMachines();
                if(!machines.isEmpty()){
                    try {
                        saveGroupToServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.menu_deleteGroup:
                new AlertDialog.Builder(EditGroup.this)
                        .setTitle("그룹 삭제 확인")
                        .setMessage(WasherRoom.getInstance().getRoomName() + " 그룹을 삭제하시겠습니까?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // yes 버튼 누르면
                                try {
                                    deleteGroupToServer();
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
                break;
            case R.id.menu_addWasher:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void findModuleToServer(final String Module_id) throws Exception {

        String URL="http://52.41.19.232/findModule";

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("moduleId", Module_id);
        postParam.put("roomName", WasherRoom.getInstance().getRoomName());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("fail")) {
                        showDialog();
                        Toast.makeText(EditGroup.this, "모듈을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();

                    } else if(response.getString("result").equals("success")) {
                        Toast.makeText(EditGroup.this, "모듈이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        canvasview.addMachine(SENSOR_ID);
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

    private void saveGroupToServer() throws Exception {

        String URL="http://52.41.19.232/saveGroup";

        String str="";

        for(int i=0;i<machines.size();i++){
            if(i==0){
                str +=  "[{\"x\":"+machines.get(i).getX()+"," +
                        "\"y\":"+machines.get(i).getY()+"," +
                        "\"module\":\""+machines.get(i).getModule()+"\"},";

            }else if(i != machines.size()-1){
                str += "{\"x\":"+machines.get(i).getX()+"," +
                        "\"y\":"+machines.get(i).getY()+"," +
                        "\"module\":\""+machines.get(i).getModule()+"\"},";

            }else{
                str +=  "{\"x\":"+machines.get(i).getX()+"," +
                        "\"y\":"+machines.get(i).getY()+"," +
                        "\"module\":\""+machines.get(i).getModule()+"\"}]";
            }
        }

        HashMap<String, String> postParam = new HashMap<String, String>();
        postParam.put("machine", str);
        postParam.put("roomName", WasherRoom.getInstance().getRoomName());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("fail")) {
                        Toast.makeText(EditGroup.this, "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();

                    } else if (response.getString("result").equals("success")) {
                        Toast.makeText(EditGroup.this, "세탁방이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditGroup.this, ShowGroup.class);
                        startActivity(intent);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
            }
        });

        volley.getInstance().addToRequestQueue(req);
    }


    private void getWasherToServer() throws Exception{

        String URL= String.format("http://52.41.19.232/getWasher?roomName=%s",
                URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                machines.clear();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        Machine machine = new Machine(response.optJSONObject(i).getString("module"),
                                Double.parseDouble(response.optJSONObject(i).getString("x")), Double.parseDouble(response.optJSONObject(i).getString("y")));
                        machines.add(machine);
                    }
                    canvasview.setMachines(machines,0);
                }catch(JSONException e){
                    e.printStackTrace();
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

    private void deleteGroupToServer() throws Exception {

        String URL = String.format("http://52.41.19.232/deleteGroup?roomName=%s", URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getString("result").equals("fail")) {
                        Toast.makeText(EditGroup.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }else if(response.getString("result").equals("success")) {
                        Intent intent = new Intent(EditGroup.this,MainActivity.class);
                        intent.putExtra("fragNum",1);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
                Toast.makeText(EditGroup.this,
                        "네트워크 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }












    private void showDialog(){
        final EditText input_module = new EditText(EditGroup.this);
        input_module.setInputType(InputType.TYPE_CLASS_TEXT);
        input_module.setLines(1);

        new AlertDialog.Builder(EditGroup.this)
                .setTitle("세탁기 추가")
                .setMessage("세탁기에 부착한 센서의 ID를 적어주세요.")
                .setView(input_module)
                .setPositiveButton(android.R.string.search_go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // yes 버튼 누르면
                        SENSOR_ID = input_module.getText().toString();
                        try {
                            findModuleToServer(SENSOR_ID);
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditGroup.this, ShowGroup.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
