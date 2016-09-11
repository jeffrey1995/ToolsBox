package com.red.assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.red.assistant.R;
import com.red.assistant.activity.CalcActivity;
import com.red.assistant.activity.MirrorActivity;
import com.red.assistant.activity.MyTorchActivity;
import com.red.assistant.activity.PlumbActivity;
import com.red.assistant.activity.ProtractorActivity;
import com.red.assistant.activity.TorchActivity;
import com.red.assistant.activity.ZoomActivity;

/**
 * Created by tianxiying on 16/8/8.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private Intent intent;
    private String[] mTitles = {"铅锤", "放大镜", "手电筒", "计算器", "量角器", "镜子"};
    private int[] bg_img = {R.drawable.toolbox_btn_plumb, R.drawable.toolbox_btn_magnifier, R.drawable.toolbox_btn_light,
            R.drawable.toolbox_btn_calculator, R.drawable.toolbox_btn_protractor, R.drawable.toolbox_btn_mirror};

    public NormalRecyclerViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_tools, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
        holder.item_bg_iv.setBackgroundResource(bg_img[position]);
        if (position == 0 || position == 1) {
            holder.item_bg_rl.setBackgroundColor(Color.parseColor("#3cbca8"));
            holder.item_bar_rl.setBackgroundColor(Color.parseColor("#23a189"));
        }
        if (position == 2 || position == 3) {
            holder.item_bg_rl.setBackgroundColor(Color.parseColor("#ffac33"));
            holder.item_bar_rl.setBackgroundColor(Color.parseColor("#ff8e1d"));
        }
        if (position == 4 || position == 5) {
            holder.item_bg_rl.setBackgroundColor(Color.parseColor("#54c3df"));
            holder.item_bar_rl.setBackgroundColor(Color.parseColor("#36abd0"));
        }
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView item_bg_iv;
        RelativeLayout item_bg_rl, item_bar_rl;
        @SuppressWarnings("deprecation")
        NormalTextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.item_title_tv);
            item_bg_iv = (ImageView) view.findViewById(R.id.item_bg_iv);
            item_bg_rl = (RelativeLayout) view.findViewById(R.id.item_bg_rl);
            item_bar_rl = (RelativeLayout) view.findViewById(R.id.item_bar_rl);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                    switch(getPosition()) {
                        case 0:     //铅锤
                            intent = new Intent(mContext,PlumbActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 1:     //放大镜
                            intent = new Intent(mContext,ZoomActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 2:     //手电筒
                            intent = new Intent(mContext,MyTorchActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 3:     //计算器
                            intent = new Intent(mContext, CalcActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 4:     //量角器
                            intent = new Intent(mContext, ProtractorActivity.class);
                            mContext.startActivity(intent);
                            break;
                        case 5:     //镜子
                            intent = new Intent(mContext, MirrorActivity.class);
                            mContext.startActivity(intent);
                            break;
                    }
                }
            });
        }
    }
}

