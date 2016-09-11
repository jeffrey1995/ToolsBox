package com.red.assistant.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;


public class AppUtils {

    public static String getAppTag(Context context) {
        return getAppSource(context) + "v" + getAppVersion(context);
    }

    public static String getAppVersion(Context context) {
        try {
            return context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static String getAppSource(Context context) {
        try {
            return context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("TD_CHANNEL_ID");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static void uninstallApp(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }
}
