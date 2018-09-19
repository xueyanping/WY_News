package com.xue.yado.wy_news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.myView.MyProgressDialog;

public class NewsContentActivity extends Activity implements View.OnClickListener{

    String title;
    String content_url;
    private ImageButton back;
    private WebView webView;
    WebSettings settings;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content_url = intent.getStringExtra("content_url");
        initView();
        initData();
        clickEvent();
   // Log.i( "onCreate: ","11"+title);
}

    private void initView() {
        //tv_title = findViewById(R.id.tv_title);
        back = findViewById(R.id.back);
        webView = findViewById(R.id.webview);
    }

    public void initData(){
     //   tv_title .setText(title);
        settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setSupportZoom(true);  //支持缩放
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.setWebChromeClient(new WebChromeClient());// 支持运行特殊的javascript(例如：alert())
        final MyProgressDialog dialog = new MyProgressDialog(this,"正在加载...",R.drawable.progressdialoganim);
        dialog.show();
        webView.loadUrl(content_url);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                LogUtils.e("putongframenturl", "==" + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }

            }
    });

    }

    public void clickEvent(){
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
               NewsContentActivity.this.finish();
        }
    }
}
