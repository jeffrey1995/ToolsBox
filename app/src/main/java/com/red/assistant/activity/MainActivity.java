package com.red.assistant.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.red.assistant.R;
import com.red.assistant.fragment.LivingHelperFragment;
import com.red.assistant.fragment.PhoneManagerFragment;
import com.red.assistant.fragment.ToolKitFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipengzhao on 16/7/29.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private List<Map<String, Object>> funcList;
    private SimpleAdapter mainAdapter;

    private LinearLayout tool_kit_ll, phone_manager_ll, living_helper_ll;
    private ImageView tool_kit_iv, phone_manager_iv, living_helper_iv;
    private TextView tool_kit_tv, phone_manager_tv, living_helper_tv;

    private Fragment currentFragment, toolKitFragment, phoneManagerFragment, livingHelperFragment;
    private FragmentManager mFragmentManager;


    private int[] icon = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private String[] iconName = {"指南针", "铅锤", "镜子", "放大镜", "手电筒", "检测屏幕", "量角器",
            "手机信息", "电池信息", "计算器", "手机加速", "快递查询", "地图"};
    private String[] acts = {"CompassActivity", "PlumbActivity", "MirrorActivity", "ZoomActivity",
            "TorchActivity", "ScreenDetectActivity", "ProtractorActivity", "PhoneInfoActivity", "BatteryInfoActivity",
            "CalcActivity", "ClearActivity", "QueryActivity", "MapActivity"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        getFragment();
        changeTabStyle(0);
//        GridView gvMain = (GridView) findViewById(R.id.gv_main);
//
//        funcList = new ArrayList<>();
//        inflateListData();
//
//        String[] from ={"iv_shortcut","tv_shortcut"};
//        int[] to = {R.id.iv_shortcut, R.id.tv_shortcut};
//        mainAdapter = new SimpleAdapter(this, funcList, R.layout.main_gv_item, from, to);
//        gvMain.setAdapter(mainAdapter);
//        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                startDetailActivity(acts[i]);
//            }
//        });
    }

    private void findView() {
        tool_kit_ll = (LinearLayout) findViewById(R.id.tool_kit_ll);
        phone_manager_ll = (LinearLayout) findViewById(R.id.phone_manager_ll);
        living_helper_ll = (LinearLayout) findViewById(R.id.living_helper_ll);
        tool_kit_ll.setOnClickListener(this);
        phone_manager_ll.setOnClickListener(this);
        living_helper_ll.setOnClickListener(this);

        tool_kit_iv = (ImageView) findViewById(R.id.tool_kit_iv);
        phone_manager_iv = (ImageView) findViewById(R.id.phone_manager_iv);
        living_helper_iv = (ImageView) findViewById(R.id.living_helper_iv);

        tool_kit_tv = (TextView) findViewById(R.id.tool_kit_tv);
        phone_manager_tv = (TextView) findViewById(R.id.phone_manager_tv);
        living_helper_tv = (TextView) findViewById(R.id.living_helper_tv);

        tool_kit_iv.setOnClickListener(this);
        phone_manager_iv.setOnClickListener(this);
        living_helper_iv.setOnClickListener(this);

        tool_kit_tv.setOnClickListener(this);
        phone_manager_tv.setOnClickListener(this);
        living_helper_tv.setOnClickListener(this);
    }

    /**
     * 实例化Fragment
     */
    private void getFragment() {
        toolKitFragment = new ToolKitFragment();
        phoneManagerFragment = new PhoneManagerFragment();
        livingHelperFragment = new LivingHelperFragment();
        mFragmentManager = getFragmentManager();
        if (mFragmentManager.beginTransaction().isEmpty()) {
            mFragmentManager.beginTransaction().add(R.id.main_content_ll, toolKitFragment).commit();
        }
        currentFragment = toolKitFragment;
    }

    /**
     * 切换fragment
     *
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (currentFragment != to) {
            currentFragment = to;
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from);
                transaction.add(R.id.main_content_ll, to);
                transaction.commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from); // 隐藏当前的fragment，显示下一个
                transaction.show(to);
                transaction.commit();
            }
        }
    }

    private void startDetailActivity(String actName) {
//        try {
//            Intent i = new Intent(this, Class.forName(actName));
//            startActivity(i);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        Intent i = new Intent();
        i.setClassName("com.red.assistant", "com.red.assistant.activity." + actName);
        startActivity(i);
    }

    public void inflateListData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("iv_shortcut", icon[i]);
            map.put("tv_shortcut", iconName[i]);
            funcList.add(map);
        }
    }

    /**
     * 更改主页底部Tab样式
     *
     * @param index
     */
    @SuppressWarnings("deprecation")
    private void changeTabStyle(int index) {
        boolean f = false, i = false, m = false;
        switch (index) {
            case 0:
                f = true;
                break;
            case 1:
                i = true;
                break;
            case 2:
                m = true;
                break;
        }

        Resources rs = getResources();
        if (f) {
            tool_kit_iv.setImageResource(R.drawable.tab_btn_toolbox_pressed);
            tool_kit_tv.setTextColor(rs.getColor(R.color.black));
        } else {
            tool_kit_iv.setImageResource(R.drawable.tab_btn_toolbox);
            tool_kit_tv.setTextColor(rs.getColor(R.color.darkgrey));
        }

        if (i) {
            phone_manager_iv.setImageResource(R.drawable.tab_btn_phone_pressed);
            phone_manager_tv.setTextColor(rs.getColor(R.color.black));
        } else {
            phone_manager_iv.setImageResource(R.drawable.tab_btn_phone);
            phone_manager_tv.setTextColor(rs.getColor(R.color.darkgrey));
        }

        if (m) {
            living_helper_iv.setImageResource(R.drawable.tab_btn_life_pressed);
            living_helper_tv.setTextColor(rs.getColor(R.color.black));
        } else {
            living_helper_iv.setImageResource(R.drawable.tab_btn_life);
            living_helper_tv.setTextColor(rs.getColor(R.color.darkgrey));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tool_kit_ll:
                Log.d("click!", "0");
                switchContent(currentFragment, toolKitFragment);
                changeTabStyle(0);
                break;
            case R.id.phone_manager_ll:
                Log.d("click!", "1");
                switchContent(currentFragment, phoneManagerFragment);
                changeTabStyle(1);
                break;
            case R.id.living_helper_ll:
                Log.d("click!", "2");
                switchContent(currentFragment, livingHelperFragment);
                changeTabStyle(2);
                break;
        }
    }

}
