package com.red.assistant.helper;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: PocketMoney_Moonlighting
 * Package: com.cc.moonlighting.Network
 * Created by zhangziqi on 6/12/15.
 */
public class NetworkHelper {

    private static NetworkHelper networkHelper;
    private AsyncHttpClient client;

    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    public static NetworkHelper getInstance() {
        if (networkHelper == null) {
            init();
        }
        return networkHelper;
    }

    public void get(String url, AsyncHttpResponseHandler handler) {
        client.get(url, handler);
    }

    public void post(String url, Map params, AsyncHttpResponseHandler handler) {
        client.post(url, networkHelper.convertMapToRequestParams(params), handler);
        client.setEnableRedirects(true);
    }

    public void cancel() {
        client.cancelAllRequests(true);
    }

    private static void init() {
        if (networkHelper == null) {
            networkHelper = new NetworkHelper();
        }
    }

    private NetworkHelper() {
        client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
        client.setAuthenticationPreemptive(true);
    }

    private RequestParams convertMapToRequestParams(final Map map) {
        if (map != null) {
            String q = new Gson().toJson(map);

            Map<String, String> params = new HashMap<>(2);
            params.put("q", q);

            return new RequestParams(params);
        } else {
            return null;
        }
    }
}
