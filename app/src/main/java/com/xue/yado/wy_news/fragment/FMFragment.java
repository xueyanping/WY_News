package com.xue.yado.wy_news.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.activity.FMDetailActivity;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.myView.FMRecyclerAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/12.
 */

public class FMFragment extends android.support.v4.app.Fragment  {

    public static final String URL = "http://mobile.ximalaya.com/mobile/v1/album/track?albumId=203355&device=android&isAsc=true&pageId=1&pageSize=40&statEvent=pageview%2Falbum%40203355&statModule=%E6%9C%80%E5%A4%9A%E6%94%B6%E8%97%8F%E6%A6%9C&statPage=ranklist%40%E6%9C%80%E5%A4%9A%E6%94%B6%E8%97%8F%E6%A6%9C&statPosition=8";

    HttpUtils http = new HttpUtils();

    FM fm;
    RecyclerView recyclerView;
    FMRecyclerAdapter adapter;
    ArrayList<FM.DataBean.ListBean> fmList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getData();
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.redian_view,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FMRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);
        setItemClick();
    }

    private void setItemClick() {
        adapter.setOnItemClick(new FMRecyclerAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, List<FM.DataBean.ListBean> list) {

                Intent intent = new Intent(getContext(), FMDetailActivity.class);

                intent.putExtra("list", fmList);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });
    }

    private void getData() {
        http.send(HttpRequest.HttpMethod.GET,URL, new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
               // Log.i( "onSuccess: ","result=="+responseInfo.result);
                Gson gson = new Gson();
                fm = gson.fromJson(responseInfo.result, FM.class);
                fmList = (ArrayList<FM.DataBean.ListBean>) fm.getData().getList();
                if(fmList.size()>0){
                    adapter.clearDatas();
                    adapter.addDatas(fmList);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("onFailure", "onFailure: ");
            }
        });
    }

}
