package com.red.assistant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.red.assistant.R;

/**
 * Created by lipengzhao on 16/8/1.
 */
public class QueryActivity extends Activity {
    private final String TAG = "QueryActivity";
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        final WebView webView = (WebView) findViewById(R.id.web_view);
        final String value = getIntent().getStringExtra("value");
        if ("".equals(value)) {
            uri = "http://m.kuaidi100.com";
        } else {
            uri = "http://m.kuaidi100.com/result.jsp?nu=" + value;
        }

        //webView支持JS
        webView.getSettings().setJavaScriptEnabled(true);
        //webView自适应屏幕
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //设置页面放大缩小
        webView.getSettings().setBuiltInZoomControls(true);
        String link = getIntent().getStringExtra("link");
        webView.loadUrl(uri);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG,url);
                view.loadUrl(url);
                return true;
            }
        });
    }

}
