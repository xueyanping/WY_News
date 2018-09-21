package com.xue.yado.wy_news.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/20.
 */

public class FMDetailActivity extends Activity {

    private ImageView image;
    private TextView title;
    int position;
    ArrayList<? extends FM.DataBean.ListBean> list;
    MediaPlayer player  = new MediaPlayer();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fmdetail);
        initView();
    }

    private void initView() {
        Intent i = getIntent();
            position = i.getIntExtra("position",0);
            list = i.getParcelableArrayListExtra("list");
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        title.setText(list.get(position).getTitle());
        new BitmapUtils(this).display(image,list.get(position).getCoverMiddle());
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    player.reset();
                    Uri uri = Uri.parse(list.get(position).getPlayUrl64());
                    player.setDataSource(FMDetailActivity.this,uri);
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            Log.e("MusicReceiver", "a");
                            mp.start();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
