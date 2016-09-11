package com.red.assistant.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.text.LoginFilter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by lipengzhao on 16/7/30.
 */
public class ClearActivity extends Activity {
    private static final String LOG_TAG = ClearActivity.class.getSimpleName();

    private long getAvailableMem(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem / (1024 * 1024); // 单位--> Mb?
    }

    private long getTotalMem(Context context) {
        String memInfoFile = "/proc/meminfo";// 系统内存信息文件
        String memInfo;

        long initialMem = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(memInfoFile));
            memInfo = reader.readLine();// 读取meminfo第一行，系统总内存大小


            initialMem = Integer.valueOf(memInfo.split("\\s+")[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            reader.close();

        } catch (IOException e) {
        }

        return initialMem / (1024*1024);
    }

    private void killUselessProc() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningAppProcessInfo appInfo = list.get(i);

                Log.i(LOG_TAG, "pid: " + appInfo.pid);
                Log.i(LOG_TAG, "processName: " + appInfo.processName);
                Log.i(LOG_TAG, "importance: " + appInfo.importance);

                if (appInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    String[] pkgList = appInfo.pkgList;
                    for (int j = 0; j < pkgList.length; j++) {
                        am.killBackgroundProcesses(pkgList[j]);
                    }
                }

            }
        }
    }

}
