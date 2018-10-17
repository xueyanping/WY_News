package com.xue.yado.wy_news.activity;

import android.app.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;

import android.support.annotation.RequiresApi;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.BitmapUtils;

import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.listener.PlayListener;
import com.xue.yado.wy_news.myView.CustomPopupWindow;
import com.xue.yado.wy_news.myView.GoodView;
import com.xue.yado.wy_news.service.PlayService;

import java.text.SimpleDateFormat;



/**
 * Created by Administrator on 2018/9/20.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FMDetailActivity extends Activity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private ImageView back,menu,image,last,next,ILike,playtime,sharetime;
    private ImageView stop;
    private TextView title;
    private SeekBar seekBar;
    private TextView currenttime,maxtime,ILike_times,playtimes,sharetimes;
    //private boolean isPause = false ;
    private boolean isbind = false;
    private FM.DataBean.ListBean fm;
    Intent intent ;
    private String url;
    private PlayService service;
    private Handler handler = new Handler();
    private CustomPopupWindow popupWindow;
    private GoodView goodView;
    private static boolean isZaned;
    PlayService.NotificationBroadcastReceiver notificationbroadcastreceiver;
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
                IntentFilter filter = new IntentFilter();
                filter.addAction("pause");
                notificationbroadcastreceiver = service.new NotificationBroadcastReceiver();
                registerReceiver(notificationbroadcastreceiver, filter);
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
        goodView = new GoodView(this);
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
        ILike = findViewById(R.id.ILike);
        playtime = findViewById(R.id.playtime);
        sharetime = findViewById(R.id.sharetime);

        ILike_times = findViewById(R.id.ILike_times);
        sharetimes = findViewById(R.id.sharetimes);
        playtimes = findViewById(R.id.playtimes);
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
        playtimes.setText(fm.getPlaytimes()+"");
        sharetimes.setText(fm.getShares()+"");
        ILike_times.setText(fm.getLikes()+"");
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
       url = fm.getPlayPathAacv164();
       intent.putExtra("fm",fm);
       startService(intent);
       if(!isbind){
           bindService(intent, conn, Context.BIND_AUTO_CREATE);
       }
       PlayService.isPause = false;

    }


    public void initClick(){
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
        stop.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        ILike.setOnClickListener(this);
        sharetime.setOnClickListener(this);
        playtime.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {

        intent.setClass(this,PlayService.class);
        switch (v.getId()){
            case R.id.back:
//                stopService(intent);
//                unbindService(conn);
                this.finish();
                break;
            case R.id.popwindowmenu:
                popupWindow = new CustomPopupWindow(this);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                popClick();
                break;
            case R.id.stop:
                if(PlayService.isPause){
                    stop.setImageResource(R.mipmap.stop);
                    service.playOrStop(PlayService.isPause);
                    PlayService.isPause=false;
                }else{
                    stop.setImageResource(R.mipmap.bofang);
                    service.playOrStop(PlayService.isPause);
                    PlayService.isPause = true;
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
            case R.id.ILike:
                if(!isZaned){
                    ILike.setImageResource(R.mipmap.good_checked);
                    goodView.setText("+1");
                    goodView.show(ILike);
                    fm.setLikes(fm.getLikes()+1);
                    ILike_times.setText(fm.getLikes()+"");
                    isZaned = true;
                }else{
                    ILike.setImageResource(R.mipmap.good);
                    goodView.reset();
                    fm.setLikes(fm.getLikes()-1);
                    ILike_times.setText(fm.getLikes()+"");
                    isZaned = false;
                }
                break;
            case R.id.playtime:
                Toast.makeText(this, "playtime="+fm.getPlaytimes(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.sharetime:
                Toast.makeText(this, "sharetime="+fm.getShares(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void popClick() {
        popupWindow.setOnItemClickListener(new CustomPopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()){
                    case R.id.download:

                        service.download(url);
                        popupWindow.dismiss();
                        break;
                    case R.id.like:
                        Toast.makeText(FMDetailActivity.this, "like", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.id_btn_cancelo:
                        popupWindow.dismiss();
                        break;
                        default:
                            popupWindow.dismiss();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationbroadcastreceiver);
    }


}
