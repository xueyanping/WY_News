package com.xue.yado.wy_news.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.xue.yado.wy_news.activity.BaseActivity;
import com.xue.yado.wy_news.newWork.NetConnectUtils;
import com.xue.yado.wy_news.newWork.NetWorkUtils;


/**
 * Created by Administrator on 2018/8/2.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

        netEvent event = BaseActivity.event;
    @Override
    public void onReceive(Context context, Intent intent) {
        //若相等则网络状态发生变化
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            Log.i("xue", "onReceive:网络状态发生变化 ");
            int netState = NetWorkUtils.getNetWorkType(context);
            event.onNetChange(netState);
        }

    }

    public interface netEvent{
        void onNetChange(int netType);
    }


}
