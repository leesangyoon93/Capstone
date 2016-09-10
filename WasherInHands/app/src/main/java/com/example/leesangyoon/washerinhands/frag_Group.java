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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Group extends Fragment implements AdapterView.OnItemClickListener {

    ListView roomList;
    AdapterRoomList adapterRoomList;
    ArrayList<WasherRoom> washerRooms = new ArrayList<WasherRoom>();
    User user = new User();

    boolean isAdmin = true;
    Button addGroup;

    SharedPreferences userSession;
    SharedPreferences currentRoom;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        userSession = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        currentRoom = getActivity().getSharedPreferences("CurrentRoom", Context.MODE_PRIVATE);

        View root = inflater.inflate(R.layout.fragment_group, container, false);
        addGroup = (Button) root.findViewById(R.id.btn_addGroup);

        roomList = (ListView) root.findViewById(R.id.listView_group);
        roomList.setOnItemClickListener(this);

        user = user.getUserById(userSession.getString("userId", ""));
        washerRooms = user.getWasherRooms();

        adapterRoomList = new AdapterRoomList(getActivity(), washerRooms);
        adapterRoomList.notifyDataSetChanged();

        roomList.setAdapter(adapterRoomList);
//        roomList.setCacheColorHint(Color.TRANSPARENT);

//        showGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ShowGroup.class);
//                startActivity(intent);
//            }
//        });

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

    private void createListView() {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WasherRoom washerRoom = adapterRoomList.getItem(position);

        SharedPreferences.Editor editor = currentRoom.edit();
        editor.putString("roomName", washerRoom.getRoomName());
        editor.commit();

        Intent intent = new Intent(getActivity(), ShowGroup.class);
        startActivity(intent);
    }

}

