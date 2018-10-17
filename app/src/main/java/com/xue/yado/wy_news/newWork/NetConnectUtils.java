package com.xue.yado.wy_news.newWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2018/8/2.
 */

public class NetConnectUtils {

    public static int No_NETWORK = -1;
    public static int WIFI_NETWORK = 1;
    public static int MOBILE_NETWORK = 0;

    public static int getNetState(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
           if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE ){
               return MOBILE_NETWORK;
           }else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
               return WIFI_NETWORK;
           }else{
               return -1;
           }
        }
        return No_NETWORK;
    }

}
