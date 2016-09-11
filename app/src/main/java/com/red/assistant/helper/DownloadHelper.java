package com.red.assistant.helper;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.red.assistant.R;

import org.apache.http.Header;

import java.io.File;

/**
 * Project: PocketMoney_Moonlighting
 * Package: com.cc.moonlighting.Helper
 * Created by zhangziqi on 6/12/15.
 */
public class DownloadHelper {
    public static void downloadFile(final Context context, final String url, final IDownloadListener listener) {
        Toast.makeText(context, "正在为您升级", Toast.LENGTH_SHORT).show();
        listener.onStart();
        makeDownloadDir();
        NetworkHelper.getInstance().get(url, new FileAsyncHttpResponseHandler(getStorePath()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                listener.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                listener.onSuccess(file);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                listener.onProgress(bytesWritten, totalSize);
            }
        });
    }

    private static File getStorePath() {
        return new File(getDownloadDirPath(), "com.app.pocketmoney.apk");
    }

    private static File getDownloadDirPath() {
        return new File(Environment.getExternalStorageDirectory(), "Download");
    }

    private static boolean makeDownloadDir() {
        return getDownloadDirPath().mkdirs();
    }
}
