package com.red.assistant.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.red.assistant.R;
/**
 * Created by tianxiying on 16/8/10.
 */
@SuppressWarnings("ALL")
public class MyTorchActivity extends Activity {

    private Camera camera;
    private Camera.Parameters parameters;
    private boolean is_open_torch;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_torch);

        view = (View) findViewById(R.id.view_torch);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_open_torch) {
                    // 开启闪光灯（手电筒）
                    openTorch();
                } else {
                    // 关闭闪光灯
                    closeTorch();
                }
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
    protected void onPause() {
        // TODO Auto-generated method stub
        closeTorch();
        super.onStop();
    }

    // 打开闪光灯做手电筒
    private void openTorch() {
        camera = Camera.open();
        parameters = camera.getParameters();
        // 判断闪光灯是否存在
        if (parameters.getFlashMode() == null) {
            Toast.makeText(this, "本机没有闪光灯装置!", Toast.LENGTH_SHORT).show();
            return;
        }
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        is_open_torch = true;
        view.setBackgroundResource(R.drawable.toolbox_btn_light_bg_pressed);
    }

    // 关闭手电筒
    private void closeTorch() {
        if (camera != null) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
            camera = null;
            parameters = null;
        }
        is_open_torch = false;
        view.setBackgroundResource(R.drawable.toolbox_btn_light_bg);
    }

}

