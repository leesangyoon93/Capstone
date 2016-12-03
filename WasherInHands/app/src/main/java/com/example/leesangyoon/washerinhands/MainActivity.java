package com.example.leesangyoon.washerinhands;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;
import java.util.Vector;

// 로고, 탭 색 변경, 게시판 디자인 전체 다 바꾸기

public class MainActivity extends AppCompatActivity {

    TabLayout mainTab;
    BackPressCloseHandler backPressCloseHandler;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();

        mainTab = (TabLayout) findViewById(R.id.mainTab);
        mainTab.addTab(mainTab.newTab().setText("메인"));
        mainTab.addTab(mainTab.newTab().setText("그룹"));
        mainTab.addTab(mainTab.newTab().setText("프로필"));
        mainTab.addTab(mainTab.newTab().setText("설정"));
        mainTab.setTabGravity(TabLayout.GRAVITY_FILL);
        mainTab.setTabTextColors(ColorStateList.valueOf(Color.BLACK));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.mainPager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), mainTab.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTab));
        mainTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(intent.getIntExtra("fragNum", 0));
//
        registBroadcastReceiver();
        getInstanceIdToken();

//        // 토큰을 보여줄 TextView를 정의
//        mInformationTextView = (TextView) findViewById(R.id.informationTextView);
//        mInformationTextView.setVisibility(View.GONE);
//        // 토큰을 가져오는 동안 인디케이터를 보여줄 ProgressBar를 정의
//        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
//        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
//        // 토큰을 가져오는 Button을 정의
//        mRegistrationButton = (Button) findViewById(R.id.registrationButton);
//        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
//            /**
//             * 버튼을 클릭하면 토큰을 가져오는 getInstanceIdToken() 메소드를 실행한다.
//             * @param view
//             */
//            @Override
//            public void onClick(View view) {
//                getInstanceIdToken();
//            }
//        });
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    User.getInstance().setToken(token);
                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
