package com.xue.yado.wy_news.utils;

import com.xue.yado.wy_news.bean.Toutiao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class XinWenJson {
    public static Toutiao getdata(String data, String type){
        try {
            Toutiao toutiao=new Toutiao();
            JSONObject object=new JSONObject(data);
          //  LogUtils.e("xinwenjsonobject", object + "");
            JSONArray array = null;
            switch (type){

                case "toutiao":
                    array=object.getJSONArray("T1348647853363");//头条
                    break;
                case "yule":
                    array=object.getJSONArray("T1348648517839");//娱乐
                    break;
                case "tiyu":
                    array = object.getJSONArray("T1348649079062");//体育
                    break;
                case "junshi":
                    array=object.getJSONArray("T1348648141035");//军事
                    break;
            }
           // Log.i( "getdata: ","getdata: "+array);

            List<Toutiao.T1348647853363Bean> list=new ArrayList<>();
            for (int i=0;i<array.length();i++){
                Toutiao.T1348647853363Bean t1348647853363Entity = new Toutiao.T1348647853363Bean();
                JSONObject arrayobj = array.getJSONObject(i);

                if (!arrayobj.isNull("skipID")){
                    String skipId = arrayobj.getString("skipID");
                    t1348647853363Entity.setSkipID(skipId);
                }

                if (!arrayobj.isNull("replyCount")){
                    int replyCount=arrayobj.getInt("replyCount");
                    t1348647853363Entity.setReplyCount(replyCount);
                }
                if (!arrayobj.isNull("skipType")){
                    String skiptype=arrayobj.getString("skipType");
                    t1348647853363Entity.setSkipType(skiptype);
                }

                if (!arrayobj.isNull("title")){
                    String title=arrayobj.getString("title");
                    t1348647853363Entity.setTitle(title);
                }
                if (!arrayobj.isNull("digest")){
                    String digest=arrayobj.getString("digest");
                    t1348647853363Entity.setDigest(digest);
                }
                if (!arrayobj.isNull("priority")){
                    int  priority=arrayobj.getInt("priority");
                    t1348647853363Entity.setPriority(priority);
                }
                if (!arrayobj.isNull("imgsrc")){
                    String imgsrc=arrayobj.getString("imgsrc");
                    t1348647853363Entity.setImgsrc(imgsrc);
                }
                if (!arrayobj.isNull("url")){
                    String url=arrayobj.getString("url");
                    t1348647853363Entity.setUrl(url);
                }
                if (!arrayobj.isNull("url_3w")){
                    String url_3w=arrayobj.getString("url_3w");
                    t1348647853363Entity.setUrl_3w(url_3w);
                }

                if (!arrayobj.isNull("imgextra")){
                    JSONArray imagetraArray = arrayobj.getJSONArray("imgextra");
                    List<Toutiao.T1348647853363Bean.ImgextraBean> listimagestra = new ArrayList<>();
                    for (int j=0;j<imagetraArray.length();j++){
                        Toutiao.T1348647853363Bean.ImgextraBean imgextraEntity = new Toutiao.T1348647853363Bean.ImgextraBean();
                        JSONObject imagestra=imagetraArray.getJSONObject(j);
                        String imagesra=imagestra.getString("imgsrc");
                        imgextraEntity.setImgsrc(imagesra);
                        listimagestra.add(imgextraEntity);
                    }
                    t1348647853363Entity.setImgextra(listimagestra);
                }


                if (!arrayobj.isNull("ads")){
                    JSONArray adsArray=arrayobj.getJSONArray("ads");
                    List<Toutiao.T1348647853363Bean.AdsBean> adsEntities=new ArrayList<>();
                    for (int k=0;k<adsArray.length();k++){
                       // LogUtils.e("xinwenjsonads", "xinwenadsentity");
                        Toutiao.T1348647853363Bean.AdsBean ads=new Toutiao.T1348647853363Bean.AdsBean();
                        JSONObject adsobj=adsArray.getJSONObject(k);
                        String adstitle=adsobj.getString("title");
                        String subtitle=adsobj.getString("subtitle");
                        String adsurl=adsobj.getString("url");

                        String adstag=adsobj.getString("tag");
                        String adsimgsrc=adsobj.getString("imgsrc");
                        String skipID = adsobj.getString("skipID");
                        String skipType = adsobj.getString("skipType");

                        ads.setUrl(adsurl);
                        ads.setImgsrc(adsimgsrc);
                        ads.setTitle(adstitle);
                        ads.setTag(adstag);
                        ads.setSkipID(skipID);
                        ads.setSkipType(skipType);
                        ads.setSubtitle(subtitle);
                        adsEntities.add(ads);
                    }
                    t1348647853363Entity.setAds(adsEntities);
                }

                list.add(t1348647853363Entity);
            }
            toutiao.setT1348647853363(list);
            //LogUtils.e("xinwenjson", "========" + list.get(0).getTitle());
         //   for (int i=0;i<toutiao.getT1348647853363().size();i++){
          //      LogUtils.e("xinwenjson", i + "========" + toutiao.getT1348647853363().get(i).getTitle());
         //   }
           // LogUtils.e("xinwenjson", "========" + toutiao.getT1348647853363());
            return toutiao;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
