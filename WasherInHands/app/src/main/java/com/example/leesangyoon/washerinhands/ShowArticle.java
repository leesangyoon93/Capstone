package com.example.leesangyoon.washerinhands;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class ShowArticle extends AppCompatActivity {

    Button editArticle;
    Button deleteArticle;
    Button saveComment;
    ListView commentList;
    TextView text_content;
    TextView title;
    TextView commentCount;
    TextView articleDate;
    TextView author;
    EditText input_comment;
    AdapterCommentList adapterCommentList;

    ArrayList<JSONObject> comments = new ArrayList<JSONObject>();

    String _id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_showarticle);

        Intent intent = getIntent();
        _id = intent.getStringExtra("articleNum");

        editArticle = (Button) findViewById(R.id.btn_editArticle);
        deleteArticle = (Button) findViewById(R.id.btn_deleteArticle);
        commentList = (ListView) findViewById(R.id.listView_comment);
        text_content = (TextView) findViewById(R.id.text_commentCount);
        saveComment = (Button) findViewById(R.id.btn_commentSave);
        input_comment = (EditText) findViewById(R.id.input_comment);
        title = (TextView) findViewById(R.id.text_title2);
        author = (TextView) findViewById(R.id.text_author2);
        commentCount = (TextView) findViewById(R.id.text_commentCount2);
        articleDate = (TextView) findViewById(R.id.text_articleDate2);


        commentList.setAdapter(adapterCommentList);

        adapterCommentList = new AdapterCommentList(ShowArticle.this, comments);
        adapterCommentList.notifyDataSetChanged();

        editArticle.setVisibility(View.GONE);
        deleteArticle.setVisibility(View.GONE);

        try {
            showArticleToServer();
            showCommentsToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveCommentToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void showCommentsToServer() throws Exception {

        String URL = String.format("http://52.41.19.232/showComments?articleId=%s",
                URLEncoder.encode(_id, "utf-8"));

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    comments.add(response.optJSONObject(i));
                    adapterCommentList.notifyDataSetChanged();
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

    private void showArticleToServer() throws Exception {

        String URL = String.format("http://52.41.19.232/showArticle?articleId=%s",
                URLEncoder.encode(_id, "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    text_content.setText(response.getString("content"));
                    title.setText(response.getString("title"));
                    author.setText(response.getString("author"));
                    commentCount.setText(response.getString("commentCount"));
                    articleDate.setText(response.getString("articleDate"));

                    if(User.getInstance().getUserId().equals(response.getString("author"))){
                        editArticle.setVisibility(View.VISIBLE);
                        deleteArticle.setVisibility(View.VISIBLE);


                        editArticle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //넘어가서 DB에서 받아오기 아이디도 받아오기
                                Intent intent = new Intent(ShowArticle.this, EditArticle.class);
                                intent.putExtra("articleNum", _id);
                                intent.putExtra("path", "fromShow");
                                startActivity(intent);
                            }
                        });

                        deleteArticle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //글삭제하는요청
                                Intent intent = new Intent(ShowArticle.this, ShowGroup.class);
                                intent.putExtra("fragNum", 1);
                                startActivity(intent);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void saveCommentToServer() throws Exception {

        String URL = String.format("http://52.41.19.232/saveComment?articleId=%s&author=%s&content=%s",
                URLEncoder.encode(_id, "utf-8"),URLEncoder.encode(User.getInstance().getUserId(), "utf-8"),URLEncoder.encode(input_comment.getText().toString(), "utf-8"));

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("success")) {
                        Toast.makeText(ShowArticle.this, "댓글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShowArticle.this, "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }

                Intent intent = new Intent(ShowArticle.this,ShowArticle.class);
                startActivity(intent);
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