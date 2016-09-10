package com.example.leesangyoon.washerinhands;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Profile extends Fragment {
    Button changePassword = null;
    Button logoutButton = null;
    TextView profileId, profileName, textView = null;

    SharedPreferences userSession;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        changePassword = (Button)root.findViewById(R.id.btn_pwchange);
        logoutButton = (Button)root.findViewById(R.id.btn_logout);
        profileId = (TextView)root.findViewById(R.id.profile_userId);
        profileName = (TextView)root.findViewById(R.id.profile_userName);

        userSession = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        String userId = userSession.getString("userId", "");
        String userName = userSession.getString("userName", "");

        profileId.setText(userId);
        profileName.setText(userName);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditPassword.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = userSession.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        if(container == null)
            return null;

        return root;
    }

}