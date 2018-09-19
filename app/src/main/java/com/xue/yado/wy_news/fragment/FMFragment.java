package com.xue.yado.wy_news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.yado.wy_news.R;

/**
 * Created by Administrator on 2018/9/12.
 */

public class FMFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.redian_view,container,false);
        return view;
    }
}
