package com.red.assistant.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.red.assistant.R;
import com.red.assistant.view.MirrorView;

/**
 * Created by lipengzhao on 16/7/30.
 */
@SuppressWarnings("deprecation")
public class MirrorActivity extends AppCompatActivity {
    private MirrorView mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
        mPreview = (MirrorView) findViewById(R.id.mirror_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
    }
}
