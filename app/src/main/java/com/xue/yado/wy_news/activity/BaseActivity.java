package com.xue.yado.wy_news.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.vondear.rxtools.view.RxToast;
import com.xue.yado.wy_news.broadCast.NetBroadcastReceiver;
import com.xue.yado.wy_news.newWork.NetConnectUtils;
import com.xue.yado.wy_news.newWork.NetWorkUtils;


import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Administrator on 2018/10/16.
 */

public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.netEvent{

    private String[] PERMISSIONARRAY = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static NetBroadcastReceiver.netEvent event;

    private NetBroadcastReceiver receiver;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        event = this;
        receiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        //filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        //filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        dialog = new Dialog(this);
        requestPermission();

    }

    public boolean isConnect(int netType){
        if(netType == NetWorkUtils.NETWORK_WIFI ||  netType == NetWorkUtils.NETWORK_4G ||  netType == NetWorkUtils.NETWORK_3G ){
            return true;
        }
        return false;
    }

    @Override
    public void onNetChange(int netType) {
        //this.netType = netType;
        // Log.i("xue", "onNetChange:接口被回调了 ");
        if(isConnect(netType)){
            RxToast.success(this, "网络已连接", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }else{
            showNetSettingDialog();
        }
    }

    public void showNetSettingDialog(){
        AlertDialog.Builder  builder = new AlertDialog.Builder(this );
        builder.setMessage("网络未连接，请设置网络");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NetWorkUtils.openWirelessSettings(getApplicationContext());

                //startActivity(new Intent().setAction("android.settings.WIRELESS_SETTINGS"));
            }
        });
        dialog =  builder.show();
    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= M){

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                this.requestPermissions(PERMISSIONARRAY,1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0){
            int i;
            for( i = 0;i<grantResults.length;i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    break;
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("广播被销毁", "onDestroy: ");
        unregisterReceiver(receiver);
    }

    public abstract int getLayoutId();

    private long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再点一次退出应用", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
