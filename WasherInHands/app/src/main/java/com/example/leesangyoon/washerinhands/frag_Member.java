package com.example.leesangyoon.washerinhands;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Member extends Fragment {

    View root;
    GridView memberList;
    AdapterMemberList adapterMemberList;
    List<JSONObject> members = new ArrayList<JSONObject>() ;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        root=inflater.inflate(R.layout.fragment_member, container, false);

        members.clear();

        memberList = (GridView) root.findViewById(R.id.grid_member);

        try {
            showMembersToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapterMemberList = new AdapterMemberList(getActivity(), members);
        adapterMemberList.notifyDataSetChanged();

        memberList.setAdapter(adapterMemberList);

        if(container == null)
            return null;

        return root;
    }



    private void showMembersToServer() throws Exception{
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);

        String URL= String.format("http://52.41.19.232/showGroupMember?roomName=%s",
                URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                for (int i = 0; i < response.length(); i++) {
                    members.add(response.optJSONObject(i));
                    try {
                        Log.e("dddddddd",members.get(i).getString("userId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterMemberList.notifyDataSetChanged();
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

