package com.xue.yado.wy_news.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.fragment.*;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton rb_xinwen,rb_FM,rb_Music,rb_Setting;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;
    private String[] PERMISSIONARRAY = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        requestPermission();


    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= M){

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(PERMISSIONARRAY,1);
            }else{
                initView();
            }
        }else{
            initView();
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
            if(i>=grantResults.length){
                initView();
            }
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        radioGroup = findViewById(R.id.content_radiogroup);
        rb_xinwen = findViewById(R.id.rb_xinwen);
        rb_FM = findViewById(R.id.rb_FM);
        rb_Music = findViewById(R.id.rb_Music);
        rb_Setting = findViewById(R.id.rb_Setting);

        fragmentList = new ArrayList<>();
        fragmentList.add(new XinWenFragment());
        fragmentList.add(new FMFragment());
        fragmentList.add(new MusicFragment());
        fragmentList.add(new SettingFragment());

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
    viewPager.setOffscreenPageLimit(4);
    viewPager.setAdapter(adapter);
    viewPager.setCurrentItem(0);

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_xinwen:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.rb_FM:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.rb_Music:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.rb_Setting:
                    viewPager.setCurrentItem(3);

                    break;
            }
        }
    });
    radioGroup.check(R.id.rb_xinwen);
        }


}
