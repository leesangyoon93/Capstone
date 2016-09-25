package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

// EditArticle,ShowArticle,fragArticle,fragHome
// Login,Register,showGroup,StartActivity,fragGroup,fragProfile,JoinGroup,EditPassword

//Article 리스트뷰 만들기
//startActivity 밑에 동그란 바, 그림 이상하게 넘어가는거 해결
//네트워크 잡힐때까지 기다려주기(progress bar)

//세탁기에 누르면 정보띄우기
//
//게시판
//채팅



/**
 * Created by daddyslab on 2016. 9. 4..
 */

public class StartActivity extends AppCompatActivity {

    imagePagerAdapter adapter;
    SharedPreferences userSession;

    static String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        if(userSession.contains("userId")){
            try {
                loginToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            adapter = new imagePagerAdapter(getSupportFragmentManager(),4);
            final ViewPager pager = (ViewPager)findViewById(R.id.imagePagerView);

            pager.setAdapter(adapter);
        }
    }

    private void loginToServer() throws Exception {

        URL = String.format("http://52.41.19.232/getUser?userId=%s", URLEncoder.encode(userSession.getString("userId",""), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getClass().getName().equals("String")) {
                        if (response.getString("result").equals("fail")) {
                            //서버연결에 실패하였습니다.
                            Toast.makeText(StartActivity.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(response.isNull("userId")){
                            Intent intent = new Intent(StartActivity.this, Login.class);
                            startActivity(intent);
                        } else{

                            //response받아와서 user객체에 넘김
                            User.getInstance().setUserId(response.getString("userId"));
                            User.getInstance().setPassword(response.getString("password"));
                            User.getInstance().setUserName(response.getString("userName"));
                            User.getInstance().setAdmin(response.getBoolean("isAdmin"));
                            User.getInstance().setMainRoomName(response.getString("mainRoomName"));

                            SharedPreferences.Editor editor = userSession.edit();
                            editor.putString("userId", User.getInstance().getUserId());
                            editor.commit();

                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
                Toast.makeText(StartActivity.this,
                        "네트워크 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }


}
