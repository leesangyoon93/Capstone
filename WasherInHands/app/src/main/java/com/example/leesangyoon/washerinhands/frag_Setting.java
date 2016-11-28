package com.example.leesangyoon.washerinhands;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by daddyslab on 2016. 9. 4..
 */
public class frag_Setting extends Fragment implements NumberPicker.OnValueChangeListener {

    Switch push_switch = null;
    TextView tv;
    Timer mTimer = null;
    Message msg = null;
    Boolean state = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null)
            return null;

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        push_switch = (Switch) root.findViewById(R.id.switch1);
        tv = (TextView) root.findViewById(R.id.text_remainTime);

        if (push_switch.isChecked()) {
            try {
                getAlarmToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                getAlarmToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        push_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (push_switch.isChecked()) {
                    show();
                } else {
                    tv.setText("0");
                    try {
                        stopAlarmToServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return root;
    }

    private void show() {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.clearFocus();
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(String.valueOf(np.getValue()));
                try {
                    setAlarmToServer(np.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push_switch.setChecked(false);
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    private void setAlarmToServer(final int time) throws Exception {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("time", String.valueOf(time));
        postParam.put("token", User.getInstance().getToken());
        postParam.put("userId", User.getInstance().getUserId());

        String URL = "http://52.41.19.232/setAlarm";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if (response.toString().contains("result")) {
                        if (response.getString("result").equals("fail")) {
                            Toast.makeText(getActivity(), "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (response.getString("result").equals("success")) {
                            Toast.makeText(getActivity(), "알람 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            TimerTask task = new TimerTask() {
                                public void run() {
                                    try {
                                        msg = handler.obtainMessage();
                                        msg.arg1 = 1;
                                        handler.sendMessage(msg);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            mTimer = new Timer();
                            mTimer.schedule(task, 1000, 1000);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }

    private void stopAlarmToServer() throws Exception {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("userId", User.getInstance().getUserId());

        String URL = "http://52.41.19.232/stopAlarm";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if (response.toString().contains("result")) {
                        if (response.getString("result").equals("fail")) {
                            Toast.makeText(getActivity(), "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (response.getString("result").equals("success")) {
                            Toast.makeText(getActivity(), "알람이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                            mTimer.cancel();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }

    private void getAlarmToServer() throws Exception {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("userId", User.getInstance().getUserId());

        String URL = "http://52.41.19.232/getAlarm";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loading.dismiss();
                try {
                    if (response.toString().contains("result")) {
                        if (response.getString("result").equals("fail")) {
                            Toast.makeText(getActivity(), "알 수 없는 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (response.getString("result").equals("success")) {
                            if(state) {
                                if(response.getString("alarm").equals("0")) {
                                    push_switch.setChecked(false);
                                    tv.setText("0");
                                }
                                else {
                                    push_switch.setChecked(true);
                                    tv.setText(response.getString("alarm"));
                                    Log.e("adsf", tv.getText().toString());
                                }

                                state = false;
                            }
                            else {
                                tv.setText(response.getString("alarm"));
                                TimerTask task = new TimerTask() {
                                    public void run() {
                                        try {
                                            msg = handler.obtainMessage();
                                            msg.arg1 = 1;
                                            handler.sendMessage(msg);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                mTimer = new Timer();
                                mTimer.schedule(task, 1000, 1000);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("development", "Error: " + error.getMessage());
                    }
                });
        volley.getInstance().addToRequestQueue(req);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                        if (tv.getText().toString().equals("1")) {
                            mTimer.cancel();
                            push_switch.setChecked(false);
                        }
                    tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) - 1));
                        break;

            }
            return true;
        }
    });
}
