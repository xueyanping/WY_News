package com.xue.yado.wy_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.activity.TextSizeShowActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Function_SetFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.textSize)
    TextView textSize;

    public Function_SetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_setfragment_view, container, false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textSize:
                startActivity(new Intent(getActivity(), TextSizeShowActivity.class));
                break;
        }
    }
}
