package com.red.assistant.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.red.assistant.R;
import com.red.assistant.helper.DownloadHelper;
import com.red.assistant.helper.IDownloadListener;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;

/**
 * Project: PocketMoney_Moonlighting
 * Package: com.cc.moonlighting.Fragment
 * Created by zhangziqi on 6/12/15.
 */
public class UpdateDialog extends Activity implements View.OnClickListener{

    private String url;


    private ImageButton close;


    private Button update;


    private ProgressBar progress;


    private void onCloseClick() {
        finish();
        TCAgent.onEvent(this, "CLOSE_UPDATE_DIALOG");
    }

    private void onUpdateClick() {
        TCAgent.onEvent(this, "CLICK_UPDATE_BUTTON");
        DownloadHelper.downloadFile(UpdateDialog.this, url,
                new IDownloadListener() {
                    @Override
                    public void onStart() {
                        update.setVisibility(View.INVISIBLE);
                        close.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        progress.setProgress(0);
                    }

                    @Override
                    public void onProgress(long written, long total) {
                        progress.setProgress((int) ((float) written / (float) total * 100));
                    }

                    @Override
                    public void onSuccess(File file) {
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(installIntent);
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        update.setVisibility(View.VISIBLE);
                        close.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.INVISIBLE);
                        progress.setProgress(0);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.dialog_update);
        url = getIntent().getStringExtra("url");
        findView();
    }

    private void findView() {
        close = (ImageButton) findViewById(R.id.close);
        update = (Button) findViewById(R.id.update);
        progress = (ProgressBar) findViewById(R.id.progress);
        close.setOnClickListener(this);
        update.setOnClickListener(this);
        progress.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                onCloseClick();
                break;
            case R.id.update:
                onUpdateClick();
                break;
        }
    }
}
