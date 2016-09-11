package com.red.assistant.fragment;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.red.assistant.R;
import com.red.assistant.activity.BatteryInfoActivity;
import com.red.assistant.activity.PhoneInfoActivity;
import com.red.assistant.activity.ScreenDetectActivity;
import com.red.assistant.view.KSCircleProgressBar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by tianxiying on 16/8/9.
 */
public class PhoneManagerFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "PhoneManagerFragment";
    private KSCircleProgressBar clear_pb;
    private TextView memory_ratio_tv;
    private RelativeLayout phone_info_rl, screen_detect_rl, battery_info_rl;
    private View view;
    private Intent intent;
    private Thread mThread;
    private long totalMemery = 0;
    private long availMemery = 0;
    private long getMemery = 0;
    public static final int UPDATEPROGRESSBAR = 0;
    public static final int UPDATEPROGRESSBAR_SIZE = 1;
    public static final int UPDATEPROGRESSBAR_SUCCESS = 2;

    public Handler handler = new Handler() { //更新清理进度条
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PhoneManagerFragment.UPDATEPROGRESSBAR_SIZE:  //首先获得任务总大小,方便计算任务比例
                    clear_pb.setMax(msg.arg1);
                    clear_pb.setProgress(0,1);
                    break;
                case PhoneManagerFragment.UPDATEPROGRESSBAR:    //更新任务比例
                    clear_pb.setProgress(msg.arg1,1);
                    break;
                case PhoneManagerFragment.UPDATEPROGRESSBAR_SUCCESS:    //任务成功后,将比例重置为内存占用比例
                    updateMemoryRatio(2);
                    Toast.makeText(getActivity(), "优化成功!", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone_manager, null);
        findView();
        Log.d(TAG, "可用内存" + getAvailMemory());
        Log.d(TAG, "总内存" + getTotalMemory());
        updateMemoryRatio(0);
        return view;
    }

    private void findView() {
        phone_info_rl = (RelativeLayout) view.findViewById(R.id.phone_info_rl);
        screen_detect_rl = (RelativeLayout) view.findViewById(R.id.screen_detect_rl);
        battery_info_rl = (RelativeLayout) view.findViewById(R.id.battery_info_rl);
        clear_pb = (KSCircleProgressBar) view.findViewById(R.id.clear_pb);
        memory_ratio_tv = (TextView) view.findViewById(R.id.memory_ratio_tv);

        phone_info_rl.setOnClickListener(this);
        screen_detect_rl.setOnClickListener(this);
        battery_info_rl.setOnClickListener(this);
        clear_pb.setOnClickListener(this);
    }

    private void updateMemoryRatio(int state) {
        getMemery = getAvailMemory() - availMemery + 10;
        availMemery = getAvailMemory();
        totalMemery = getTotalMemory();
        //在vivo手机上获取手机总内存出现错误,在这里设置一个虚拟数据
        if (totalMemery <= 0) {
            totalMemery = availMemery * 4;
        }
        clear_pb.setMax((int) totalMemery);
        if (state == 2) {
            clear_pb.setGetMemery((int) getMemery);
        }
        clear_pb.setProgress((int) (totalMemery - availMemery),state);
        memory_ratio_tv.setText((totalMemery - availMemery) + "MB / " + totalMemery + "MB");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_pb: //手机加速
                if (mThread == null || !mThread.isAlive()) {
                    mThread = new Thread(new MyThread());
                    mThread.start();
                } else {
                    Toast.makeText(getActivity(), "正在优化中...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phone_info_rl:   //手机信息
                intent = new Intent(getActivity(), PhoneInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.screen_detect_rl:     //屏幕检测
                intent = new Intent(getActivity(), ScreenDetectActivity.class);
                startActivity(intent);
                break;
            case R.id.battery_info_rl:      //电池信息
                intent = new Intent(getActivity(), BatteryInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    private long getAvailMemory() {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return mi.availMem / (1024 * 1024);// 将获取的内存大小规格化
    }

    private long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return initial_memory / (1024 * 1024);// Byte转换为MB，内存大小规格化
    }

    /**
     * 进程清理
     */
    class MyThread implements Runnable {

        @Override
        public void run() {
            ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
            Message message_01 = new Message();
            message_01.what = PhoneManagerFragment.UPDATEPROGRESSBAR_SIZE;
            message_01.arg1 = list.size();
            handler.sendMessage(message_01);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ActivityManager.RunningAppProcessInfo appInfo = list.get(i);

                    Log.i(TAG, "pid: " + appInfo.pid);
                    Log.i(TAG, "processName: " + appInfo.processName);
                    Log.i(TAG, "importance: " + appInfo.importance);

                    if (appInfo.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        String[] pkgList = appInfo.pkgList;
                        for (int j = 0; j < pkgList.length; j++) {
                            am.killBackgroundProcesses(pkgList[j]);
                        }
                    }
                    Message message_02 = new Message();
                    message_02.what = PhoneManagerFragment.UPDATEPROGRESSBAR;
                    message_02.arg1 = i+1;
                    handler.sendMessage(message_02);
                }
                Message message_03 = new Message();
                message_03.what = PhoneManagerFragment.UPDATEPROGRESSBAR_SUCCESS;
                handler.sendMessage(message_03);
            }
        }
    }
}
