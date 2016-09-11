package com.red.assistant.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.red.assistant.R;

/**
 * Created by lipengzhao on 16/7/30.
 */
public class ScreenDetectActivity extends Activity {
    private boolean detecting = false;
    private int state = 0; //决定了处于"检测中"的时候,当前屏幕应为什么颜色的纯色

    private final int NEXT_BLUE = 1;
    private final int NEXT_BLACK = 2;
    private final int NEXT_WHITE = 3;
    private final int NEXT_YELLOW = 4;
    private final int NEXT_GREEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_detect);
        final Button btnStart = (Button) findViewById(R.id.btn_start);
        final TextView tvDetect = (TextView) findViewById(R.id.tv_detect_hint);
        final LinearLayout llDetecting = (LinearLayout) findViewById(R.id.ll_detecting);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setVisibility(View.INVISIBLE);
                tvDetect.setVisibility(View.INVISIBLE);
                llDetecting.setBackgroundColor(Color.RED);
                state++;
                detecting = true;
            }
        });

        llDetecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detecting) {
                    switch (state) {
                        case NEXT_BLUE:
                            llDetecting.setBackgroundColor(Color.BLUE);
                            state++;
                            break;
                        case NEXT_BLACK:
                            llDetecting.setBackgroundColor(Color.BLACK);
                            state++;
                            break;
                        case NEXT_WHITE:
                            llDetecting.setBackgroundColor(Color.WHITE);
                            state++;
                            break;
                        case NEXT_YELLOW:
                            llDetecting.setBackgroundColor(Color.YELLOW);
                            state++;
                            break;
                        case NEXT_GREEN:
                            llDetecting.setBackgroundColor(Color.GREEN);
                            state++;
                            break;
                        default:
                            finish();
                    }
                }
            }
        });
    }
}
