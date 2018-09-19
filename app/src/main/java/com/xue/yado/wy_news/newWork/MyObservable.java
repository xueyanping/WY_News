package com.xue.yado.wy_news.newWork;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import com.xue.yado.wy_news.listener.*;

/**
 * Created by Administrator on 2018/8/29.
 */

public class MyObservable<T> implements Observer<T>,CancelProgressListener {
    private ObservableOnNextListener listener;
    private Context context;
    private ProgressDiaologHandler handler;
    private Disposable d;
    String TAG = "xue";

    public MyObservable(ObservableOnNextListener listener,Context context){
        this.listener = listener;
        this.context = context;
        handler = new ProgressDiaologHandler(context,this,true);
    }
    private void showprogessDialog() {
     if(handler != null){
         handler.obtainMessage(ProgressDiaologHandler.SHOW_DIAOLG).sendToTarget();
     }
    }

    private void dismissProgressDialog() {
        if(handler != null){
            handler.obtainMessage(ProgressDiaologHandler.DISSMISS_DIAOLG).sendToTarget();
        }
        handler = null;
    }
    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: "+e.toString());
        dismissProgressDialog();
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onCompleted ");
        dismissProgressDialog();
    }



    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        showprogessDialog();
    }

    @Override
    public void onNext(T t) {
       // Log.i(TAG, "onNext: "+t);
        listener.onNext(t);
    }

    @Override
    public void cancelable() {
        //如果处于订阅状态，则取消订阅
        if(!d.isDisposed()){
            d.dispose();
        }
    }
}
