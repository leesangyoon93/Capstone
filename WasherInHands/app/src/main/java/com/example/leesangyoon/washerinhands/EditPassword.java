package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class EditPassword extends AppCompatActivity {

    EditText input_newPassword = null;
    EditText input_checkPassword = null;
    Button changePasswordButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_editpassword);

        changePasswordButton = (Button)findViewById(R.id.btn_changePassword);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input_newPassword = (EditText)findViewById(R.id.input_newPassword);
                input_checkPassword = (EditText)findViewById(R.id.input_checkPassword);

                String newPassword = input_newPassword.getText().toString();
                String checkPassword = input_checkPassword.getText().toString();

                if(newPassword.equals(checkPassword)){
                    try {
                        changePasswordToServer(newPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(EditPassword.this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void changePasswordToServer (final String newPassword) throws Exception{

        String URL = "http://52.41.19.232/editPassword";

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("userId", User.getInstance().getUserId());
        postParam.put("password", newPassword);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    if(response.getString("result").equals("fail")) {
                        Toast.makeText(EditPassword.this, "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    } else if(response.getString("result").equals("success")){

                        Toast.makeText(EditPassword.this, "정상적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        User.getInstance().setPassword(newPassword);

                        Intent intent = new Intent(EditPassword.this, MainActivity.class);
                        intent.putExtra("fragNum",2);
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
                    }
                });

        volley.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}