package com.xue.yado.wy_news.myView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.yado.wy_news.R;

/**
 * Created by Administrator on 2018/9/7.
 */

public class MyProgressDialog extends ProgressDialog {
    private ImageView load_image;

    private TextView load_text;

    private Context context;

    private String loading_info;

    private int loading_xml;

    private AnimationDrawable drawable;

    public MyProgressDialog(Context context, String loading_info, int loading_xml) {
        super(context);
        this.context = context;
        this.loading_info = loading_info;
        this.loading_xml = loading_xml;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprogressdialog);
        initView();
        setData();
    }

    public void initView(){
        load_image = findViewById(R.id.load_image);
        load_text = findViewById(R.id.load_text);
    }

    public void setData(){
        load_text.setText(loading_info);
        load_image.setBackgroundResource(loading_xml);
        drawable = (AnimationDrawable) load_image.getBackground();
        load_image.post(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        });

    }


}
