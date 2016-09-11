package com.red.assistant.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.red.assistant.R;
import com.red.assistant.util.PhoneUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipengzhao on 16/7/30.
 */
public class PhoneInfoActivity extends Activity {
    private TelephonyManager phoneManager;

    private List<Map<String, Object>> list = new ArrayList<>();
    private ListView lvPhoneInfo;
    private TextView tvCPUMax;
    private TextView tvCPUMin;
    private TextView tvRAMMax;
    private TextView tvRAMUsed;

    String[] tvInfos = {"型号", "IMEI", "系统", "屏幕", "网络"};
    int[] ivInfos = {R.drawable.phone_icon_info_model, R.drawable.phone_icon_info_imei, R.drawable.phone_icon_info_system,
            R.drawable.phone_icon_info_screen, R.drawable.icon_phone_net};
    String[] infoDetails = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);
        phoneManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        initView();
        setPhoneInfoAdpater();
        getAndSetCPUInfo(tvCPUMax, tvCPUMin);
        getAndSetRAMInfo(tvRAMMax, tvRAMUsed);
    }

    private void initView() {
        lvPhoneInfo = (ListView) findViewById(R.id.lv_phone_info);
        tvCPUMax = (TextView) findViewById(R.id.tv_phone_cpu_max);
        tvCPUMin = (TextView) findViewById(R.id.tv_phone_cpu_min);
        tvRAMMax = (TextView) findViewById(R.id.tv_phone_ram_max);
        tvRAMUsed = (TextView) findViewById(R.id.tv_phone_ram_used);
        findViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setPhoneInfoAdpater() {
        infoDetails[0] = Build.MODEL; //型号

        infoDetails[1] = phoneManager.getDeviceId(); //IMEI
        infoDetails[2] = Build.VERSION.RELEASE; //系统

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = (int) (dm.widthPixels * dm.density / 3);
        int screenHeight = (int) (dm.heightPixels * dm.density /3);
        infoDetails[3] = "分辨率: " + screenWidth + " * " + screenHeight;

        infoDetails[4] = PhoneUtil.getProviderName(this);

        inflateListData();

        String[] from = {"iv_phone_info", "tv_phone_info", "tv_phone_info_detail"};
        int[] to = {R.id.iv_phone_info, R.id.tv_phone_info, R.id.tv_phone_info_detail};
        SimpleAdapter phoneInfoAdapter = new SimpleAdapter(this, list, R.layout.phone_lv_item,
                from, to);
        lvPhoneInfo.setAdapter(phoneInfoAdapter);
    }

    private void inflateListData() {
        for(int i = 0; i < tvInfos.length; i++){
            Map<String, Object> map = new HashMap<>();
            map.put("iv_phone_info", ivInfos[i]);
            map.put("tv_phone_info", tvInfos[i]);
            map.put("tv_phone_info_detail", infoDetails[i]);
            list.add(map);
        }
    }



    private void getAndSetCPUInfo(TextView tvMax, TextView tvMin) {
        // tvMax.setText(PhoneUtil.getCpuFreq(true));
        // tvMin.setText(PhoneUtil.getCpuFreq(false));
        tvMax.setText(getString(R.string.format_phone_cpu_max,
                Integer.valueOf(PhoneUtil.getCpuFreq(true)) / 1000));
        tvMin.setText(getString(R.string.format_phone_cpu_min,
                Integer.valueOf(PhoneUtil.getCpuFreq(false)) / 1000));
    }

    private void getAndSetRAMInfo(TextView tvMax, TextView tvUsed) {
        long maxMem = PhoneUtil.getTotalMem(this) / 1024; //GB
        long availMem = PhoneUtil.getAvailableMem(this) / 1024; //GB
        tvMax.setText(getString(R.string.format_phone_ram_max, (float) maxMem));
        long usedMem = maxMem - availMem;
        tvUsed.setText(getString(R.string.format_phone_ram_used, (int) usedMem));
    }

}