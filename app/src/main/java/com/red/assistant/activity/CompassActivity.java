package com.red.assistant.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.red.assistant.R;

/**
 * Created by lipengzhao on 16/7/30.
 */
public class CompassActivity extends Activity implements SensorEventListener {
    private ImageView ivCompass;
    private float currentDegree = 0f;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ivCompass = (ImageView) findViewById(R.id.iv_compass);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(CompassActivity.this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    @SuppressWarnings("deprecation")
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];

            RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            ra.setDuration(200);
            ivCompass.startAnimation(ra);

            currentDegree = -degree;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
