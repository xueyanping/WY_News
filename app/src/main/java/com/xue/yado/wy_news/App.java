package com.xue.yado.wy_news;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.vondear.rxtools.RxTool;
import com.xue.yado.wy_news.utils.CacheUtils;
import com.xue.yado.wy_news.utils.PreferencesHelper;

/**
 * Created by Administrator on 2018/9/27.
 */

public class App extends Application {

    private HttpProxyCacheServer proxy;
    private PreferencesHelper ph;
    private static App instance;

    // 单例模式获取唯一的Application实例
    public static Application getInstance() {
        return instance.getApplication();
    }
  public static App getMyInstance() {
        return instance;
    }

    private Application getApplication(){
        return  this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
       // instance = this;//初始化
        RxTool.init(this);
       // ph = new PreferencesHelper(getApplication(), "text");
    }

    public PreferencesHelper getPreferencesHelper() {
        return ph;
    }

    /**
     *
     * @return 获取字体缩放比例
     */
    public float getFontScale(){
        int currentIndex= ph.getValueInt("currentIndex",1);
        return 1+currentIndex*0.1f;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(CacheUtils.getVideoCacheDir(this))
                .build();
    }





}