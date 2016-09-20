package com.example.leesangyoon.washerinhands;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016-09-15.
 */
public class imagePagerAdapter extends FragmentPagerAdapter {

    private int pageCount = 0;

    public imagePagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount =  pageCount;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return frag_image.create(position);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

}
