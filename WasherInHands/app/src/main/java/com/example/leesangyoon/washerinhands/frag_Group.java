package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Group extends Fragment implements AdapterView.OnItemClickListener {

    ListView roomList;
    AdapterRoomList adapterRoomList;
    ArrayList<JSONObject> washerRooms = new ArrayList<JSONObject>();

    boolean isAdmin = User.getInstance().getAdmin();
    Button addGroup;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View root = inflater.inflate(R.layout.fragment_group, container, false);

        washerRooms.clear();
        int token=0;
        try {
            showJoinedGroupToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addGroup = (Button) root.findViewById(R.id.btn_addGroup);

        roomList = (ListView) root.findViewById(R.id.listView_group);
        roomList.setOnItemClickListener(this);

        //리스트뷰에 별찍기 위해 admin검사해서 adapt로 넘김
        if(User.getInstance().getAdmin()==true){
            addGroup.setText("그룹생성");
        }

        adapterRoomList = new AdapterRoomList(getActivity(), washerRooms,1);
        adapterRoomList.notifyDataSetChanged();

        roomList.setAdapter(adapterRoomList);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (!isAdmin)
                    intent = new Intent(getActivity(), JoinGroup.class);
                else
                    intent = new Intent(getActivity(), CreateGroup.class);
                startActivity(intent);
            }
        });

        if(container==null)
            return null;

        return root;
    };

    private void createListView() {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {
            WasherRoom.getInstance().setRoomName(adapterRoomList.getItem(position).getString("roomName"));
            WasherRoom.getInstance().setAddress(adapterRoomList.getItem(position).getString("address"));
        } catch (JSONException e) {
            e.printStackTrace() ;
        }

        Intent intent = new Intent(getActivity(), ShowGroup.class);
        startActivity(intent);
    }


    private void showJoinedGroupToServer() throws Exception{

        String URL= String.format("http://52.41.19.232/showJoinedGroup?userId=%s",
                URLEncoder.encode(User.getInstance().getUserId(), "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    washerRooms.add(response.optJSONObject(i));
                    adapterRoomList.notifyDataSetChanged();
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

