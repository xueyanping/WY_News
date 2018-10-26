package com.xue.yado.wy_news.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.yado.wy_news.App;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.RxBus;
import com.xue.yado.wy_news.bean.MessageSocket;
import com.xue.yado.wy_news.fontsliderbar.FontSliderBar;
import com.xue.yado.wy_news.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by zsj on 2017/9/11.
 * 字体设置展示
 */

public class TextSizeShowActivity extends BaseActivity {
    @BindView(R.id.fontSliderBar)
    FontSliderBar fontSliderBar;
    @BindView(R.id.tv_chatcontent1)
    TextView tvContent1;
    @BindView(R.id.tv_chatcontent)
    TextView tvContent2;
    @BindView(R.id.tv_chatcontent3)
    TextView tvContent3;
    @BindView(R.id.tv_chatcontent4)
    TextView tvContent4;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_userhead)
    ImageView ivUserhead;
    private float textsize1, textsize2, textsize3,textsize4;
    private float textSizef;//缩放比例
    private int currentIndex;
    private boolean isClickable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
       // initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_textsizeshow;
    }

    private void initData() {
        currentIndex = App.getMyInstance().getPreferencesHelper().getValueInt("currentIndex", 1);
        textSizef = 1 + currentIndex * 0.1f;
        textsize1 = tvContent1.getTextSize() / textSizef;
        textsize2 = tvContent2.getTextSize() / textSizef;
        textsize3 = tvContent3.getTextSize() / textSizef;
        textsize4 = tvContent4.getTextSize() / textSizef;
        fontSliderBar.setTickCount(6).setTickHeight(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 15)).setBarColor(Color.GRAY)
                .setTextColor(Color.BLACK).setTextPadding(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 10)).setTextSize(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 14))
                .setThumbRadius(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 10)).setThumbColorNormal(Color.GRAY).setThumbColorPressed(Color.GRAY)
                .setOnSliderBarChangeListener(new FontSliderBar.OnSliderBarChangeListener() {
                    @Override
                    public void onIndexChanged(FontSliderBar rangeBar, int index) {
                        if(index>5){
                            return;
                        }
                        index = index - 1;
                        float textSizef = 1 + index * 0.1f;
                        setTextSize(textSizef);
                    }
                }).setThumbIndex(currentIndex).withAnimation(false).applay();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontSliderBar.getCurrentIndex() != currentIndex) {
                    if (isClickable) {
                        isClickable = false;
                        refresh();
                    }
                } else {
                    finish();
                }
            }
        });
    }

    private void setTextSize(float textSize) {
        //改变当前页面的字体大小
        tvContent1.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize1 * textSize));
        tvContent2.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize2 * textSize));
        tvContent3.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize3 * textSize));
        tvContent4.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize3 * textSize));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentIndex != fontSliderBar.getCurrentIndex()) {
                if (isClickable) {
                    isClickable = false;
                    refresh();
                }
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void refresh() {
        //存储标尺的下标
        App.getMyInstance().getPreferencesHelper().setValue("currentIndex", fontSliderBar.getCurrentIndex());
        //通知主页面重启
        RxBus.getInstance().post(MainActivity.class.getSimpleName(), new MessageSocket(99, null, null, null));
        //重启mainActivity
        RxBus.getInstance().post(MainActivity.class.getSimpleName(), new MessageSocket(99, null, null, null));
//        showMyDialog();
        //2s后关闭  延迟执行任务 重启完主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                hideMyDialog();
                finish();
            }
        }, 2000);
    }


}
