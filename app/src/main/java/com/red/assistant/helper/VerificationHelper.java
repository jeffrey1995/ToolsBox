package com.red.assistant.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.red.assistant.my_interface.IVerification;
import com.red.assistant.util.AppUtils;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: PocketMoney_Moonlighting
 * Package: com.cc.moonlighting.Helper
 * Created by zhangziqi on 6/12/15.
 */
public class VerificationHelper {

    private static final String HOST = "http://114.215.149.238/appDistribute.php";
    private static final String DEFAULT_URL = "http://app-distribute.oss-cn-qingdao.aliyuncs.com/default/shengji.apk";

    public static void checkVerificationState(Context context, final IVerification verification) {

        Map params = new HashMap(1);
        params.put("oper", "checkVerify");
        params.put("tag", AppUtils.getAppTag(context));

        Log.d("checkUpdate",AppUtils.getAppTag(context));


        NetworkHelper.getInstance().post(HOST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Map responseMap = new Gson().fromJson(new String(responseBody), Map.class);

                Log.d("checkUpdate",new String(responseBody));
                String errorCode = responseMap.get("errorCode").toString();
                String isVerifying = ((Map) responseMap.get("data")).get("isVerify").toString();

                if (errorCode.equals("0") && isVerifying.equals("0")) {
                    String updateUrl = (String) ((Map) responseMap.get("data")).get("updateUrl");
                    if (updateUrl == null) {
                        updateUrl = DEFAULT_URL;
                    }

                    verification.onNoVerification(updateUrl);
                } else {
                    verification.onVerification();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                verification.onVerification();
            }
        });
    }
}
