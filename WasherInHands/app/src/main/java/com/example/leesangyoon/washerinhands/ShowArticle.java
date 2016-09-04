package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class ShowArticle extends AppCompatActivity {
    boolean isAuthor = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_showarticle);

        Button editArticle = (Button)findViewById(R.id.btn_editArticle);
        Button deleteArticle = (Button)findViewById(R.id.btn_deleteArticle);

        if(isAuthor) {
            editArticle.setVisibility(View.VISIBLE);
            deleteArticle.setVisibility(View.VISIBLE);
        }
        else {
            editArticle.setVisibility(View.GONE);
            deleteArticle.setVisibility(View.GONE);
        }
    }


    public void Edit(View view) {
        Intent intent = new Intent(ShowArticle.this, EditArticle.class);
        intent.putExtra("path", "fromShow");
        startActivity(intent);
    }

    public void Delete(View view) {
        Intent intent = new Intent(ShowArticle.this, ShowGroup.class);
        intent.putExtra("fragNum", 1);
        startActivity(intent);
    }
}