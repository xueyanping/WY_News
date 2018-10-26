package com.xue.yado.wy_news.newWork;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import com.xue.yado.wy_news.listener.RetrofitService;

/**
 * Created by Administrator on 2018/8/28.
 */

public class MyRetrofit  {
   // public static String BASE_URL = "http://192.168.1.80:8080/TestApp/";
//    public static String BASE_URL = "http://c.m.163.com/nc/auto/list/5bmz6aG25bGx/";网易新闻_汽车
   public static String BASE_URL = "http://c.m.163.com/nc/";//网易新闻
    public static RetrofitService retrofitService;

    public static RetrofitService getRetrofitService() {
        if (retrofitService == null) {
            synchronized (MyRetrofit.class){
                if (retrofitService == null) {
                    new MyRetrofit();
                }
            }
        }

        return retrofitService;
    }

    public MyRetrofit(){
        //读超时长，单位：毫秒
       int READ_TIME_OUT = 7676;
        //连接时长，单位：毫秒
         int CONNECT_TIME_OUT = 7676;

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
//        File cacheFile = new File(getCacheDir(), "cache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        //增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader( "User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)" )
                        .addHeader("Content-Type", "application/json")//设置允许请求json数据
                        .build();
                return chain.proceed(build);
            }
        };

        //创建一个OkHttpClient并设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
//                .addInterceptor(mRewriteCacheControlInterceptor)
//                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                //.cache(cache)
                .build();

            Retrofit  retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);

    }

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
//    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            String cacheControl = request.cacheControl().toString();
//            if (!NetWorkUtils.isConnected()) {
//                request = request.newBuilder()
//                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl
//                                .FORCE_NETWORK : CacheControl.FORCE_CACHE)
//                        .build();
//            }
//            Response originalResponse = chain.proceed(request);
//            if (NetWorkUtils.isConnected(getContext())) {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();
//            } else {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" +
//                                CACHE_STALE_SEC)
//                        .removeHeader("Pragma")
//                        .build();
//            }
//        }
//    };
}
