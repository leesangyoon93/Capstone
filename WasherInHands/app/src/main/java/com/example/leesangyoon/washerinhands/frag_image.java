package com.example.leesangyoon.washerinhands;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016-09-15.
 */
public class frag_image extends android.support.v4.app.Fragment {

    private static int position;
    private Button registerButton, loginButton;

    public static frag_image create(int position){
        frag_image frg = new frag_image();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        frg.setArguments(bundle);
        return frg;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.position = this.getArguments().getInt("position");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imgView = (ImageView)root.findViewById(R.id.imageView);
        registerButton = (Button)root.findViewById(R.id.btn_register);
        loginButton = (Button)root.findViewById(R.id.btn_login);

        Log.e("ssss",Integer.toString(position));

        switch(this.position){
            case 0:
                registerButton.setVisibility(root.GONE);
                loginButton.setVisibility(root.GONE);
                imgView.setImageResource(R.drawable.start_image_1);
                break;
            case 1:
                registerButton.setVisibility(root.GONE);
                loginButton.setVisibility(root.GONE);
                imgView.setImageResource(R.drawable.start_image_2);
                break;
            case 2:
                registerButton.setVisibility(root.GONE);
                loginButton.setVisibility(root.GONE);
                imgView.setImageResource(R.drawable.start_image_3);
                break;
            case 3:
                registerButton.setVisibility(root.VISIBLE);
                loginButton.setVisibility(root.VISIBLE);
                imgView.setImageResource(R.drawable.start_image_4);
                break;
        }

        if(container == null)
            return null;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Register.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return root;
    }

    public static int getFragSize(){
        return position;
    }
}
