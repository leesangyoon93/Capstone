package com.example.leesangyoon.washerinhands;

import android.content.DialogInterface;
import android.content.Intent;
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
    LinearLayout layout_host;
    LinearLayout layout_guest;
    Button editGroup;
    Button mainGroup;
    Button exitGroup;
    View root;

    canvasView_onlyShow canvasview;
    LinearLayout canvasLayout;

    List<Machine> machines = new ArrayList<Machine>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 서버한테 정보 주기적으로 계속 받아와야해
                Log.e("sssss","Thread Test");
            }
        }).start();

        root = inflater.inflate(R.layout.fragment_groupinfo, container, false);
        layout_host = (LinearLayout)root.findViewById(R.id.layout_btn_host);
        layout_guest = (LinearLayout)root.findViewById(R.id.layout_btn_guest);

        editGroup = (Button)root.findViewById(R.id.btn_editGroup);
        mainGroup = (Button)root.findViewById(R.id.btn_main);
        exitGroup = (Button)root.findViewById(R.id.btn_exitGroup);
        isMainText = (TextView)root.findViewById(R.id.text_main);
        roomName = (TextView)root.findViewById(R.id.text_groupInfo_name);

        canvasLayout = (LinearLayout)root.findViewById(R.id.cnavas_layout_onlyShow);
        canvasview = new canvasView_onlyShow(getActivity());

        if(User.getInstance().getMainRoomName().isEmpty() && WasherRoom.getInstance().getRoomName().isEmpty()){
            canvasLayout.setVisibility(root.GONE);
        }else{
            canvasLayout.addView(canvasview);
        }

        try {
            getWasherToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }


        isMainText.setVisibility(root.GONE);
        layout_host.setVisibility(root.GONE);
        layout_guest.setVisibility(root.GONE);

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

        String URL = String.format("http://52.41.19.232/getHost?userId=%s&roomName=%s",
                URLEncoder.encode(User.getInstance().getUserId(), "utf-8"),URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("fail")) {
                        isHost = false;
                    } else if(response.getString("result").equals("success")) {
                        isHost = true;
                    }

                    if(isHost) {
                        layout_host.setVisibility(root.VISIBLE);

                        editGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), EditGroup.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else {

                        layout_guest.setVisibility(root.VISIBLE);

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

                try {
                    for (int i = 0; i < response.length(); i++) {
                        Machine machine = new Machine(response.optJSONObject(i).getString("module"),
                                Double.parseDouble(response.optJSONObject(i).getString("x")), Double.parseDouble(response.optJSONObject(i).getString("y")));
                        machines.add(machine);
                    }
                    canvasview.setMachines(machines);
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
}
