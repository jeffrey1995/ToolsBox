package com.red.assistant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.red.assistant.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lipengzhao on 16/8/1.
 */
public class CalcActivity extends Activity {
    private final String TAG = "MainActivity";
    private final int STATE_INIT = 0;           //输入第一个数字
    private final int STATE_EDITING = 1;        //数字编辑阶段
    private final int STATE_RESULT = 2;         //返回结果状态
    List<StringBuilder> list;

    private int state;                                //输入框的状态
    private double result;
    private double cacheCount;
    char operate;
    TextView etDisplay;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        init();
    }

    /**
     * 程序初始化
     */
    private void init() {
        etDisplay = (TextView) findViewById(R.id.et_display);
        list = new LinkedList<>();
        result = 0;
        cacheCount = 0;
        operate = '+';
        state = STATE_INIT;
        sb = new StringBuilder();
        etDisplay.setText(sb);
        findViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_0:
                updateData('0');
                break;
            case R.id.btn_1:
                updateData('1');
                break;
            case R.id.btn_2:
                updateData('2');
                break;
            case R.id.btn_3:
                updateData('3');
                break;
            case R.id.btn_4:
                updateData('4');
                break;
            case R.id.btn_5:
                updateData('5');
                break;
            case R.id.btn_6:
                updateData('6');
                break;
            case R.id.btn_7:
                updateData('7');
                break;
            case R.id.btn_8:
                updateData('8');
                break;
            case R.id.btn_9:
                updateData('9');
                break;
            case R.id.btn_point:
                updateData('.');
                break;
            case R.id.btn_delete:
                updateData('d');
                break;
            case R.id.btn_add:
                updateData('+');
                break;
            case R.id.btn_minus:
                updateData('-');
                break;
            case R.id.btn_multiply:
                updateData('*');
                break;
            case R.id.btn_divide:
                updateData('/');
                break;
            case R.id.btn_equal:
                updateData('=');
                break;
            case R.id.tv_init:
                init();
                break;
        }
    }

    /**
     * 每通过按钮　传入该方法一个字符
     * 实现对数据的更新
     */
    public void updateData(char ch){
        if ((ch <= '9' && ch >= '0') || ch == '.') {
            sb.append(ch);
            etDisplay.setText(sb);
        }
        switch (ch){
            case 'd':                                           //删除
                if (sb.length() == 0) {
                    break;
                }
                sb.deleteCharAt(sb.length() - 1);
                etDisplay.setText(sb);
                break;
            case '+':
                if (sb.length() == 0){
                    init();
                    break;
                }
                cacheCount = Double.parseDouble(sb.toString());
                operator();
                operate = '+';
                break;
            case '-':
                if (sb.length() == 0) {
                    init();
                    break;
                }
                cacheCount = Double.parseDouble(sb.toString());
                operator();
                operate = '-';
                break;
            case '*':
                if (sb.length() == 0) {
                    init();
                    break;
                }
                cacheCount = Double.parseDouble(sb.toString());
                operator();
                operate = '*';
                break;
            case '/':
                if (sb.length() == 0){
                    init();
                    break;
                }
                cacheCount = Double.parseDouble(sb.toString());
                operator();
                operate = '/';
                break;
            case '=':
                if (sb.length()==0) {
                    init();
                    break;
                }
                cacheCount = Double.parseDouble(sb.toString());
                operator();
                operate = '+';
                cacheCount = 0;
                result = 0;
                break;
        }
        Log.d(TAG,cacheCount + "#" + result);
    }

    private void operator(){
        String str;
        switch (operate){
            case '+':
                result += cacheCount;
                str = String.valueOf(result);
                sb = new StringBuilder();
                //       state=STATE_RESULT;
                etDisplay.setText(str);
                break;
            case '-':
                result -= cacheCount;
                str = String.valueOf(result);
                sb = new StringBuilder();
                //       state=STATE_RESULT;
                etDisplay.setText(str);
                break;
            case '/':
                if (cacheCount == 0) {
                    etDisplay.setText("除零错误");
                    init();
                    break;
                }
                result /= cacheCount;
                str = String.valueOf(result);
                sb = new StringBuilder();
                //       state=STATE_RESULT;
                etDisplay.setText(str);
                break;
            case '*':
                result *= cacheCount;
                str = String.valueOf(result);
                sb = new StringBuilder();
                //       state=STATE_RESULT;
                etDisplay.setText(str);
                break;
            case '=':
                result += cacheCount;
                str = String.valueOf(result);
                sb = new StringBuilder();
                //       state=STATE_RESULT;
                etDisplay.setText(str);
                cacheCount = 0;
                result = 0;
                break;
        }
    }

    /**
     * 检测状态
     * @param v
     */
    public void checkIsResult(View v ){
        if (state == STATE_RESULT){
            init();
            onClick(v);
        }
    }

}
