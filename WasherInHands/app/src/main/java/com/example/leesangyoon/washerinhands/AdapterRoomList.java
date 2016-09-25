package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 7..
 */

public class AdapterRoomList extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<JSONObject> washerRooms;

    private int token=0;

    public AdapterRoomList(Context context, ArrayList<JSONObject> washerRooms, int token) {

        this.washerRooms = washerRooms;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.token = token;
    }
    @Override
    public int getCount() {
        return washerRooms.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return washerRooms.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JSONObject washerRoom = washerRooms.get(position);

        convertView = mInflater.inflate(R.layout.list_room, null);
        TextView textName = (TextView) convertView.findViewById(R.id.text_groupName);
        TextView textAddress = (TextView) convertView.findViewById(R.id.text_groupAddress);

        LinearLayout starimgView = (LinearLayout)convertView.findViewById(R.id.star_img_layout);
        ImageView img_main = (ImageView) convertView.findViewById(R.id.img_main);

        if(token == 2){
            starimgView.setVisibility(View.GONE);
        }

        try {
            if(washerRoom.getString("roomName").equals(User.getInstance().getMainRoomName())){
                img_main.setImageResource(R.drawable.main_star);
            }else{
                img_main.setImageResource(R.drawable.not_main_star);
            }
            textName.setText(washerRoom.getString("roomName"));
            textAddress.setText(washerRoom.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
