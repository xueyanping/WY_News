package com.xue.yado.wy_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.activity.FMDetailActivity;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.adapter.FMAdapter;
import com.xue.yado.wy_news.adapter.FMRecyclerAdapter;
import com.xue.yado.wy_news.utils.SharedPreferenceUtil;

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
//    FMRecyclerAdapter adapter;
    FMAdapter adapter;
    ArrayList<FM.DataBean.ListBean> fmList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.redian_view,container,false);
        initView(view);
        if(SharedPreferenceUtil.getData(getContext(),URL)!=null){
            Log.i("onNext: ", "SharedPreferenceUtilFM:=== "+SharedPreferenceUtil.getData(getContext(),URL));
            paraseJson(SharedPreferenceUtil.getData(getContext(),URL));
        }else{
            getData();
        }
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//      adapter = new FMRecyclerAdapter(getContext());
        adapter = new FMAdapter(getContext());
        recyclerView.setAdapter(adapter);
        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.progress_loading_image_02);
//        adapter.addHeaderView(image);
        setItemClick();
    }

    private void setItemClick() {
       adapter.setOnItemClick(new FMRecyclerAdapter.OnItemClickListener<FM.DataBean.ListBean>() {
           @Override
           public void itemClick(int position, List<FM.DataBean.ListBean> list) {
               Log.i( "itemClick: ","position=="+position);
                int pos ;
               if (adapter.haveHeaderView() && position == 0){
                   pos = position;
                   Toast.makeText(getContext(), "这是头部", Toast.LENGTH_SHORT).show();
              }else{
                   if(adapter.haveHeaderView()){
                       pos = -- position;
                   }else{
                       pos = position;
                   }

                  Intent intent = new Intent(getContext(), FMDetailActivity.class);
//                   Bundle bundle = new Bundle();
//                   bundle.putSerializable("fm", fmList.get(pos));
                   intent.putExtra("fm", fmList.get(pos));
                  startActivity(intent);
                  getActivity().overridePendingTransition(R.anim.in_anim,R.anim.in_anim);

              }

               }

       });
    }

    private void getData() {
        http.send(HttpRequest.HttpMethod.GET,URL, new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i( "onSuccess: ","result=="+responseInfo.result);
                SharedPreferenceUtil.saveData(getContext(),URL,responseInfo.result);
                paraseJson(responseInfo.result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("onFailure", "onFailure: ");
            }
        });
    }

    private void paraseJson(String result) {
        Gson gson = new Gson();
        fm = gson.fromJson(result, FM.class);
        fmList = (ArrayList<FM.DataBean.ListBean>) fm.getData().getList();
        if(fmList.size()>0){
            adapter.clearDatas();
            adapter.addDatas(fmList);
        }

    }

}
