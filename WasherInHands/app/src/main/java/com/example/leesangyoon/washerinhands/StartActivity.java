package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

        if(userSession.getString("isLogin", "false").equals("true")){
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

        URL = String.format("http://52.41.19.232/getUser");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result") == "fail") {
                        //서버연결에 실패하였습니다.
                        Toast.makeText(StartActivity.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if(response.isNull("userId")){
                            Log.e("sss","일로들어옴1");
                            Intent intent = new Intent(StartActivity.this, Login.class);
                            startActivity(intent);
                        } else{
                            SharedPreferences.Editor editor = userSession.edit();
                            editor.putString("isLogin", "true");
                            editor.commit();

                            Log.e("sss","일로들어옴2");
                            //response받아와서 user객체에 넘김
                            User.getInstance().setUserId(response.getString("userId"));
                            User.getInstance().setPassword(response.getString("password"));
                            User.getInstance().setUserName(response.getString("userName"));
                            User.getInstance().setAdmin(response.getBoolean("isAdmin"));

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
