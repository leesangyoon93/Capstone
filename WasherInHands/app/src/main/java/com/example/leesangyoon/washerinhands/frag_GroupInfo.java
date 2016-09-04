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
public class frag_GroupInfo extends Fragment {
    boolean isHost = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_groupinfo, container, false);
        Button editGroup = (Button)root.findViewById(R.id.btn_editGroup);
        Button deleteGroup = (Button)root.findViewById(R.id.btn_deleteGroup);

        if(isHost) {
            editGroup.setVisibility(root.VISIBLE);
            deleteGroup.setVisibility(root.VISIBLE);

            editGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CreateGroup.class);
                    startActivity(intent);
                }
            });

            deleteGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("fragNum", 1);
                    startActivity(intent);
                }
            });
        }
        else {
            editGroup.setVisibility(root.GONE);
            deleteGroup.setVisibility(root.GONE);
        }

        if(container == null)
            return null;

        return root;
    }
}
