package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    SharedPreferences userSession;

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        userSession = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        String userId = userSession.getString("userId", "");
        if(!userSession.contains("userName")) {
            SharedPreferences.Editor editor = userSession.edit();
            User user = new User();
            user = user.getUserById(userId);
            editor.putString("userName", user.getUserName());
            editor.commit();
        }

        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, frag_Home.class.getName()));
        fragments.add(Fragment.instantiate(this, frag_Group.class.getName()));
        fragments.add(Fragment.instantiate(this, frag_Profile.class.getName()));
        fragments.add(Fragment.instantiate(this, frag_Setting.class.getName()));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        final ViewPager pager = (ViewPager)findViewById(R.id.mainPager);

        pager.setAdapter(adapter);


        final ActionBar actionBar = getSupportActionBar();


        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        actionBar.addTab(actionBar.newTab().setText("HOME").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("GROUP").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("PROFILE").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("SETTING").setTabListener(tabListener));

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        pager.setCurrentItem(intent.getIntExtra("fragNum", 0));

    }

}
