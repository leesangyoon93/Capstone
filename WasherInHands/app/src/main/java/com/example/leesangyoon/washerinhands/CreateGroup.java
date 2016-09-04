package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CreateGroup extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_creategroup);
    }

    public void Create(View view) {
        Intent intent = new Intent(CreateGroup.this, ShowGroup.class);
        startActivity(intent);
    }
}