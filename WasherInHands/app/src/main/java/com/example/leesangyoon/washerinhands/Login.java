package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class Login extends AppCompatActivity {

    Button loginButton;
    EditText userId, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_login);

        userId = (EditText)findViewById(R.id.input_loginId);
        password = (EditText)findViewById(R.id.input_loginPassword);
        loginButton = (Button)findViewById(R.id.submit_login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.submit_login:
                        break;
                }
            }
        });
    }

    public void main(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    public void notRegistered(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }
}
