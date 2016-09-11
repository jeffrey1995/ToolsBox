package com.red.assistant.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.red.assistant.R;
import com.red.assistant.view.TorchView;

/**
 * Created by lipengzhao on 16/7/30.
 *
 * 该activity为手电筒功能,此方法在红米手机上无法运行,采取了另一种可行方法,见MyTorchActivity
 */
@SuppressWarnings("deprecation")
public class TorchActivity extends Activity {
    private TorchView torchView;
    private ImageView ivSwitch;
    private Camera c;
    private static boolean isTorchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);

        torchView = (TorchView) findViewById(R.id.torch_view);
        ivSwitch = (ImageView) findViewById(R.id.iv_switch);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            if (isTorchOn) {
                c.stopPreview();
                ivSwitch.setImageResource(R.drawable.toolbox_btn_light_bg_pressed);
                isTorchOn = false;
            } else {
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                Camera.Parameters p = c.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                c.setParameters(p);
                c.startPreview();
                ivSwitch.setImageResource(R.drawable.toolbox_btn_light_bg_pressed);
                isTorchOn = true;
            }
        }
        return super.onTouchEvent(event);
    }

}
