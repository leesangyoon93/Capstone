package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Article extends Fragment implements AdapterView.OnItemClickListener{
    Button showButton = null;
    Button editButton = null;

    ListView articleList;
    AdapterArticleList adapterArticleList;
    ArrayList<JSONObject> articles = new ArrayList<JSONObject>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_article, container, false);
        showButton = (Button)root.findViewById(R.id.btn_showArticle);
        editButton = (Button)root.findViewById(R.id.btn_newArticle);

        articleList = (ListView) root.findViewById(R.id.listView_group);
        articleList.setOnItemClickListener(this);

        articleList.setAdapter(adapterArticleList);

        adapterArticleList = new AdapterArticleList(getActivity(), articles);
        adapterArticleList.notifyDataSetChanged();

        try {
            getArticlesToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ShowArticle.class);
        try {
            intent.putExtra("articleNum",articles.get(position).getString("_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }


    private void getArticlesToServer() throws Exception{

        String URL= String.format("http://52.41.19.232/getArticles?roomName=%s",
                URLEncoder.encode(WasherRoom.getInstance().getRoomName(), "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    articles.add(response.optJSONObject(i));
                    adapterArticleList.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("development", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        volley.getInstance().addToRequestQueue(req);
    }
}