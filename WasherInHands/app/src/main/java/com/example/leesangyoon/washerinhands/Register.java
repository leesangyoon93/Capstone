//response 후 post연구


package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class Register extends AppCompatActivity {

    Button registerButton;
    EditText userId, password, userName;
    final String URL = "http://52.41.19.232/register";
    SharedPreferences userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userId = (EditText)findViewById(R.id.input_registerUserId);
        password = (EditText)findViewById(R.id.input_registerPassword);
        userName = (EditText)findViewById(R.id.input_registerUserName);
        registerButton = (Button)findViewById(R.id.submit_register);

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.submit_register:

                        String id = userId.getText().toString();
                        String pw = password.getText().toString();
                        String name = userName.getText().toString();

                        try{
                            userinfoToServer(id,pw,name);
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        break;
                }
            }
        });

    }




    private void userinfoToServer(final String id, final String pw, final String name) throws Exception{
/*
        URL= String.format("http://52.41.19.232/register?userId=%s&passward=%s&userName=%s",
                URLEncoder.encode(id, "utf-8"),URLEncoder.encode(pw, "utf-8"),URLEncoder.encode(name, "utf-8")
        );



        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,URL,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try{
                    Log.e("sss","3: " + response.getString("result"));

                    if(response.getString("result") == "fail") {
                        //서버연결에 실패하였습니다.
                        Toast.makeText(Register.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    } else if(response.getString("result") == "overlap"){
                        //중복된 아이디 입니다.
                        Toast.makeText(Register.this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString("isLogin", "true");
                        editor.commit();

                        //response받아와서 user객체에 넘김
                        User.getInstance().setUserId(response.getString("userId"));
                        User.getInstance().setPassword(response.getString("password"));
                        User.getInstance().setUserName(response.getString("userName"));
                        User.getInstance().setAdmin(response.getBoolean("isAdmin"));

                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }


                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);*/

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("userId", id);
        postParam.put("password", pw);
        postParam.put("userName", name);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            if(response.getClass().getName() == "String"){
                                if(response.getString("result") == "fail") {
                                    //서버연결에 실패하였습니다.
                                    Toast.makeText(Register.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                } else if(response.getString("result") == "overlap"){
                                    //중복된 아이디 입니다.
                                    Toast.makeText(Register.this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                SharedPreferences.Editor editor = userSession.edit();
                                editor.putString("isLogin", "true");
                                editor.commit();

                                //response받아와서 user객체에 넘김
                                User.getInstance().setUserId(response.getString("userId"));
                                User.getInstance().setPassword(response.getString("password"));
                                User.getInstance().setUserName(response.getString("userName"));
                                User.getInstance().setAdmin(response.getBoolean("isAdmin"));

                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }
}
