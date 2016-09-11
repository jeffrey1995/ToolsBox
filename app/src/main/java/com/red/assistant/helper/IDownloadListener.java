package com.red.assistant.helper;

import java.io.File;

/**
 * Project: PocketMoney_Moonlighting
 * Package: com.cc.moonlighting.Interface
 * Created by zhangziqi on 6/12/15.
 */
public interface IDownloadListener {

    void onStart();

    void onProgress(long written, long total);

    void onSuccess(File file);

    void onFailure();

}
