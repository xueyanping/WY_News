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
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.vondear.rxtools.view.RxToast;
import com.xue.yado.wy_news.App;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.listener.PlayListener;

import java.io.File;

/**
 * Created by Administrator on 2018/9/25.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class PlayService extends Service implements MediaPlayer.OnCompletionListener,CacheListener{
    private  MediaPlayer player ;

    Uri uri;
    private final IBinder binder = (IBinder) new AudioBinder();

    int sec; //缓存百分比
    public static boolean isPause;//是否已暂停
    boolean isCached = false;//是否缓存完毕
    FM.DataBean.ListBean fm;
    String url;//播放的网络路径

    private static final String notificationStrId = "FM";
    private static final String notificationName = "NOTIFICATION_NAME";
    private static final String download_notificationStrId = "FM_DOWNLOAD";
    private static final String download_notificationName = "DOWNLOAD_NOTIFICATION_NAME";
    private static final int ID = 5;
    private static final int DOWNLOAD_ID = 6;
    RemoteViews contentView,downContentView;
    Notification.Builder builder,download_builder;
    Notification notification,download_notification;
    NotificationBroadcastReceiver notificationbroadcastreceiver;

    NotificationManager notificationManager,download_notificationManager;
    PlayListener listener;

    //为了和Activity交互，须要定义一个Binder对象
    public class AudioBinder extends Binder {
        //返回Service对象
        public PlayService getService(){
            return PlayService.this;
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


    public void setPlayerListener(PlayListener listener){
        this.listener = listener;
    }

    public  class NotificationBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            switch (str){
                case "download complete":
                    downContentView.setTextViewText(R.id.download_notificationTitle,"下载完成 点击查看");
                    download_notificationManager.notify(DOWNLOAD_ID,download_notification);
                    break;
                case "download updating":
                    getDownloadNotification();
                    downContentView.setTextViewText(R.id.download_notificationTitle,"正在下载");
                    downContentView.setTextViewText(R.id.download_notificationPercent, intent.getIntExtra("progress",0)+"%");
                    downContentView.setProgressBar(R.id.download_notificationProgress,100, intent.getIntExtra("progress",0),false);
                    download_notificationManager.notify(DOWNLOAD_ID,download_notification);
                    break;
                case "download failure":
                    downContentView.setTextViewText(R.id.download_notificationTitle,"下载失败 点击返回");
//                    PendingIntent pendingIntent = PendingIntent.getService(PlayService.this,0x11,new Intent(PlayService.this,this.getClass()),PendingIntent.FLAG_CANCEL_CURRENT);
//                    notification.contentIntent = pendingIntent;
                    download_notificationManager.cancel(DOWNLOAD_ID);
                    break;
                case "pause":
                  Log.i( "onReceive: ","PlayService.isPause="+PlayService.isPause);
                    if(PlayService.isPause){
                        contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.stop));
                        playOrStop(PlayService.isPause);
                        listener.onPlayPause();
                        PlayService.isPause = false;
                    }else{
                        contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.bofang));
                        playOrStop(PlayService.isPause);
                        listener.onPlayPause();
                        PlayService.isPause = true;
                    }

                    notificationManager.notify(ID,notification);
                    break;

                case "next":
                    jin();
                    break;
                case "last":
                    tui();
                    break;
                case "cancel":
                    notificationManager.cancel(ID);
                    break;
                }
        }

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
        filter.addAction("cancel");
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
        // Log.i( "onCacheAvailable: ","1111111111"+percentsAvailable);
        if(percentsAvailable == 100){
            isCached = true;
            return ;
        }else{
            isCached = false;
        }
    }



    public void playOrStop(boolean ispause){
        if(player == null){
            return ;
        }
        if(!ispause){
            player.pause();
        }else{
            seekTo(getCurrentPosition());
            player.start();
        }

    }

    public void changeImage(boolean ispause){
        if(!ispause){
            contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.bofang));
        }else{
            contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.stop));

        }
        notificationManager.notify(ID,notification);
    }

    public int  getDuration(){
        return player.getDuration();
    }

    public int  getCurrentPosition(){
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
        player.seekTo(player.getCurrentPosition() - 15*1000);
    }
    public void jin(){
        player.seekTo(player.getCurrentPosition() + 15*1000);
    }

    public void setsecondBar(int sec){
        this.sec = sec;
    }

    public int getSecondBar(){
        return sec * player.getDuration();
    }

    //音频下载
    public void download(final String url){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/duanzi_download"+"/"+fm.getTitle()+".mp3";
       // String path = url.substring(url.lastIndexOf("/") + 1);

        File file = new File(filePath);
        if(file.exists()){
            RxToast.info(this,"文件已存在",Toast.LENGTH_SHORT).show();
            return;
        }

            HttpUtils http = new HttpUtils();
            http.download(url,filePath , new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //Log.i("onSuccess", "download complete");
                    Intent intentComplete = new Intent("download complete");
                    sendBroadcast(intentComplete);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                   // Log.i("DOWNLOAD", "download failed");
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
                   // Log.i("onLoading", "progress=="+progress);
                    sendBroadcast(intentUpdate);
                }
            });
    }


    private void getNotification() {

         notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel 适配8.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationStrId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        contentView = new RemoteViews(getPackageName(),R.layout.normal_notification);
        contentView.setImageViewBitmap(R.id.normal_notification_stop, BitmapFactory.decodeResource(getResources(),R.mipmap.stop));
        contentView.setImageViewBitmap(R.id.normal_notification_image, BitmapFactory.decodeResource(getResources(),R.mipmap.fm));
        contentView.setTextViewText(R.id.normal_notification_title,fm.getTitle());


         PendingIntent pauseIntent = PendingIntent.getBroadcast(this,1,new Intent("pause"),PendingIntent.FLAG_UPDATE_CURRENT);
         contentView.setOnClickPendingIntent(R.id.normal_notification_stop,pauseIntent);

        PendingIntent lastIntent = PendingIntent.getBroadcast(this,2,new Intent("last"),PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.normal_notification_last,lastIntent);

        PendingIntent nextIntent = PendingIntent.getBroadcast(this,3,new Intent("next"),PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.normal_notification_next,nextIntent);

        PendingIntent cancelIntent = PendingIntent.getBroadcast(this,4,new Intent("cancel"),PendingIntent.FLAG_CANCEL_CURRENT);
        contentView.setOnClickPendingIntent(R.id.cancel_notification,cancelIntent);

        builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.nopicture)
                .setContentTitle("FM")
                .setContentText("段子来了");


        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationStrId);
        }

        builder.setOngoing(true);//设置通知栏不能通过滑动删除
        notification = builder.build();
        notification.defaults= Notification.DEFAULT_SOUND;//设置声音
        notification.contentView = contentView;
        notificationManager.notify(ID,notification);
    }

    private void getDownloadNotification() {
        download_notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel 适配8.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(download_notificationStrId, download_notificationName, NotificationManager.IMPORTANCE_MIN);
            download_notificationManager.createNotificationChannel(channel);
        }
        downContentView = new RemoteViews(getPackageName(),R.layout.download_notification_layout);
        download_builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.download)
                .setContentTitle("下载任务")
                .setContentText("我正在运行");


        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            download_builder.setChannelId(download_notificationStrId);
        }
        download_builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        download_notification = download_builder.build();
        download_notification.defaults= Notification.DEFAULT_SOUND;//设置声音
        download_notification.flags = Notification.FLAG_AUTO_CANCEL;//通知被点击后自动消失
        download_notification.contentView = downContentView;
        download_notificationManager.notify(DOWNLOAD_ID,download_notification);
    }
}


