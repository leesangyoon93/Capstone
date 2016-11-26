package com.example.leesangyoon.washerinhands;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class PagerAdapter extends FragmentPagerAdapter {

    int num;

    public PagerAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
//        return fragments.get(position);
        switch (position) {
            case 0:
                frag_Home tab1 = new frag_Home(); // Fragment 는 알아서 만들자
                return tab1;
            case 1:
                frag_Group tab2 = new frag_Group();
                return tab2;
            case 2:
                frag_Profile tab3 = new frag_Profile();
                return tab3;
            case 3:
                frag_Setting tab4 = new frag_Setting();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
