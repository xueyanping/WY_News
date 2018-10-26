package com.xue.yado.wy_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.activity.MultiPhotoActivity;
import com.xue.yado.wy_news.activity.NewsContentActivity;
import com.xue.yado.wy_news.bean.Toutiao;

import com.xue.yado.wy_news.listener.ObservableOnNextListener;

import com.xue.yado.wy_news.adapter.MyRecyclerAdapter;
import com.xue.yado.wy_news.adapter.NewsAdapter;
import com.xue.yado.wy_news.newWork.MyObservable;
import com.xue.yado.wy_news.newWork.RetrofitMethods;
import com.xue.yado.wy_news.utils.SharedPreferenceUtil;
import com.xue.yado.wy_news.utils.XinWenJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/12.
 */

public class NewsRecyclerFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerview;
    private MyRecyclerAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private SmartRefreshLayout refresh;
    List<Toutiao.T1348647853363Bean> toutiao_list = new ArrayList<>();
    List<Toutiao.T1348647853363Bean.AdsBean> listads ;
    String type;
    View view;
    int START_INDEX = 0;
    int END_INDEX = 10;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("xinwen");
        getData(type,START_INDEX,END_INDEX);
    }

    public void initView() {
        recyclerview = view.findViewById(R.id.recyclerview);
        refresh = view.findViewById(R.id.refresh);
        adapter = new MyRecyclerAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mLayoutManager);
        Log.i("initView: ", "initView: " + type);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);

        setItemClick();
        refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                END_INDEX +=10;
                getData(type,START_INDEX,END_INDEX);
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void setItemClick() {
        adapter.setOnItemClick(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, List<Toutiao.T1348647853363Bean> toutiao) {
                int pos;
                if (adapter.haveHeaderView() && lunboList != null ) {
                    pos = --position;
                }else{
                    pos = position;
                }
                    String type = toutiao.get(pos).getSkipType();
                    int typecode = -1;
                    if (type == null) {
                        typecode = NewsAdapter.PUTONG;
                    } else {
                        typecode = NewsAdapter.getSkipType(type);
                    }
                    switch (typecode) {
                        case NewsAdapter.PUTONG:
                        case NewsAdapter.ZHUANTI:
                            Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                            intent.putExtra("title", toutiao.get(pos).getTitle());
                            intent.putExtra("content_url", toutiao.get(pos).getUrl_3w());
                            startActivity(intent);
                            // getActivity().overridePendingTransition(R.anim.xinwen_inactivity, R.anim.xinwen_inactivity);
                            break;
                        case NewsAdapter.DUOTU:
                            String SkipID = toutiao.get(pos).getSkipID();
                            String url_key = SkipID.substring(SkipID.lastIndexOf("|") - 4);
                            url_key = url_key.replaceAll("\\|", "/");
                            String url = "http://c.3g.163.com/photo/api/set/" + url_key + ".json";
                            Intent intent2 = new Intent(getActivity(), MultiPhotoActivity.class);
                            intent2.putExtra("url_json", url);
                            intent2.putExtra("title", toutiao.get(pos).getTitle());
                            startActivity(intent2);
                            break;
                    }
                }

        });

    }

    public void getData(final String type, int start, int end){
        ObservableOnNextListener<String> listener = new ObservableOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                //Log.i("onNext: ", "onNext: "+s);
                SharedPreferenceUtil.saveData(getContext(),type,s);
                Toutiao toutiao = XinWenJson.getdata(s, type);
                toutiao_list = toutiao.getT1348647853363();
               // Log.i("onNext:", "toutiao_list.size: "+toutiao_list.size());
                if(toutiao_list.size()>0){
                    adapter.clearDatas();
                    adapter.addDatas(toutiao_list);
                    if(toutiao_list.get(0).getAds()!=null && toutiao_list.get(0).getAds().size()>0){
                        listads = toutiao_list.get(0).getAds();
                    }else{
                        listads = null;
                    }
                    View view = showLunbo();
                    if(view != null ){
                        adapter.addHeaderView(view);
                    }
                }
            }
        };

        switch (type){
            case "toutiao":
                RetrofitMethods.Wngyi_CarData(start,end,new MyObservable(listener,getContext()));
            break;
            case "yule":
                RetrofitMethods.Wngyi_YuLeData(start,end,new MyObservable(listener,getContext()));
                break;
            case "junshi":
                RetrofitMethods.Wngyi_JunShiData(start,end,new MyObservable(listener,getContext()));
                break;
            case "tiyu":
                RetrofitMethods.Wngyi_TiYuData(start,end,new MyObservable(listener,getContext()));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newsrecycler_view,container,false);
        initView();
        return view;
    }


    public void lunboToActivity(int position){
        String type = listads.get(position).getSkipType();
        //Log.i( "onClick: ","type==="+type);
        int typecode ;
        if(type == null){
            typecode = NewsAdapter.PUTONG;
        }else{
            typecode = NewsAdapter.getSkipType(type);
        }
        switch(typecode){
            case NewsAdapter.PUTONG:
            case NewsAdapter.ZHUANTI:
                Intent intent = new Intent(getActivity(),NewsContentActivity.class);
                intent.putExtra("title",listads.get(position).getTitle());
                intent.putExtra("content_url",listads.get(position).getUrl());
                startActivity(intent);
                // getActivity().overridePendingTransition(R.anim.xinwen_inactivity, R.anim.xinwen_inactivity);
                break;
            case NewsAdapter.DUOTU:
                String SkipID = listads.get(position).getSkipID();
                String url_key = SkipID.substring(SkipID.lastIndexOf("|")-4);
                url_key = url_key.replaceAll("\\|","/");
                String url = "http://c.3g.163.com/photo/api/set/" + url_key + ".json";
                Intent intent2 = new Intent(getActivity(),MultiPhotoActivity.class);
                intent2.putExtra("url_json",url);
                intent2.putExtra("title",listads.get(position).getTitle());
                startActivity(intent2);
                break;
        }
    }


    //轮播显示的方法
    private List<Lunbo> lunboList = new ArrayList<>();
    private View lunboView;
    private LinearLayout linearLayouticon;
          List<ImageView> dot_list = new ArrayList<>();
    private ViewPager lunbo_viewPager;
            String xiangxiUrl;//跳转详细页面的url
    int currentItem = 0;
    int oldItem = 0;
    int size =0;
    private View showLunbo() {
        lunboList.clear();
       // dot_list.clear();
        if (listads == null) {
            return null;
        }
        size = listads.size();
        if(lunboView == null){
            lunboView = View.inflate(getActivity(), R.layout.xinwen_toutiao_lunbo, null);
        }
        //如果只有一个图片则不轮播
        if (size == 1) {
            TextView title = (TextView) lunboView.findViewById(R.id.toutiao_lunboyitu_title);
            title.setText(listads.get(0).getTitle());
            ImageView lunbo_yitu = (ImageView) lunboView.findViewById(R.id.daohang_lunbo_yitu);
            lunbo_yitu.setVisibility(View.VISIBLE);
            xiangxiUrl = listads.get(0).getUrl();
            new BitmapUtils(getContext()).display(lunbo_yitu, listads.get(0).getImgsrc());
            lunbo_yitu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //一个图片的轮播跳转的页面
                   lunboToActivity(0);
                }
            });
            return lunboView;

        }else if(size>1){

            if(linearLayouticon == null){
                linearLayouticon = (LinearLayout) lunboView.findViewById(R.id.toutitao_lunbo_ll);
                //轮播icon以及把数据添加到lunboList
                for (int i = 0; i < size; i++) {
                    //开辟一个宽和高的空间放入icon
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView icon = new ImageView(getActivity());
                    icon.setLayoutParams(params);//设置icon宽高
                    icon.setImageResource(R.mipmap.toutiao_lunbo_icon);
                    dot_list.add(icon);
                    linearLayouticon.addView(icon);//添加到布局
                     }
            }

            for (int i = 0; i < size; i++) {
                final ImageView image = new ImageView(getActivity());
                String SkipID = listads.get(i).getSkipID();
                String url_key = SkipID.substring(SkipID.lastIndexOf("|")-4);
                url_key = url_key.replaceAll("\\|","/");
                String url = "http://c.3g.163.com/photo/api/set/" + url_key + ".json";
                Log.i("showLunbo", "showLunbo: "+url);
                HttpUtils httpUtils = new HttpUtils();
                httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String str = responseInfo.result;
                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");
                            JSONObject o = (JSONObject) jsonArray.get(0);
                            String imgurl = o.getString("imgurl");
                            new BitmapUtils(getContext()).display(image,imgurl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i( "onFailure: ","onFailure: ");
                    }
                });
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setTag(i);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lunboToActivity((Integer) view.getTag());
                    }
                });
                xiangxiUrl = listads.get(i).getUrl();
                Lunbo lunbo = new Lunbo(listads.get(i).getTitle(), xiangxiUrl, image);
                lunboList.add(lunbo);

            }

            //设置第一个图片的标题
            final TextView title = (TextView) lunboView.findViewById(R.id.toutiao_lunbo_title);
            title.setText(lunboList.get(0).getTitle());



            if(lunbo_viewPager == null){

                lunbo_viewPager = (ViewPager) lunboView.findViewById(R.id.toutiao_lunbo_viewpager);
                lunbo_viewPager.setVisibility(View.VISIBLE);
                lunbo_viewPager.setOffscreenPageLimit(0);
                lunbo_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {

                        int m = position%size;
                        title.setText(lunboList.get(m).getTitle());
//                        dot_list.get(oldItem).setImageResource(R.mipmap.toutiao_lunbo_icon);
//                        dot_list.get(currentItem).setImageResource(R.mipmap.toutiao_lunbo_icon2);
//

                        for(int i=0;i<size;i++){
                            if(i==m){
                                dot_list.get(i).setImageResource(R.mipmap.toutiao_lunbo_icon2);
                            }else {
                                dot_list.get(i).setImageResource(R.mipmap.toutiao_lunbo_icon);
                            }
                        }
                        currentItem = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

                Lunboadapter lunboadapter = new Lunboadapter();
                lunbo_viewPager.setAdapter(lunboadapter);
                lunbo_viewPager.setCurrentItem(0);
            }

            if (thread == null) {//如果只有一个图片不需要轮播
                startthreadLunbo();//开启子线程轮播
                thread.start();
            }
            return lunboView;
        }

         return null;
        }



    private Thread thread;//子线程轮播对象
    private void startthreadLunbo() {
        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                lunbo_viewPager.setCurrentItem(((currentItem+1)));
            }
        }
    };


    class Lunboadapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                View v=lunboList.get(position%size).getImageView();
                ViewGroup parent = (ViewGroup) v.getParent();

                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(lunboList.get(position %size).getImageView());
                return lunboList.get(position %size ).getImageView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    //轮播工具类
    class Lunbo {
        String title;
        String xiangxiurl;
        ImageView imageView;

        public Lunbo(String title, String xiangxiurl, ImageView imageView) {
            this.title = title;
            this.xiangxiurl = xiangxiurl;
            this.imageView = imageView;
        }

        public String getTitle() {
            return title;
        }

        public String getXiangxiurl() {
            return xiangxiurl;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
