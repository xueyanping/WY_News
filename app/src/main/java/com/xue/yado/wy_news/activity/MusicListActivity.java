package com.xue.yado.wy_news.activity;



import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;


import com.vondear.rxtools.view.RxToast;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.adapter.Adapter;
import com.xue.yado.wy_news.bean.FM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MusicListActivity extends AppCompatActivity {

    private static final String TAG = "info";
    @BindView(R.id.recyclerview_fmlist)
    RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private List<FM.DataBean.ListBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_von_photo);
        ButterKnife.bind(this);
        scanMusic();
        initViews();
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new Adapter(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setData(list);
        mAdapter.setIonSlidingViewClickListener(new Adapter.IonSlidingViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RxToast.info(list.get(position).getTitle());
            }

            @Override
            public void onDeleteBtnCilck(View view, int position) {
                mAdapter.removeData(position);
            }
        });
    }


    /**
     * 扫描本地FM
     */
    public void scanMusic(){
        list = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = this.getContentResolver().query(uri,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

      //  Log.i( "scanMusic: ","count=="+cursor.getCount());

        if(cursor!=null){
            while (cursor.moveToNext()){
               FM.DataBean.ListBean data = new FM.DataBean.ListBean();
                String name = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                long size = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                int duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String path = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

               if(!path.contains("duanzi_download")){
               continue;
                }
               data.setTitle(name);
               data.setPlayPathAacv164(path);
               data.setDuration(duration);
               list.add(data);
            }
        }
       // Log.i( "scanMusic: ","size=="+list.size());
    }

}
