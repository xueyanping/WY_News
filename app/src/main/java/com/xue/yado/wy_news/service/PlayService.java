package com.xue.yado.wy_news.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.xue.yado.wy_news.App;

import java.io.File;
import java.io.IOException;


/**
 * Created by Administrator on 2018/9/25.
 */

public class PlayService extends Service implements MediaPlayer.OnCompletionListener,CacheListener{
    private  MediaPlayer player ;
    Uri uri;
    private final IBinder binder = (IBinder) new AudioBinder();
    int sec;
    boolean isCached = false;
    String url;
    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }
    /**
     * 当Audio播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        stopSelf();//结束了。则结束Service
    }

    //在这里我们须要实例化MediaPlayer对象
    public void onCreate(){
        super.onCreate();

    }

    public   MediaPlayer getInstance(){
        if(player == null){
            player = new MediaPlayer();

        }
        return player;
    }



    /**
     * 该方法在SDK2.0才開始有的。替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId){
            getInstance();
            url = intent.getStringExtra("uri");
            HttpProxyCacheServer proxy = App.getProxy(this);
            proxy.registerCacheListener(this,url);
            isCached = proxy.isCached(url);
            if(isCached){
                setsecondBar(100);
            }
            String proxyUrl = proxy.getProxyUrl(url,true);
            uri = Uri.parse(proxyUrl);
            player.reset();
            player = MediaPlayer.create(this,uri);
            player.setOnCompletionListener(this);

        if(!player.isPlaying()){
            player.start();
        }
        return START_STICKY;
    }

    public void onDestroy(){
        //super.onDestroy();
        player.seekTo(0);
        if(player.isPlaying()){
            player.stop();
        }
        player.release();
        player = null;
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        setsecondBar(percentsAvailable);
        if(percentsAvailable == 100){
            isCached = true;
        }else{
            isCached = false;
        }
    }


    //为了和Activity交互，须要定义一个Binder对象
   public class AudioBinder extends Binder {
        //返回Service对象
        public PlayService getService(){
            return PlayService.this;
        }
    }

    public void playOrStop(boolean ispause){
        if(player == null){
            return ;
        }
        if(ispause){
                seekTo(getCurrentPosition());
                player.start();
        }else{
            player.pause();
        }
    }

    public int  getDuration(){
        return player.getDuration();
    }

    public int  getCurrentPosition(){
       // Log.i( "getCurrentPosition: ","player.getCurrentPosition()="+player.getCurrentPosition());
        return player.getCurrentPosition();
    }

    public void setCurrentPotion(int position){
        seekTo(position);
    }

    public void seekTo(int position){
            //毫秒
         player.seekTo(position);
    }

    public void tui(){
        player.seekTo(player.getCurrentPosition()-15*1000);
    }
    public void jin(){
        player.seekTo(player.getCurrentPosition()+15*1000);
    }

    public void setsecondBar(int sec){
        this.sec = sec;
    }

    public int getSecondBar(){
        return sec * player.getDuration();
    }
}


