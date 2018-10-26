package com.xue.yado.wy_news.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.myView.BaseViewHolder;

/**
 * Created by Administrator on 2018/9/26.
 */

public class FMAdapter extends FMRecyclerAdapter<FM.DataBean.ListBean> {

    private Context context;
    public FMAdapter(Context mContext) {
        super(mContext);
        context = mContext;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, FM.DataBean.ListBean fm, int position) {
        holder.setTextView(R.id.text,fm.getTitle());
        Long cha = (System.currentTimeMillis()-fm.getCreatedAt());
        cha = cha/1000/60/60/24;
        holder.setTextView(R.id.digest,"更新于 "+cha+" 天前");
        ImageView imageView = holder.setImageView(R.id.image);
        new BitmapUtils(context).display(imageView,fm.getCoverMiddle());
    }


}
