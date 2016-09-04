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
public class frag_Group extends Fragment {
    boolean isAdmin = true;
    Button showGroup = null;
    Button addGroup = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        showGroup = (Button) root.findViewById(R.id.btn_showGroup);
        addGroup = (Button) root.findViewById(R.id.btn_addGroup);

        showGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowGroup.class);
                startActivity(intent);
            }
        });

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (!isAdmin)
                    intent = new Intent(getActivity(), JoinGroup.class);
                else
                    intent = new Intent(getActivity(), CreateGroup.class);
                startActivity(intent);
            }
        });

        if(container==null)
            return null;

        return root;
    };


}

