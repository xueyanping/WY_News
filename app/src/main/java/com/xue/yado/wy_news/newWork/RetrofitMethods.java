package com.xue.yado.wy_news.newWork;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/8/29.
 */

public class RetrofitMethods {
    public static void retrofitObservable(Observable observable, Observer observer){

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(observer);
    }


    public static void Wngyi_CarData(int startIndex,int endIndex,Observer observer){
        retrofitObservable(MyRetrofit.getRetrofitService().getWangyi_CarData(startIndex,endIndex),observer);
    }

    public static void Wngyi_YuLeData(int startIndex,int endIndex,Observer observer){
        retrofitObservable(MyRetrofit.getRetrofitService().getWangyi_YuLeData(startIndex,endIndex),observer);
    }

    public static void Wngyi_JunShiData(int startIndex,int endIndex,Observer observer){
        retrofitObservable(MyRetrofit.getRetrofitService().getWangyi_JunShiData(startIndex,endIndex),observer);
    }

    public static void Wngyi_TiYuData(int startIndex,int endIndex,Observer observer){
        retrofitObservable(MyRetrofit.getRetrofitService().getWangyi_TiYuData(startIndex,endIndex),observer);
    }

    public static void getDuoTuData(Observer observer){
        retrofitObservable(MyRetrofit.getRetrofitService().getDuoTu(),observer);
    }



}
