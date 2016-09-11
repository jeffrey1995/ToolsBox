package com.red.assistant.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.red.assistant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLUE;

/**
 * Created by lipengzhao on 16/7/30.
 */
public class BatteryInfoActivity extends Activity {
    private static final String LOG_TAG = BatteryInfoActivity.class.getSimpleName();
    private List<Map<String, Object>> funcList;

    private TextView tvBatteryRemain;
    private TextView tvTimeLeft;
    private ListView lvBatteryInfo;

    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_info);
        tvBatteryRemain = (TextView) findViewById(R.id.tv_battery_remain);
        tvTimeLeft = (TextView) findViewById(R.id.tv_time_left);
        lvBatteryInfo = (ListView) findViewById(R.id.lv_battery_info);
        funcList = new ArrayList<Map<String, Object>>();
        findViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBatteryLevel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void getBatteryLevel() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    tvBatteryRemain = (TextView) BatteryInfoActivity.this.findViewById(R.id.tv_battery_remain);
                    int level = intent.getIntExtra("level", 0);
                    tvBatteryRemain.setTextSize(38);
                    tvBatteryRemain.setText(getString(R.string.format_battery_remain, level));
                    int timeLeft = level * 1043 / 100; //剩余使用时间(单位:分钟)
                    int hourLeft = timeLeft / 60;
                    int minuteLeft = timeLeft % 60;
                    tvTimeLeft.setText(getString(R.string.format_battery_time_left, hourLeft, minuteLeft));
                    initList(timeLeft);
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void initList(int timeLeft) {
        int[] timeLefts = {timeLeft * 411 / 782, timeLeft * 318 / 782, timeLeft * 342 / 782,
                        timeLeft * 789 / 782, timeLeft * 1233 / 782};
        String[] times = new String[5];
        for (int i = 0; i < timeLefts.length; i++) {
            times[i] = getString(R.string.format_detail_time_left, timeLefts[i] / 60, timeLefts[i] % 60);
        }

        int[] icons = {R.drawable.phone_icon_battery_call, R.drawable.phone_icon_battery_internet, R.drawable.phone_icon_battery_video,
                        R.drawable.phone_icon_battery_music, R.drawable.phone_icon_battery_down};
        String[] items = {"通话", "上网", "视频", "音乐", "待机"};

        String[] from = {"iv_remain", "tv_remain", "tv_time"};

        int[] to = {R.id.iv_remain, R.id.tv_remain, R.id.tv_time};

        funcList.clear();
        for (int i = 0; i < items.length; i++) {
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("iv_remain", icons[i]);
            map.put("tv_remain", items[i]);
            map.put("tv_time", times[i]);
            funcList.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(BatteryInfoActivity.this, funcList, R.layout.battery_lv_item,
                from, to);

        lvBatteryInfo.setAdapter(simpleAdapter);
    }
}
