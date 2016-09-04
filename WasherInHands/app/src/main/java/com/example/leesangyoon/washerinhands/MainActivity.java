package com.example.leesangyoon.washerinhands;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }
}
