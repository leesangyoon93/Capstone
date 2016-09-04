package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class EditArticle extends AppCompatActivity {

    String path = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_editarticle);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
    }

    public void Complete(View view) {
        Intent intent = new Intent(EditArticle.this, ShowArticle.class);
        startActivity(intent);
    }

    public void Cancel(View view) {
        Intent intent = null;
        if(path == "fromList") {
            intent = new Intent(EditArticle.this, ShowGroup.class);
            intent.putExtra("fragNum", 1);
        }
        else {
            intent = new Intent(EditArticle.this, ShowArticle.class);
        }

        startActivity(intent);
    }
}