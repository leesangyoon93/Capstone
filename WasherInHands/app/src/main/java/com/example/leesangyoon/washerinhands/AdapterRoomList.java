package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 7..
 */
public class AdapterRoomList extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<WasherRoom> washerRooms;

    public AdapterRoomList(Context context, ArrayList<WasherRoom> washerRooms) {
        this.washerRooms = washerRooms;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return washerRooms.size();
    }

    @Override
    public WasherRoom getItem(int position) {
        return washerRooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public void addWasherRoom(WasherRoom washerRoom){
//        washerRooms.add(washerRoom);
//        AdapterRoomList.notifyDatasetChanged();
//    }
//
//    public void removeItem(int positionToRemove){
//        items.remove(positionToRemove);
//        notifyDatasetChanged();
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WasherRoom washerRoom = washerRooms.get(position);
        convertView = mInflater.inflate(R.layout.list_room, null);
        TextView textName = (TextView) convertView.findViewById(R.id.text_groupName);
        TextView textAddress = (TextView) convertView.findViewById(R.id.text_groupAddress);

        textName.setText(washerRoom.getRoomName());
        textAddress.setText(washerRoom.getAddress());
        return convertView;
    }
}
