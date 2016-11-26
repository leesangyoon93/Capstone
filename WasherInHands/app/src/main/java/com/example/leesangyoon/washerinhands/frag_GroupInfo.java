package com.example.leesangyoon.washerinhands;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_GroupInfo extends Fragment {

    boolean isHost;
    TextView roomName;
    TextView isMainText;
    Button editGroup;
    Button mainGroup;
    Button exitGroup;
    View root;

    //alpha,beta 초기화 문제 때문에 존재하는 토큰값
    int token=0;

    canvasView_onlyShow canvasview;
    LinearLayout canvasLayout;

    List<Machine> machines = new ArrayList<Machine>();

    ServerThread serverThread = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        root = inflater.inflate(R.layout.fragment_groupinfo, container, false);

        editGroup = (Button)root.findViewById(R.id.btn_editGroup);
        mainGroup = (Button)root.findViewById(R.id.btn_main);
        exitGroup = (Button)root.findViewById(R.id.btn_exitGroup);
        isMainText = (TextView)root.findViewById(R.id.text_main);
        roomName = (TextView)root.findViewById(R.id.text_roomName);

        roomName.setText(WasherRoom.getInstance().getRoomName());
        canvasLayout = (LinearLayout)root.findViewById(R.id.cnavas_layout_onlyShow);
        canvasview = new canvasView_onlyShow(getActivity());

        try {
            getWasherToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(User.getInstance().getMainRoomName().isEmpty() && WasherRoom.getInstance().getRoomName().isEmpty()){
            canvasLayout.setVisibility(root.GONE);
        }else{
            canvasLayout.addView(canvasview);
        }

        serverThread = new ServerThread();
        serverThread.start();

        isMainText.setVisibility(root.GONE);
        exitGroup.setVisibility(root.GONE);
        editGroup.setVisibility(root.GONE);

        try {
            hostCheckToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(container == null)
            return null;

        return root;
    }



    private void hostCheckToServer() throws Exception{
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);

        String URL = String.format("http://52.41.19.232/getHost?userId=%s&roomName=%s",
                URLEncoder.encode(User.getInstance().getUserId(), "utf-8"),URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if (response.getString("result").equals("fail")) {
                        isHost = false;
                    } else if(response.getString("result").equals("success")) {
                        isHost = true;
                    }

                    if(isHost) {
                        editGroup.setVisibility(root.VISIBLE);

                        mainGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    setMainToServer();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        editGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), EditGroup.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else {

                        exitGroup.setVisibility(root.VISIBLE);

                        mainGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    setMainToServer();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        exitGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("그룹 가입 확인")
                                        .setMessage(WasherRoom.getInstance().getRoomName() + " 그룹에서 탈퇴하시겠습니까?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // yes 버튼 누르면
                                                try {
                                                    exitGroupToServer();
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
                        });

                    }

                } catch (JSONException e) {
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

    private void exitGroupToServer() throws Exception{

        String URL = String.format("http://52.41.19.232/exitGroup?userId=%s&roomName=%s",
                URLEncoder.encode(User.getInstance().getUserId(), "utf-8"),URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("fail")) {
                        Toast.makeText(getActivity(), "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if(response.getString("result").equals("success")) {
                        if(User.getInstance().getMainRoomName().equals(WasherRoom.getInstance().getRoomName())){
                            User.getInstance().setMainRoomName("");
                        }
                        Toast.makeText(getActivity(), "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(),MainActivity.class);
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
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }

    private void setMainToServer() throws Exception{

        String URL = String.format("http://52.41.19.232/setMainRoom?userId=%s&mainRoomName=%s",
                URLEncoder.encode(User.getInstance().getUserId(), "utf-8"),URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("fail")) {
                        Toast.makeText(getActivity(), "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if(response.getString("result").equals("success")) {
                        User.getInstance().setMainRoomName(WasherRoom.getInstance().getRoomName());
                        Toast.makeText(getActivity(), "메인세탁방으로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
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


    private void getWasherToServer() throws Exception{

        String URL= String.format("http://52.41.19.232/getWasher?roomName=%s",
                URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                machines.clear();
                Message msg = handler.obtainMessage();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Machine machine = new Machine(response.optJSONObject(i).getString("module"),
                                Double.parseDouble(response.optJSONObject(i).getString("x")), Double.parseDouble(response.optJSONObject(i).getString("y")),
                                Integer.parseInt(response.optJSONObject(i).getString("runTime")), Boolean.parseBoolean(response.optJSONObject(i).getString("isTrouble")),
                                Boolean.parseBoolean(response.optJSONObject(i).getString("isWorking")));
                        machines.add(machine);
                    }

                    //서버에서 받아오는게 성공한다면 핸들에 1을 보낸다
                    msg.arg1 = 1;
                    handler.sendMessage(msg);

                }catch(JSONException e){
                    e.printStackTrace();
                    msg.arg1=0;
                    handler.sendMessage(msg);
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

    Handler handler = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {

            switch(msg.arg1){

                case 0:
                    Toast.makeText(getActivity(),"서버와의 연결이 원할하지 않습니다.",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    canvasview.setMachines(machines);
                    Log.e("sssss","쓰레드 성공!!!!");
                    break;
            }

            return true;
        }
    });


    class ServerThread extends Thread implements Runnable{

        private boolean isPlay = false;

        public ServerThread() {
            isPlay = true;
        }

        public void isThreadState(boolean isPlay) {
            this.isPlay = isPlay;
        }

        public void stopThread() {
            this.isPlay = false;
        }

        @Override
        public void run() {
            super.run();
            while(isPlay) {
                try {
                    Thread.sleep(5 * 1000);
                    getWasherToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        serverThread.stopThread();
    }
}
