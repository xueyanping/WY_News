package com.xue.yado.wy_news.activity;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;


import android.widget.RadioButton;
import android.widget.RadioGroup;


import java.util.ArrayList;
import java.util.List;


import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.fragment.*;



public class MainActivity extends BaseActivity{

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton rb_xinwen,rb_FM,rb_Music,rb_Setting;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
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
