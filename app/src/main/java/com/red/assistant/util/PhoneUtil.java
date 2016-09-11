package com.red.assistant.util;

import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lipengzhao on 16/8/1.
 */
public class PhoneUtil {
    /**
     * 获取当前可用内存
     * @param context
     * @return 当前可用内存(单位MB)
     */
    public static long getAvailableMem(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem / (1024 * 1024); // 单位--> MB?
    }

    /**
     * 获取当前手机的总内存
     * @param context
     * @return 返回当前手机的总内存(单位MB)
     */
    public static long getTotalMem(Context context) {
        String memInfoFile = "/proc/meminfo";// 系统内存信息文件
        String memInfo;

        long initialMem = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(memInfoFile));
            memInfo = reader.readLine();// 读取meminfo第一行，系统总内存大小

//            for (String num : memInfo.split("\\s+")) {
//                Log.i(str2, num + "\t");
//            }

            initialMem = Integer.valueOf(memInfo.split("\\s+")[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            reader.close();

        } catch (IOException e) {
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        return initialMem / (1024*1024);
    }

    /**
     * 获取CPU的最大/最小频率
     * @param isMax
     * @return isMax为true则返回最大频率,否则返回最小频率
     */
    public static String getCpuFreq(boolean isMax) {
        String freq = "";
        ProcessBuilder cmd;
        try {
            String[] args = new String[2];
            args[0] = "/system/bin/cat";
            if (isMax) {
                args[1] = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
            } else {
                args[1] = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
            }
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                freq += new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            freq = "N/A";
        }
        return freq.trim();
    }

    /**
     * 获取手机运营商名称
     * @param context
     * @return 手机运营商名称, 如"中国移动"
     */
    public static String getProviderName(Context context) {
        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = null;
        String IMSI = phoneManager.getSubscriberId();
        if (IMSI == null) {
            return "None";
        }
        // IMSI号前面3位460是国家，紧接着后面2位00/02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }
}
