package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class StartActivity extends AppCompatActivity {

    Button registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);

//        if(userSession.contains("userId")) {
//            Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

        registerButton = (Button)findViewById(R.id.btn_register);
        loginButton = (Button)findViewById(R.id.btn_login);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, Register.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
