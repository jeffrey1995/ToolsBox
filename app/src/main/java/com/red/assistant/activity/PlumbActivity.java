package com.red.assistant.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.red.assistant.R;
import com.red.assistant.view.ZoomView;

public class PlumbActivity extends AppCompatActivity implements SensorEventListener {
    private GSensitiveView gsView;
    private ImageView camera_iv,plumb_bg_iv;
    private ZoomView zoomView;
    private SensorManager sm;
    private ImageView pointer_img;
    private float currentDegree = 0f;


    @Override
    protected void onDestroy() {
        sm.unregisterListener(this);
        super.onDestroy();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plumb);
        camera_iv = (ImageView) findViewById(R.id.camera_iv_);
        plumb_bg_iv = (ImageView) findViewById(R.id.plumb_bg_iv);
        zoomView = (ZoomView) findViewById(R.id.zoom_view);
        pointer_img = (ImageView) findViewById(R.id.pointer_img);

        zoomView.setVisibility(View.INVISIBLE);
        camera_iv.setBackgroundResource(R.drawable.common_btn_camera1);
        plumb_bg_iv.setBackgroundResource(R.drawable.toolbox_bg_plumb1);

        camera_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoomView.getVisibility() == View.VISIBLE) {
                    zoomView.setVisibility(View.INVISIBLE);
                    camera_iv.setBackgroundResource(R.drawable.common_btn_camera1);
                    plumb_bg_iv.setBackgroundResource(R.drawable.toolbox_bg_plumb1);
                } else {
                    zoomView.setVisibility(View.VISIBLE);
                    camera_iv.setBackgroundResource(R.drawable.common_btn_camera2);
                    plumb_bg_iv.setBackgroundResource(R.drawable.toolbox_bg_plumb2);
                }
            }
        });

//        gsView = new GSensitiveView(this);
//        setContentView(gsView);
//
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //通过公式导出重力g在xoy平面上的映射与坐标轴产生的角度rad(弧度)
            float[] values = event.values;
            float ax = values[0];
            float ay = values[1];

            double g = Math.sqrt(ax * ax + ay * ay);
            double cos = ay / g;
            if (cos > 1) {
                cos = 1;
            } else if (cos < -1) {
                cos = -1;
            }
            double rad = Math.acos(cos);
            if (ax < 0) {
                rad = 2 * Math.PI - rad;
            }

            int uiRot = getWindowManager().getDefaultDisplay().getRotation();
            double uiRad = Math.PI / 2 * uiRot;
            rad -= uiRad;
            Log.d("Rotation_change", rad * 180 / Math.PI + "");
//            gSensitiveView.setRotation(rad);
            float angle = (float) (rad * 180 / Math.PI);    //计算出偏离垂直中心的角度
            if (angle > 180) angle = 180;
            int width = pointer_img.getWidth();
            int height = pointer_img.getHeight();
            if ((angle - currentDegree) > 0.1) {    //设定角度变化的最低精度
                //指定旋转中心为顶部中间
                RotateAnimation ra = new RotateAnimation(currentDegree, angle - 90,
                        width,
                        height / 2);

                ra.setDuration(200);
                pointer_img.startAnimation(ra);

                currentDegree = angle - 90;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
