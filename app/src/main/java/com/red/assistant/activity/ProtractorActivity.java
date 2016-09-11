package com.red.assistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.red.assistant.R;
import com.red.assistant.view.ZoomView;

public class ProtractorActivity extends AppCompatActivity {
    private ImageView camera_iv, protractor_iv;
    private ZoomView zoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protractor);
        camera_iv = (ImageView) findViewById(R.id.camera_iv);
        protractor_iv = (ImageView) findViewById(R.id.protractor_iv);
        zoomView = (ZoomView) findViewById(R.id.zoom_view);

        zoomView.setVisibility(View.INVISIBLE);
        camera_iv.setBackgroundResource(R.drawable.common_btn_camera1);
        protractor_iv.setBackgroundResource(R.drawable.toolbox_bg_protractor1);

        camera_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoomView.getVisibility() == View.VISIBLE) {
                    zoomView.setVisibility(View.INVISIBLE);
                    camera_iv.setBackgroundResource(R.drawable.common_btn_camera1);
                    protractor_iv.setBackgroundResource(R.drawable.toolbox_bg_protractor1);
                }
                else {
                    zoomView.setVisibility(View.VISIBLE);
                    camera_iv.setBackgroundResource(R.drawable.common_btn_camera2);
                    protractor_iv.setBackgroundResource(R.drawable.toolbox_bg_protractor2);
                }
            }
        });
    }
    @Override
    protected void onPostResume() {
        zoomView.onResume();
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        zoomView.onPause();
        super.onPause();
    }
}
