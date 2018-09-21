package com.xue.yado.wy_news.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by Administrator on 2018/8/28.
 */

public interface RetrofitService {

   // http://c.3g.163.com/nc/article/list/T1429173762551/0-20.html 网易头条
   @GET("article/headline/T1348647853363/{startIndex}-{endIndex}.html")
   Observable<String> getWangyi_CarData(@Path("startIndex") int startIndex, @Path("endIndex") int endIndex);

   @GET(".json")
   Observable<String> getDuoTu();

   //http://c.3g.163.com/nc/article/list/T1348648517839/0-20.html 网易娱乐
   @GET("article/list/T1348648517839/{startIndex}-{endIndex}.html")
   Observable<String> getWangyi_YuLeData(@Path("startIndex") int startIndex, @Path("endIndex") int endIndex);

   //http://c.m.163.com/nc/article/list/T1348648141035/0-20.html  军事
   @GET("article/list/T1348648141035/{startIndex}-{endIndex}.html")
   Observable<String> getWangyi_JunShiData(@Path("startIndex") int startIndex, @Path("endIndex") int endIndex);

   //http://c.m.163.com/nc/article/list/T1348649079062/体育
   @GET("article/list/T1348649079062/{startIndex}-{endIndex}.html")
   Observable<String> getWangyi_TiYuData(@Path("startIndex") int startIndex, @Path("endIndex") int endIndex);
}
