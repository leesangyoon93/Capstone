package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class ShowGroup extends AppCompatActivity {

    int mCurrentFragmentIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_viewgroup);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(WasherRoom.getInstance().getRoomName());
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        Intent intent = getIntent();
        mCurrentFragmentIndex = intent.getIntExtra("fragNum", 0);
        fragmentReplace(mCurrentFragmentIndex);
    }

    public void fragmentReplace(int index) {
        Fragment frag = null;

        frag = getFragment(index);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragments, frag);
        transaction.commit();
    }

    private Fragment getFragment(int index) {
        Fragment frag = null;

        switch(index) {
            case 0:
                frag = new frag_GroupInfo();
                break;
            case 1:
                frag = new frag_Article();
                break;
            case 2:
                frag = new frag_Member();
                break;
        }
        return frag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_viewgroup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_info:
                mCurrentFragmentIndex = 0;
                break;
            case R.id.menu_article:
                mCurrentFragmentIndex = 1;
                break;
            case R.id.menu_member:
                mCurrentFragmentIndex = 2;
                break;
        }
        fragmentReplace(mCurrentFragmentIndex);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowGroup.this, MainActivity.class);
        intent.putExtra("fragNum",1);
        startActivity(intent);
        super.onBackPressed();
    }
}