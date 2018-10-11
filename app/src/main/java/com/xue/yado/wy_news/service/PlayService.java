package com.xue.yado.wy_news.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.annotation.Check;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.callback.RequestCallBackHandler;
import com.xue.yado.wy_news.App;

import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Administrator on 2018/9/25.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class PlayService extends Service implements MediaPlayer.OnCompletionListener,CacheListener{
    private  MediaPlayer player ;
    Uri uri;
    private final IBinder binder = (IBinder) new AudioBinder();
    int sec;
   public static boolean isPause;
    boolean isCached = false;
    FM.DataBean.ListBean fm;
    String url;
   private static final String notificationStrId = "1",notificationName = "2",download_notificationStrId = "01",download_notificationName = "02";
    private static final int id = 5;
    private static final int down_id = 6;
    RemoteViews contentView,downContentView;
    Notification.Builder builder,download_builder;
    Notification notification,download_notification;
    NotificationBroadcastReceiver notificationbroadcastreceiver;

    NotificationManager notificationManager,download_notificationManager;

   public  class NotificationBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            switch (str){
                case "download complete":
                    downContentView.setTextViewText(R.id.download_notificationTitle,"下载完成 点击查看");
                    downContentView.setProgressBar(R.id.download_notificationProgress,100,100,false);
                    download_notificationManager.notify(down_id,download_notification);
                    break;
                case "download updating":
                    getDownloadNotification();
                    downContentView.setTextViewText(R.id.download_notificationTitle,"正在下载");
                    downContentView.setTextViewText(R.id.download_notificationPercent, intent.getIntExtra("progress",0)+"%");
                    downContentView.setProgressBar(R.id.download_notificationProgress,100, intent.getIntExtra("progress",0),false);
                    Log.i("onSuccess", "download ==="+intent.getIntExtra("progress",0));
                    download_notificationManager.notify(down_id,download_notification);
                    break;
                case "download failure":
                    downContentView.setTextViewText(R.id.download_notificationTitle,"下载失败 点击返回");
//                    PendingIntent pendingIntent = PendingIntent.getService(PlayService.this,0x11,new Intent(PlayService.this,this.getClass()),PendingIntent.FLAG_CANCEL_CURRENT);
//                    notification.contentIntent = pendingIntent;
                    download_notificationManager.cancel(down_id);

                    break;
                case "pause":
                    if(PlayService.isPause){
                        contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.bofang));
                        PlayService.isPause = true;
                    }else{
                        contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.stop));
                        PlayService.isPause = false;
                    }
                    playOrStop(PlayService.isPause);
                    notificationManager.notify(id,notification);
                    break;
                case "next":
                    jin();
                    break;
                case "last":
                    tui();
                    break;
                }
        }

    }



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
        //注册BroadcastReceiver
        notificationbroadcastreceiver = new NotificationBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("download complete");
        filter.addAction("download updating");
        filter.addAction("download failure");
        filter.addAction("pause");
        filter.addAction("last");
        filter.addAction("next");
        PlayService.this.registerReceiver(notificationbroadcastreceiver, filter);

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
            fm = (FM.DataBean.ListBean) intent.getSerializableExtra("fm");
            url = fm.getPlayUrl64();
         //创建通知栏
            getNotification();
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
        PlayService.this.unregisterReceiver(notificationbroadcastreceiver);
        if(notification!=null){
            notificationManager.cancelAll();
        }
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
            player.pause();
        }else{
            player.start();
        }
    }

    public int  getDuration(){
        return player.getDuration();
    }

    public int  getCurrentPosition(){
       // Log.i( "getCurrentPosition: ","player.getCurrentPosition()="+player.getCurrentPosition());
        return player.getCurrentPosition();
    }

    public void setCurrentPosition(int position){
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


    public void download(final String url){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/duanzi_download"+"/"+url.substring(url.lastIndexOf("/") + 1);

        File file = new File(filePath);
        if(file.exists()){
            Toast.makeText(this,"文件已存在",Toast.LENGTH_SHORT).show();

            return;
        }

            HttpUtils http = new HttpUtils();
            http.download(url,filePath , new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i("onSuccess", "download complete");
                    Intent intentComplete = new Intent("download complete");
                    sendBroadcast(intentComplete);

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i("DOWNLOAD", "download failed");
                    Intent intentFailure = new Intent("download failure");
                    sendBroadcast(intentFailure);
                }

                @Override
                public void onLoading( long total,  long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Intent intentUpdate = new Intent("download updating");
                    float percent = ((current * 1.0f / total)*100);
                    int progress = (int) percent;
                    intentUpdate.putExtra("progress",progress);
                    Log.i("onLoading", "progress=="+progress);
                    sendBroadcast(intentUpdate);
                }
            });
    }


    private void getNotification() {
         notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel 适配8.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationStrId, notificationName, NotificationManager.IMPORTANCE_MIN);
            notificationManager.createNotificationChannel(channel);
        }
        contentView = new RemoteViews(getPackageName(),
                R.layout.normal_notification);


         contentView.setImageViewBitmap(R.id.normal_notification_image, BitmapFactory.decodeResource(getResources(),R.mipmap.fm));
         contentView.setTextViewText(R.id.normal_notification_title,fm.getTitle());

         PendingIntent pauseIntent = PendingIntent.getBroadcast(this,1,new Intent("pause"),PendingIntent.FLAG_CANCEL_CURRENT);
         contentView.setOnClickPendingIntent(R.id.normal_notification_stop,pauseIntent);

        PendingIntent lastIntent = PendingIntent.getBroadcast(this,2,new Intent("last"),PendingIntent.FLAG_CANCEL_CURRENT);
        contentView.setOnClickPendingIntent(R.id.normal_notification_last,lastIntent);

        PendingIntent nextIntent = PendingIntent.getBroadcast(this,3,new Intent("next"),PendingIntent.FLAG_CANCEL_CURRENT);
        contentView.setOnClickPendingIntent(R.id.normal_notification_next,nextIntent);

        builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.nopicture)
                .setContentTitle("测试服务")
                .setContentText("我正在运行");


        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationStrId);
        }
        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        notification = builder.build();
        notification.contentView = contentView;
        notificationManager.notify(id,notification);
    }

    private void getDownloadNotification() {
        download_notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel 适配8.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(download_notificationStrId, download_notificationName, NotificationManager.IMPORTANCE_MIN);
            download_notificationManager.createNotificationChannel(channel);
        }
        downContentView = new RemoteViews(getPackageName(),
                R.layout.download_notification_layout);

        download_builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.nopicture)
                .setContentTitle("下载任务")
                .setContentText("我正在运行");


        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            download_builder.setChannelId(download_notificationStrId);
        }
        download_builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        download_notification = download_builder.build();
        download_notification.contentView = downContentView;
        download_notificationManager.notify(down_id,download_notification);
    }
}


