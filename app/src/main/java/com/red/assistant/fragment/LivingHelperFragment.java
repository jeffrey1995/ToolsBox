package com.red.assistant.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.red.assistant.R;
import com.red.assistant.activity.AboutActivity;
import com.red.assistant.activity.LocationSourceActivity;
import com.red.assistant.activity.PoiAroundSearchActivity;
import com.red.assistant.activity.QueryActivity;

/**
 * Created by tianxiying on 16/8/9.
 */
public class LivingHelperFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText search_edt;
    private ImageView search_iv, delete_img;
    private RelativeLayout map_rl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_living_helper, null);
        findView();
        return view;
    }

    private void findView() {
        search_edt = (EditText) view.findViewById(R.id.search_edt);
        search_iv = (ImageView) view.findViewById(R.id.search_iv);
        delete_img = (ImageView) view.findViewById(R.id.delete_img);
        map_rl = (RelativeLayout) view.findViewById(R.id.map_rl);

        delete_img.setVisibility(View.INVISIBLE);

        view.findViewById(R.id.about_tv).setOnClickListener(this);
        search_iv.setOnClickListener(this);
        map_rl.setOnClickListener(this);
        delete_img.setOnClickListener(this);
        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TextChanged", count + "");
                if ("".equals(search_edt.getText().toString())) delete_img.setVisibility(View.INVISIBLE);
                else {
                    delete_img.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", s.toString() + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv:
                Intent intent = new Intent(getActivity(), QueryActivity.class);
                intent.putExtra("value", search_edt.getText() + "");
                startActivity(intent);
                break;
            case R.id.map_rl:
                startActivity(new Intent(getActivity(), PoiAroundSearchActivity.class));
                break;
            case R.id.delete_img:
                search_edt.setText("");
                break;
            case R.id.about_tv:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
        }
    }
}
