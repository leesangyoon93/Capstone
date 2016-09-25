package com.example.leesangyoon.washerinhands;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 25..
 */

public class ServerThread extends Thread implements Runnable{
    private ServerThread() {}

    List<Machine> machines = new ArrayList<Machine>();
    Handler handler;

    private static class Singleton {
        private static final ServerThread serverThread = new ServerThread();
    }

    public static ServerThread getInstance () {
        return Singleton.serverThread;
    }

    private boolean isPlay = false;

    @Override
    public void run() {
        super.run();
        while(isPlay) {
            try {
                Thread.sleep(10 * 1000);
                getWasherToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
}
