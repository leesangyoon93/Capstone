package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_GroupInfo extends Fragment {

    boolean isHost;
    // 로그인된 사용자 가져오고 현재방 가져와서 현재방의 호스트랑 같으면 참으로
    // 삭제 누르면 없어지게
    TextView roomName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View root = inflater.inflate(R.layout.fragment_groupinfo, container, false);
        Button editGroup = (Button)root.findViewById(R.id.btn_editGroup);
        Button deleteGroup = (Button)root.findViewById(R.id.btn_deleteGroup);
        roomName = (TextView)root.findViewById(R.id.text_groupInfo_name);


/*
        if(washerRoom.get_host().getClass().equals(user.getClass()))
            isHost = true;
        else
            isHost = false;
*/

        if(isHost) {
            editGroup.setVisibility(root.VISIBLE);
            deleteGroup.setVisibility(root.VISIBLE);

            editGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditGroup.class);
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
