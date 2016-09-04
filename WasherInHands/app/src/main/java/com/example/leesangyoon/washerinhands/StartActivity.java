package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void login(View view) {
        Intent intent = new Intent(StartActivity.this, Login.class);
        startActivity(intent);
    }
    public void register(View view) {
        Intent intent = new Intent(StartActivity.this, Register.class);
        startActivity(intent);
    }
}
