package com.xue.yado.wy_news.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.xue.yado.wy_news.RxBus;
import com.xue.yado.wy_news.broadCast.NetBroadcastReceiver;
import com.xue.yado.wy_news.newWork.NetWorkUtils;
import com.xue.yado.wy_news.utils.SharedPreferenceUtil;

import io.reactivex.Observable;

import static android.os.Build.VERSION_CODES.M;
import static com.xue.yado.wy_news.newWork.NetWorkUtils.NETWORK_4G;
import static com.xue.yado.wy_news.newWork.NetWorkUtils.NETWORK_WIFI;

/**
 * Created by Administrator on 2018/10/16.
 */

public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.netEventListener{

    private String[] PERMISSIONARRAY = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private NetBroadcastReceiver receiver;
    private Dialog dialog;
    private Observable observable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        receiver = new NetBroadcastReceiver();
        receiver.setEventListener(this);
        IntentFilter filter = new IntentFilter();
        //filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        //filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        dialog = new Dialog(this);
        requestPermission();

        /**
         * 设置字体相关
         */
//        observable = RxBus.getInstance().register(this.getClass().getSimpleName(), MessageSocket.class);
//        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MessageSocket>() {
//
//            @Override
//            public void accept(MessageSocket message) throws Exception {
//                rxBusCall(message);
//            }
//        });

    }

    public boolean isConnect(int netType){
        if(netType == NETWORK_WIFI ||  netType == NETWORK_4G ||  netType == NetWorkUtils.NETWORK_3G ){
            return true;
        }
        return false;
    }

    @Override
    public void onNetChange(int netType) {
        SharedPreferenceUtil.saveNetState(this,netType,"newState");
        String net = null;
        if(isConnect(netType)){
            switch (netType){
                case NETWORK_WIFI:
                    net = "wifi";
                    break;
                case NETWORK_4G:
                    net = "4G";
                    break;
            }
            RxToast.success(this, net+"网络已连接", Toast.LENGTH_SHORT).show();
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

    /********************************************************设置字体相关 ******************/
//    public void rxBusCall(MessageSocket message) {
//    }
//
//
//
//
//    public int getColorById(int resId) {
//        return ContextCompat.getColor(this, resId);
//    }
//
//
//    public void goActivity(Class<?> activity) {
//        Intent intent = new Intent();
//        intent.setClass(getApplicationContext(), activity);
//        startActivity(intent);
//    }
//
//
//
//
//    //重写字体缩放比例 api<25
//    @Override
//    public Resources getResources() {
//        Resources res =super.getResources();
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
//            Configuration config = res.getConfiguration();
//            config.fontScale= App.getMyInstance().getFontScale();//1 设置正常字体大小的倍数
//            res.updateConfiguration(config,res.getDisplayMetrics());
//        }
//        return res;
//    }
//    //重写字体缩放比例  api>25
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
//            final Resources res = newBase.getResources();
//            final Configuration config = res.getConfiguration();
//            config.fontScale= App.getMyInstance().getFontScale();//1 设置正常字体大小的倍数
//            final Context newContext = newBase.createConfigurationContext(config);
//            super.attachBaseContext(newContext);
//        }else{
//            super.attachBaseContext(newBase);
//        }
//    }

    /********************************************************设置字体相关 ******************/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("广播被销毁", "onDestroy: ");
        unregisterReceiver(receiver);
        RxBus.getInstance().unregister(this.getClass().getSimpleName(), observable);
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
