package com.example.leesangyoon.washerinhands;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        Log.e("ssss","여긴오니?");
        if(container == null)
            return null;

        root = inflater.inflate(R.layout.fragment_groupinfo, container, false);
        Log.e("ssss","여긴?");

        canvasLayout = (LinearLayout)root.findViewById(R.id.cnavas_layout_onlyShow);
        canvasview = new canvasView_onlyShow(getActivity());

        editGroup = (Button)root.findViewById(R.id.btn_editGroup);
        mainGroup = (Button)root.findViewById(R.id.btn_main);
        exitGroup = (Button)root.findViewById(R.id.btn_exitGroup);

        isMainText = (TextView)root.findViewById(R.id.text_main);

        mainGroup.setVisibility(root.GONE);
        exitGroup.setVisibility(root.GONE);
        editGroup.setVisibility(root.GONE);

        if(User.getInstance().getMainRoomName().isEmpty()) {
            isMainText.setText("메인세탁방을 설정해주세요");
            Log.e("ssss","메인 없음");
        }else{
            isMainText.setVisibility(root.GONE);
            Log.e("ssss","메인 있음");
            try {
                getWasherToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
