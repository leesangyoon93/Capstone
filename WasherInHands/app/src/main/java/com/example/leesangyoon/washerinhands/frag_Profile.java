package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Profile extends Fragment {
    Button changePassword = null;
    Button logoutButton = null;
    TextView profileId, profileName, textView = null;

    private String URL;
    SharedPreferences userSession;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        changePassword = (Button) root.findViewById(R.id.btn_pwchange);
        logoutButton = (Button) root.findViewById(R.id.btn_logout);
        profileId = (TextView) root.findViewById(R.id.profile_userId);
        profileName = (TextView) root.findViewById(R.id.profile_userName);

        String userId = User.getInstance().getUserId();
        String userName = User.getInstance().getUserName();

        profileId.setText(userId);
        profileName.setText(userName);

        userSession = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditPassword.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    logoutToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (container == null)
            return null;

        return root;
    }

    private void logoutToServer() throws Exception {

        URL = String.format("http://52.41.19.232/logout");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("success")) {

                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString("isLogin", "false");
                        editor.commit();

                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);

                        Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        //서버연결에 실패하였습니다.
                        Toast.makeText(getActivity(), "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }
}