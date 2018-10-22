package com.xue.yado.wy_news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxRecyclerViewDividerTool;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.activity.MusicListActivity;
import com.xue.yado.wy_news.bean.ModelMainItem;
import com.xue.yado.wy_news.myView.AdapterRecyclerViewMain;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class Setting_MainFragment extends Fragment  {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<ModelMainItem> mData;
    private int mColumnCount = 3;
    Context mContext;
    private Unbinder unbinder;
    public Setting_MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_mainfragment_view, container, false);
        mContext = getContext();
        unbinder =  ButterKnife.bind(this,view);

        initData();
        initView();
        return view;
    }

    private void initView() {
        if (mColumnCount <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }

        recyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(mData);

        recyclerview.setAdapter(recyclerViewMain);
    }

    private void initData() {
            mData = new ArrayList<>();
            mData.add(new ModelMainItem("本地FM", R.drawable.circle_elves_ball, MusicListActivity.class));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
