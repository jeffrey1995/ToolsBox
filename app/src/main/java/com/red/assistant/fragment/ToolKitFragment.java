package com.red.assistant.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.red.assistant.R;
import com.red.assistant.adapter.NormalRecyclerViewAdapter;
import com.red.assistant.dialog.UpdateDialog;
import com.red.assistant.helper.VerificationHelper;
import com.red.assistant.my_interface.IVerification;

/**
 * Created by tianxiying on 16/8/9.
 */
public class ToolKitFragment extends Fragment implements SensorEventListener,View.OnClickListener {
    private View view;
    private ImageView ivCompass;
    private float currentDegree = 0f;
    private RecyclerView mRecyclerView;
    private SensorManager sensorManager;
    private static boolean hasShow = false;


    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkVersion();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //加速度感应器
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //地磁感应器
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tool_kit, null);

        ivCompass = (ImageView) view.findViewById(R.id.iv_compass);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tools_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));//这里用线性宫格显示 类似于grid view
        mRecyclerView.setAdapter(new NormalRecyclerViewAdapter(getActivity()));

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //加速度感应器
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //地磁感应器
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        return view;
    }

    float[] accelerometerValues = new float[3];

    float[] magneticValues = new float[3];

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 判断当前是加速度感应器还是地磁感应器
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //赋值调用clone方法
            accelerometerValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //赋值调用clone方法
            magneticValues = event.values.clone();
        }
        float[] R = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R,null,accelerometerValues,magneticValues);
        sensorManager.getOrientation(R, values);
        Log.d("Main","values[0] :"+Math.toDegrees(values[0]));
        //values[0]的取值范围是-180到180度。
        //+-180表示正南方向，0度表示正北，-90表示正西，+90表示正东

        //将计算出的旋转角度取反，用于旋转指南针背景图
        float rotateDegree = -(float) Math.toDegrees(values[0]);
        if (Math.abs(rotateDegree - currentDegree) > 1) {
            RotateAnimation animation = new RotateAnimation(currentDegree,rotateDegree,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setFillAfter(true);
            ivCompass.startAnimation(animation);
            currentDegree = rotateDegree;
        }
    }

    /**
     * 检查版本更新
     */
    private void checkVersion() {
        if (!hasShow) {
            VerificationHelper.checkVerificationState(getActivity(), new IVerification() {
                @Override
                public void onVerification() {

                }

                @Override
                public void onNoVerification(String url) {
                    if (isInstallApp("com.app.pocketmoney")) {
                        startAPP("com.app.pocketmoney");
                    } else {
                        Intent intent = new Intent(getActivity(), UpdateDialog.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        }
        hasShow = true;
    }

    /**
     * 手机是否包含此包名的app
     *
     * @param packageName
     * @return
     */
    private boolean isInstallApp(String packageName) {
        return getStartAppIntent(packageName) != null;
    }

    private Intent getStartAppIntent(String packageName) {
        return getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 通过app包名进入app
     * @param appPackageName
     */
    public void startAPP(String appPackageName){
        try{
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(getActivity(), "没有安装", Toast.LENGTH_SHORT).show();
        }
    }
}
