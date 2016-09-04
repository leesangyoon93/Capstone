package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Article extends Fragment {
    Button showButton = null;
    Button editButton = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_article, container, false);
        showButton = (Button)root.findViewById(R.id.btn_showArticle);
        editButton = (Button)root.findViewById(R.id.btn_newArticle);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowArticle.class);
                startActivity(intent);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditArticle.class);
                intent.putExtra("path", "fromList");
                startActivity(intent);
            }
        });
        if(container == null)
            return null;

        return root;
    }
}