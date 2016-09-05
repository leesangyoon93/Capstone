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
public class Register extends AppCompatActivity {

    Button registerButton;
    EditText userId, password, userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userId = (EditText)findViewById(R.id.input_registerUserId);
        password = (EditText)findViewById(R.id.input_registerPassword);
        userName = (EditText)findViewById(R.id.input_registerUserName);
        registerButton = (Button)findViewById(R.id.submit_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.submit_register:
                        break;
                }
            }
        });
    }

    public void main(View view) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }
}
