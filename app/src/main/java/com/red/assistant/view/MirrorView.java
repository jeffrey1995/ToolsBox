package com.red.assistant.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
@SuppressWarnings("deprecation")
public class MirrorView extends SurfaceView {
    private static String LOG_TAG = MirrorView.class.getSimpleName();
    private List<Camera.Size> preSizeList = null;
    private SurfaceHolder mSurfaceHolder;
    private Camera camera;
    private Camera.Parameters params = null;
    private Context mContext;
    private int currentCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;

    public MirrorView(Context context) {
        this(context, null);
    }

    public MirrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MirrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void onResume() {
        openCamera();
    }
    public void onPause() {
        releaseCamera();
    }

    private void init() {
        mSurfaceHolder = getHolder();
        openCamera();
        params = camera.getParameters();
        preSizeList = params.getSupportedPreviewSizes();
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startPreview(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //设置合适的预览大小，避免预览界面的图像变形
                Camera.Size previewSize = getClosestPreSize(width, height, preSizeList);
                int previewWidth = previewSize.width;
                int previewHeight = previewSize.height;
                setPreviewSize(previewWidth, previewHeight);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.autoFocus(null);
                }
            }
        });

    }

    private void openCamera() {
        if (camera == null) {
            camera = Camera.open(currentCameraFacing);
        }
    }

    private void setPreviewSize(int width, int height) {
        params.setPreviewSize(width, height); // 设置预览大小
        camera.setParameters(params);
    }

    private void startPreview(SurfaceHolder surfaceHolder) {
        openCamera();
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(getPreviewDegree());
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于根据手机方向获得相机预览画面旋转的角度
     */
    private int getPreviewDegree() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay()
                .getRotation();
        int degree = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private Camera.Size getClosestPreSize(int surfaceWidth, int surfaceHeight,
                                          List<Camera.Size> preSizeList) {
        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        ReqTmpWidth = surfaceHeight;
        ReqTmpHeight = surfaceWidth;

        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        return retSize;
    }
}
