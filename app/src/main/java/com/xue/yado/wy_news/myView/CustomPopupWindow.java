package com.xue.yado.wy_news.myView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.xue.yado.wy_news.R;

/**
 * Created by Administrator on 2018/10/8.
 */

public class CustomPopupWindow extends PopupWindow implements View.OnClickListener {

    private Button  btnCancel;
    private ImageView download,like;
    private View mPopView;
    private OnItemClickListener mListener;
    Context context;
    public CustomPopupWindow(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.popwindow_layout, null);
        btnCancel = (Button) mPopView.findViewById(R.id.id_btn_cancelo);
        download = mPopView.findViewById(R.id.download);
        like = mPopView.findViewById(R.id.like);
        btnCancel.setOnClickListener(this);
        download.setOnClickListener(this);
        like.setOnClickListener(this);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        //        获取屏幕宽高
        WindowManager wm = (WindowManager) context .getSystemService(Context.WINDOW_SERVICE);
       // int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();


        this.setContentView(mPopView);// 设置View
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(height/4);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.popwindow_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        this.setOutsideTouchable(true);
//        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

}

