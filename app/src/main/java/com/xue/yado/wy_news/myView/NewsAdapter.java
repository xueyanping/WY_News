package com.xue.yado.wy_news.myView;

/**
 * Created by Administrator on 2018/9/7.
 */

public class NewsAdapter {
    public static final int DUOTU = 1;

    public static final int ZHUANTI = 2;

    public static final int PUTONG = 3;

        public static int getSkipType(String type){
            if(type.equals("photoset")){
                return DUOTU;
            }
            if (type.equals("special")){
                return ZHUANTI;
            }

            if(type == null){
                return PUTONG;
            }
            return PUTONG;
    }

}
