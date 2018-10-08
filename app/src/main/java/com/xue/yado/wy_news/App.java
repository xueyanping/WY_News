package com.xue.yado.wy_news;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.Cache;
import com.danikula.videocache.HttpProxyCacheServer;
import com.xue.yado.wy_news.utils.CacheUtils;

/**
 * Created by Administrator on 2018/9/27.
 */

public class App extends Application {

    private HttpProxyCacheServer proxy;

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