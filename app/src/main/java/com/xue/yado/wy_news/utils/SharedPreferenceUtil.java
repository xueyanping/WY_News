package com.xue.yado.wy_news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/10/22.
 */

public class SharedPreferenceUtil {

    private static String DATABASE_NAME = "news_cache";

    private static SharedPreferences sp;

   public static void saveData(Context context,String type,String data){
       if(sp == null){
           sp = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
       }
       sp.edit().putString(type,data).commit();
   }

   public static String getData(Context context,String type){
       if(sp == null){
           sp = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
       }
       return sp.getString(type,null);
   }

   public static void saveNetState(Context context,int state,String netState){
       if(sp == null){
           sp = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
       }
       sp.edit().putInt(netState,state).commit();
   }

    public static int getNetState(Context context,String netState){
        if(sp == null){
            sp = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE);
        }
        return sp.getInt(netState,-1);
    }

}
