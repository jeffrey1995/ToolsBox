package com.red.assistant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.red.assistant.R;
import com.red.assistant.view.ZoomView;

/**
 * Created by lipengzhao on 16/8/1.
 */
public class ZoomActivity extends Activity {
    private ZoomView mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mPreview = (ZoomView) findViewById(R.id.zoom_view);
        SeekBar sbZoom = (SeekBar) findViewById(R.id.sb_zoom);
        sbZoom.setMax(300);
        sbZoom.setProgress(0);
        sbZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                mPreview.setZoom(progress / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        findViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
