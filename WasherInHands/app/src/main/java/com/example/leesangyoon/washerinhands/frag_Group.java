package com.example.leesangyoon.washerinhands;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Group extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if(container == null)
            return null;

        return inflater.inflate(R.layout.fragment_group, container, false);
    }
}
