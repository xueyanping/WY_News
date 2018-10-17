package com.xue.yado.wy_news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.DuoTuNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/7.
 */

public class MultiPhotoActivity extends Activity{
    private ImageButton back;
    private ViewPager viewPager;
    List<String> bitmap_list;
    List<String> content_list;
    DuoTuNews duoTuNews;
    BitmapUtils utils;
    TextView xinwenxi_content,xinwen_xi_title;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.multiphotoview);
        Intent intent = getIntent();
        initView();
        String url = intent.getStringExtra("url_json");
        title = intent.getStringExtra("title");
        Log.i("xue", "onCreate: "+title);
       httpUtils(url);

    }



    private void initView() {
        back = findViewById(R.id.back);
        viewPager = findViewById(R.id.pic_pager);
        xinwenxi_content = findViewById(R.id.xinwenxi_content);
        xinwen_xi_title = findViewById(R.id.xinwen_xi_title);

        utils = new BitmapUtils(MultiPhotoActivity.this);
        bitmap_list = new ArrayList<>();
        content_list = new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiPhotoActivity.this.finish();
            }
        });
    }

    private void setData() {
        xinwen_xi_title.setText(title);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                xinwenxi_content.setText(content_list.get(position));
                    viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(bitmap_list.size());
        viewPager.setAdapter(adapter);
    }

    public void httpUtils(String url){

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String str = responseInfo.result;
              //  Log.i( "onSuccess: ","onSuccess: "+str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("photos");
                    for (int i = 0;i<jsonArray.length();i++){

                        JSONObject o = (JSONObject) jsonArray.get(i);
                        String imgurl = o.getString("imgurl");
                        String content = o.getString("note");
//                        Log.i( "onSuccess: ","onSuccess: "+imgurl);
                        content_list.add(content);
                        bitmap_list.add(imgurl);
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i( "onFailure: ","onFailure: ");
            }
        });

//        final XutilsGetData getData = new XutilsGetData();
//        getData.xUtilsHttp(MultiPhotoActivity.this,url,new XutilsGetData.CallBackHttp(){
//
//            @Override
//            public void handleData(String data) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(data);
//                    JSONArray jsonArray = jsonObject.getJSONArray("photos");
//                    for(int i = 0;i<jsonArray.length();i++){
//                        JSONObject object = (JSONObject) jsonArray.get(i);
//                        String imgurl = (String) object.get("imgurl");
//
//                        bitmap_list.add(imgurl);
//                    }
//                    setData();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        },true);
    }

    class ImagePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return bitmap_list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView  imageView = new ImageView(MultiPhotoActivity.this);
            utils.display(imageView,bitmap_list.get(position));
            xinwenxi_content.setText(content_list.get(position));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           // super.destroyItem(container, position, object);
            container.removeView(container);
        }
    }
}
