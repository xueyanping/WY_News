package com.xue.yado.wy_news.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lidroid.xutils.BitmapUtils;
import com.xue.yado.wy_news.App;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.myView.CustomPopupWindow;
import com.xue.yado.wy_news.service.PlayService;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/20.
 */

public class FMDetailActivity extends Activity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private ImageView back,menu,image,stop,last,next;
    private TextView title;
    private SeekBar seekBar;
    private TextView currenttime,maxtime;
    private boolean isPause = false ;
    private boolean isbind = false;
    private FM.DataBean.ListBean fm;
    Intent intent ;
    private PlayService service;
    private Handler handler = new Handler();
    private CustomPopupWindow popupWindow;

    SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    //使用ServiceConnection来监听Service状态的变化
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            isbind = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化PlayService,通过binder来实现
            if(service == null) {
                service = ((PlayService.AudioBinder) binder).getService();
                maxtime.setText(time.format(service.getDuration()));
                isbind = true;
                if(isbind){
                    handler.post(runnable);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fmdetail);

        initView();

        initClick();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initStart();
            }
        }).start();

    }

    private void initView() {
        Intent i = getIntent();
        fm = (FM.DataBean.ListBean) i.getSerializableExtra("fm");
        image = findViewById(R.id.image);
        seekBar = findViewById(R.id.seekbar);
        currenttime = findViewById(R.id.currenttime);
        maxtime = findViewById(R.id.maxtime);
        back = findViewById(R.id.back);
        menu = findViewById(R.id.popwindowmenu);
        stop = findViewById(R.id.stop);
        last = findViewById(R.id.last);
        next = findViewById(R.id.next);
        title = findViewById(R.id.title);
        new BitmapUtils(this).display(image,fm.getCoverMiddle());
        title.setText(fm.getTitle());
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currenttime.setText(time.format(service.getCurrentPosition()));
            seekBar.setMax(service.getDuration());
            seekBar.setProgress(service.getCurrentPosition());
            seekBar.setSecondaryProgress(service.getSecondBar());
            maxtime.setText(time.format(service.getDuration()));
            handler.postDelayed(runnable, 500);
        }
    };


   public void initStart(){
       intent  = new Intent(this,PlayService.class);
       intent.putExtra("uri",fm.getPlayPathAacv164());
       startService(intent);
       if(!isbind){
           bindService(intent, conn, Context.BIND_AUTO_CREATE);
       }

    }

    @Override
    protected void onPause() {
        super.onPause();
   }

    @Override
    protected void onRestart() {
        super.onRestart();
   }

    public void initClick(){
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
        stop.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        intent.setClass(this,PlayService.class);
        switch (v.getId()){
            case R.id.back:
                //stopService(intent);
                this.finish();
                break;
            case R.id.popwindowmenu:
                popupWindow = new CustomPopupWindow(this);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                popClick();
                break;
            case R.id.stop:
                if(!isPause){
                    stop.setImageResource(R.mipmap.bofang);
                    service.playOrStop(isPause);
                    isPause=true;
                }else{
                    stop.setImageResource(R.mipmap.stop);
                    service.playOrStop(isPause);
                    isPause = false;
                }
                break;
            case R.id.last:
                last.setImageResource(R.mipmap.jin);
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           Thread.sleep(100);
                           last.setImageResource(R.mipmap.last);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }).start();
                service.tui();
                break;
            case R.id.next:
                next.setImageResource(R.mipmap.tui);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            next.setImageResource(R.mipmap.next);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                service.jin();
                break;

        }
    }

    private void popClick() {
        popupWindow.setOnItemClickListener(new CustomPopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()){
                    case R.id.download:
                        Toast.makeText(FMDetailActivity.this, "download", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.like:
                        Toast.makeText(FMDetailActivity.this, "like", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                service.seekTo(progress);
            }
            seekBar.setProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
