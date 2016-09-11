/*
* 自定义环形进度条 KSCircleProgressBar
*
* Author:KevinStudio
* mailto:kevinstudiohelp@gmail.com
*/
package com.red.assistant.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class KSCircleProgressBar extends View {
    public int max = 360;// 最大进度值
    public int progress = 0;// 当前进度值
    public float startAngle = -90;// 进度条从什么角度开始
    private int myWidth = 0;// view宽度
    private int myHeight = 0;// view高度
    private int circleSize = 10;// 进度环的宽度
    private int textSize = 35;// 进度文本大小
    private int frontCircleColor = Color.parseColor("#22ecf3");// 进度环前端颜色
    private int backCircleColor = Color.parseColor("#233142");// 进度环后端颜色
    private int textColor = Color.WHITE;// 进度文本字体颜色
    private String progressInfo = ""; // 进度信息 可自定义文本信息 默认如90%
    private boolean isShowProgressText = true; // 是否显示进度信息
    private boolean isCustomProgressInfo = false;// 是否使用自定义进度文本
    private int state = 0;  //进度状态,默认为0:未开始.1:进行中.2:完成.

    public int getGetMemery() {
        return getMemery;
    }

    public void setGetMemery(int getMemery) {
        this.getMemery = getMemery;
    }

    private int getMemery = 0; //优化的大小

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = null;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int circleSize) {
        paint.setStrokeWidth(circleSize);
        this.circleSize = circleSize;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        txtPaint.setTextSize(textSize);
    }

    public String getProgressInfo() {
        return progressInfo;
    }

    public void setProgressInfo(String progressInfo) {
        this.progressInfo = progressInfo;
    }

    public void setProgress(int progress,int state) {
        this.progress = progress;
        this.state = state;
        invalidate();
    }

    public KSCircleProgressBar(Context context) {
        super(context);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(circleSize);
        paint.setAntiAlias(true);

        txtPaint.setStyle(Style.STROKE);
        txtPaint.setTextSize(textSize);
        txtPaint.setAntiAlias(true);
    }

    public KSCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(circleSize);
        paint.setAntiAlias(true);

        txtPaint.setStyle(Style.STROKE);
        txtPaint.setTextSize(textSize);
        txtPaint.setAntiAlias(true);

        max = attrs.getAttributeIntValue(null, "max", 360);
        progress = attrs.getAttributeIntValue(null, "progress", 0);
    }

    public KSCircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(circleSize);
        paint.setAntiAlias(true);

        txtPaint.setStyle(Style.STROKE);
        txtPaint.setTextSize(textSize);
        txtPaint.setAntiAlias(true);
        max = attrs.getAttributeIntValue(null, "max", 360);
        progress = attrs.getAttributeIntValue(null, "progress", 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
// TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            myWidth = w;
            myHeight = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
// 绘制后端背景进度环
        paint.setColor(backCircleColor);
        canvas.drawCircle(myWidth / 2, myHeight / 2,
                (myWidth > myHeight ? myHeight : myWidth) / 2 - circleSize / 2,
                paint);

// 绘制前端进度环
        paint.setColor(frontCircleColor);
        if (rectF == null) {
            rectF = new RectF();
        }
        rectF.left = circleSize / 2;
        rectF.right = myWidth - circleSize / 2;
        rectF.top = circleSize / 2;
        rectF.bottom = myHeight - circleSize / 2;
        if (progress != 0) {
            canvas.drawArc(rectF, startAngle, 360f * progress / max, false,
                    paint);
        }

// 绘制进度信息
        if (!isCustomProgressInfo) {
            if (state == 0) {
                progressInfo = progress * 100 / max + "%" + "\n" + "点击优化";
            }
            else if (state == 1) {
                progressInfo = progress * 100 / max + "%" + "\n" + "优化中...";
            }
            else if (state == 2) {
                progressInfo = getMemery + "MB" + "\n" + "已优化";
            }
        }
        if (isShowProgressText) {
            txtPaint.setColor(textColor);
            float l = txtPaint.measureText(progressInfo);
            if (l > myWidth - circleSize * 2) {
                txtPaint.setTextSize(txtPaint.getTextSize()
                        * (myWidth - circleSize * 2) / l * 0.9f);
                l = txtPaint.measureText(progressInfo);
            }
            canvas.drawText(progressInfo, myWidth / 2 - l / 2, myHeight / 2
                    + txtPaint.getTextSize() / 2, txtPaint);
        }
    }

    public boolean isShowProgressText() {
        return isShowProgressText;
    }

    public void setShowProgressText(boolean isShowProgressText) {
        this.isShowProgressText = isShowProgressText;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isCustomProgressInfo() {
        return isCustomProgressInfo;
    }

    public void setCustomProgressInfo(boolean isCustomProgressInfo) {
        this.isCustomProgressInfo = isCustomProgressInfo;
    }

    public int getFrontCircleColor() {
        return frontCircleColor;
    }

    public void setFrontCircleColor(int frontCircleColor) {
        this.frontCircleColor = frontCircleColor;
    }

    public int getBackCircleColor() {
        return backCircleColor;
    }

    public void setBackCircleColor(int backCircleColor) {
        this.backCircleColor = backCircleColor;
    }
}
