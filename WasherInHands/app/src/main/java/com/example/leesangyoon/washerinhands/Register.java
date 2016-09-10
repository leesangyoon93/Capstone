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

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class Register extends AppCompatActivity {

    Button registerButton;
    EditText userId, password, userName;

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

                        SharedPreferences.Editor editor = userSession.edit();

                        editor.putString("userId", id);
                        editor.putString("password", pw);
                        editor.putString("userName", name);

                        editor.commit();

                        User user = new User();

                        if(user.is_existed(id))
                            Toast.makeText(Register.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        else {
                            user = new User(id, pw, name, false);
                            user.save();

                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }
}
