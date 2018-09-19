package com.xue.yado.wy_news.newWork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.xue.yado.wy_news.listener.CancelProgressListener;

/**
 * Created by Administrator on 2018/8/29.
 */

public class ProgressDiaologHandler extends Handler {

    public static final int SHOW_DIAOLG = 1;
    public static final int DISSMISS_DIAOLG = 2;
    private Context context;
    private boolean cancelable;
    private CancelProgressListener cancelProgressListener;
    private ProgressDialog progressDialog;

    public ProgressDiaologHandler(Context context,CancelProgressListener cancelProgressListener,boolean cancelable){
        this.context = context;
        this.cancelProgressListener = cancelProgressListener;
        this.cancelable = cancelable;

    }

    public void initProgress(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(cancelable);
            if (cancelable) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelProgressListener.cancelable();
                    }
                });
            }
        }
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }


    public void dissmissDialog(){
        if( progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case SHOW_DIAOLG:
                initProgress();
                break;
            case DISSMISS_DIAOLG:
                dissmissDialog();
                break;
            default:
                break;
        }
    }
}
