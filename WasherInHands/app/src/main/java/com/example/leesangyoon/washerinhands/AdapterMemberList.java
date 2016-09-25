package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016-09-22.
 */
public class AdapterMemberList extends BaseAdapter {

    LayoutInflater mInflater;
    List<JSONObject> members;

    public AdapterMemberList(Context context, List<JSONObject> members){
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.members = members;
    }

    public final int getCount(){
        return members.size();
    }

    public final Object getItem(int position){
        return members.get(position);
    }

    public final long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null){
            view = mInflater.inflate(R.layout.grid_item, viewGroup, false);
        }

        final JSONObject member = members.get(position);

        ImageView imageView = (ImageView)view.findViewById(R.id.img_user);
        TextView textView = (TextView)view.findViewById(R.id.text_memberName);

        imageView.setImageResource(R.drawable.user_icon);

        try {
            textView.setText(member.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
