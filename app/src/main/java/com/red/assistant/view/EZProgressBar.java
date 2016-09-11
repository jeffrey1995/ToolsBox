package com.red.assistant.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.red.assistant.R;


/**
 * Project: PocketMoney_Moonlighting
 * Package: com.cc.moonlighting.Widget
 * Created by zhangziqi on 6/12/15.
 */
public class EZProgressBar extends android.widget.ProgressBar {

    private String progressText;
    private Paint paint;
    private int textSize = 18;

    public EZProgressBar(Context context) {
        this(context, null);
    }

    public EZProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EZProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        initProgressText();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EZProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs, defStyleAttr);
        initProgressText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressText(canvas);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.EZProgressBar, defStyleAttr, 0);

        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.EZProgressBar_textSize) {
                textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
            }
        }
    }

    private void drawProgressText(Canvas canvas) {
        if (canvas != null) {
            Rect rect = new Rect();
            paint.getTextBounds(progressText, 0, progressText.length(), rect);
            int x = (getWidth() / 2) - rect.centerX();
            int y = (getHeight() / 2) - rect.centerY();
            canvas.drawText(progressText, x, y, paint);
        }
    }

    private void initProgressText() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(textSize);
        paint.setColor(Color.WHITE);
    }

    private void setText(int progress) {
        int i = (progress * 100) / getMax();
        progressText = String.valueOf(i) + "%";
    }
}
