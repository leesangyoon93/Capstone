package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class Login extends AppCompatActivity {

    Button loginButton, notRegisterButton;
    EditText userId, password;

    final String URL = "http://52.41.19.232/login";

    SharedPreferences userSession;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.submit_login);
        notRegisterButton = (Button) findViewById(R.id.notRegistered);

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }
        });

        notRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void userinfoToServer(final String id, final String pw) throws Exception {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("userId", id);
        postParam.put("password", pw);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getClass().getName() == "String") {
                        if (response.getString("result") == "fail") {
                            //아이디 또는 비밀번호가 잘못되었습니다
                            Toast.makeText(Login.this, "아이디가 옳바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString("isLogin", "true");
                        editor.commit();

                        //response받아와서 user객체에 넘김
                        User.getInstance().setUserId(response.getString("userId"));
                        User.getInstance().setPassword(response.getString("password"));
                        User.getInstance().setUserName(response.getString("userName"));
                        User.getInstance().setAdmin(response.getBoolean("isAdmin"));

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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        volley.getInstance().addToRequestQueue(req);
    }
}
