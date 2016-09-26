package com.example.leesangyoon.washerinhands;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import javax.net.ssl.HandshakeCompletedEvent;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Home extends Fragment {

    TextView isMainText;
    View root;

    canvasView_onlyShow canvasview;
    LinearLayout canvasLayout;

    List<Machine> machines = new ArrayList<Machine>();

    Button editGroup;
    Button mainGroup;
    Button exitGroup;

    ServerThread serverThread = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if(container == null)
            return null;

        root = inflater.inflate(R.layout.fragment_groupinfo, container, false);

        canvasLayout = (LinearLayout)root.findViewById(R.id.cnavas_layout_onlyShow);
        canvasview = new canvasView_onlyShow(getActivity());

        editGroup = (Button)root.findViewById(R.id.btn_editGroup);
        mainGroup = (Button)root.findViewById(R.id.btn_main);
        exitGroup = (Button)root.findViewById(R.id.btn_exitGroup);

        isMainText = (TextView)root.findViewById(R.id.text_main);

        try {
            getWasherToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        serverThread = new ServerThread();
        serverThread.start();

        mainGroup.setVisibility(root.GONE);
        exitGroup.setVisibility(root.GONE);
        editGroup.setVisibility(root.GONE);

        if(User.getInstance().getMainRoomName().isEmpty()) {
            isMainText.setText("메인세탁방을 설정해주세요");
        }else{
            isMainText.setVisibility(root.GONE);

            try {
                getWasherToServer();
                getWasherRoomToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvasLayout.addView(canvasview);
        }

        return root;
    }


    private void getWasherToServer() throws Exception{

        String URL= String.format("http://52.41.19.232/getWasher?roomName=%s",
                URLEncoder.encode(User.getInstance().getMainRoomName(), "utf-8"));

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

    private void getWasherRoomToServer() throws Exception{

        String URL= String.format("http://52.41.19.232/getGroup?roomName=%s",
                URLEncoder.encode(User.getInstance().getMainRoomName(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    WasherRoom.getInstance().setRoomName(response.getString("roomName"));
                    WasherRoom.getInstance().setAddress(response.getString("address"));
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