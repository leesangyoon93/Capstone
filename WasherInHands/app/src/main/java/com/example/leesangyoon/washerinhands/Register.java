package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class Register extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void main(View view) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }
}
