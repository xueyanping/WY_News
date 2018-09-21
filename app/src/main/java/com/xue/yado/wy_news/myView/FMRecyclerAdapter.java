package com.xue.yado.wy_news.myView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.bean.FM;
import com.xue.yado.wy_news.bean.Toutiao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */

public class FMRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView mRecyclerView;

    private List<FM.DataBean.ListBean> toutiao_list = new ArrayList<>();
    private Context context;

    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;
    private OnItemClickListener listener;

    public FMRecyclerAdapter(Context mContext) {
        //this.toutiao_list = data;
        this.context = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new MyHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new MyHolder(VIEW_HEADER);
        } else {
            return new MyHolder(getLayout(R.layout.hadrecycler_itemview,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView())
                position--;
            TextView title = holder.itemView.findViewById(R.id.text);
            ImageView image = holder.itemView.findViewById(R.id.image);
           TextView time = holder.itemView.findViewById(R.id.digest);
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            if (toutiao_list.get(position).getTitle() == null || toutiao_list.get(position).getTitle().equals("")) {
                title.setText("暂无标题");
            } else {
                title.setText(toutiao_list.get(position).getTitle());
            }
            if (toutiao_list.get(position).getCoverMiddle() == null || toutiao_list.get(position).getCoverMiddle().equals("")) {
                image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.nopicture));
            } else {
                bitmapUtils.display(image, toutiao_list.get(position).getCoverMiddle());
            }
           Long cha = (System.currentTimeMillis()-toutiao_list.get(position).getCreatedAt());
           cha = cha/1000/60/60/24;
           time.setText("更新于"+cha+"天前");

        }

    }

    @Override
    public int getItemCount() {
        int count = (toutiao_list == null ? 0 : toutiao_list.size());
        if (VIEW_FOOTER != null) {
            count++;
        }

        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && mRecyclerView != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getLayout(int layoutId,ViewGroup parent,boolean b) {
        return LayoutInflater.from(context).inflate(layoutId, parent,b);
    }

    public void addHeaderView(View headerView) {
        //if (haveHeaderView()) {
           // throw new IllegalStateException("hearview has already exists!");
     //   } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
      //  }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            //throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void addDatas(List<FM.DataBean.ListBean> rows) {
        toutiao_list.addAll(rows);
        notifyDataSetChanged();
        Log.i( "addDatas: ","addDatas: "+toutiao_list.size());
    }

    public void clearDatas() {
        toutiao_list.clear();
        notifyDataSetChanged();
    }


    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
    }

    public boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.itemClick(this.getAdapterPosition(),toutiao_list);
        }
    }



    public interface OnItemClickListener{
        void itemClick(int position, List<FM.DataBean.ListBean> toutiao_list);
    }

    public void setOnItemClick(OnItemClickListener listener){
        this.listener = listener;
    }


}
