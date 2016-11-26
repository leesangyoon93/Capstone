package com.example.leesangyoon.washerinhands;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class Login extends AppCompatActivity {

    Button loginButton, notRegisterButton;
    EditText userId, password;

    String URL;

    SharedPreferences userSession;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        loginButton = (Button) findViewById(R.id.submit_login);
        notRegisterButton = (Button) findViewById(R.id.notRegistered);

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        if(userSession.contains("userId")){
            try {
                loginToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loginButton.setBackground(getResources().getDrawable(R.drawable.pressed_shape));
                        userId = (EditText) findViewById(R.id.input_loginId);
                        password = (EditText) findViewById(R.id.input_loginPassword);
                        String id = userId.getText().toString();
                        String pw = password.getText().toString();

                        if(id.isEmpty()){
                            Toast.makeText(Login.this, "아이디를 쳐주세요.", Toast.LENGTH_SHORT).show();
                        }else if(pw.isEmpty()){
                            Toast.makeText(Login.this, "비밀번호를 쳐주세요.", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                userinfoToServer(id, pw);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        loginButton.setBackground(getResources().getDrawable(R.drawable.shape));
                        break;
                }
                return true;
            }
        });

        notRegisterButton.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        notRegisterButton.setBackground(getResources().getDrawable(R.drawable.pressed_shape));
                        Intent intent = new Intent(Login.this, Register.class);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_UP:
                        notRegisterButton.setBackground(getResources().getDrawable(R.drawable.shape));
                        break;
                }
                return true;
            }
        });
    }

    private void userinfoToServer(final String id, final String pw) throws Exception {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);

        URL = "http://52.41.19.232/login";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("userId", id);
        postParam.put("password", pw);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {

                    if (response.toString().contains("result")) {
                        if (response.getString("result").equals("fail")) {
                            Toast.makeText(Login.this, "알 수 없는 에러가 발생합니다.", Toast.LENGTH_SHORT).show();
                        }else if(response.getString("result").equals("failId")){
                            Toast.makeText(Login.this, "존재하지 않는 ID입니다.", Toast.LENGTH_SHORT).show();
                        }else if(response.getString("result").equals("failPw")){
                            Toast.makeText(Login.this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        //response받아와서 user객체에 넘김
                        User.getInstance().setUserId(response.getString("userId"));
                        User.getInstance().setPassword(response.getString("password"));
                        User.getInstance().setUserName(response.getString("userName"));
                        User.getInstance().setAdmin(response.getBoolean("isAdmin"));
                        User.getInstance().setMainRoomName(response.getString("mainRoomName"));


                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString("userId", User.getInstance().getUserId());
                        editor.commit();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });

        volley.getInstance().addToRequestQueue(req);
    }

    private void loginToServer() throws Exception {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);

        URL = String.format("http://52.41.19.232/getUser?userId=%s", URLEncoder.encode(userSession.getString("userId",""), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if(response.getClass().getName().equals("String")) {
                        if (response.getString("result").equals("fail")) {
                            //서버연결에 실패하였습니다.
                            Toast.makeText(Login.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(response.isNull("userId")){
                            Intent intent = new Intent(Login.this, Login.class);
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

                            Intent intent = new Intent(Login.this, MainActivity.class);
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
                Toast.makeText(Login.this,
                        "네트워크 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }
}
