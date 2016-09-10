package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class Login extends AppCompatActivity {

    Button loginButton, notRegisterButton;
    EditText userId, password;

    SharedPreferences userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_login);

        userId = (EditText) findViewById(R.id.input_loginId);
        password = (EditText) findViewById(R.id.input_loginPassword);
        loginButton = (Button) findViewById(R.id.submit_login);
        notRegisterButton = (Button) findViewById(R.id.notRegistered);

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String id = userId.getText().toString();
                String pw = password.getText().toString();
                if (user.authenticate(id, pw)) {
                    SharedPreferences.Editor editor = userSession.edit();

                    editor.putString("userId", id);
                    editor.putString("password", pw);
                    editor.commit();

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
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
}
