package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 26..
 */
public class AdapterArticleList extends BaseAdapter{

    LayoutInflater mInflater;
    ArrayList<JSONObject> articles;

    public AdapterArticleList(Context context, ArrayList<JSONObject> articles) {
        this.articles = articles;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return articles.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JSONObject article = articles.get(position);

        convertView = mInflater.inflate(R.layout.list_article, null);
        TextView articleName = (TextView) convertView.findViewById(R.id.text_title1);
        TextView commentCount = (TextView) convertView.findViewById(R.id.text_commentCount1);
        TextView writer = (TextView) convertView.findViewById(R.id.text_author1);
        TextView articleDate = (TextView) convertView.findViewById(R.id.text_articleDate1);

        try {
            articleName.setText(article.getString("title"));
            commentCount.setText("(" + article.getString("commentCount")+ ")");
            writer.setText(article.getString("author"));
            articleDate.setText(article.getString("articleDate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

}
