package com.red.assistant.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.red.assistant.R;

/**
 * Created by tianxiying on 16/8/8.
 */
public class GSensitiveView extends ImageView {

    private Bitmap image;
    private double rotation = 0;
    private Paint paint;

    public GSensitiveView(Context context) {
        super(context);
        BitmapDrawable drawble = (BitmapDrawable) context.getResources().getDrawable(R.drawable.pointer1);
        image = drawble.getBitmap();

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        double w = image.getWidth();
        double h = image.getHeight();

        Rect rect = new Rect();
        getDrawingRect(rect);

        int degrees = (int) (180 * rotation / Math.PI);
        canvas.rotate(degrees, rect.width(), rect.height() / 2);//旋转中心为屏幕右边中部
        canvas.drawBitmap(image, //初始位置在屏幕右边中部
                (float) (rect.width() - w / 2),
                (float) ((rect.height()) / 2),
                paint);
    }

    public void setRotation(double rad) {
        rotation = rad;
        invalidate();
    }

}