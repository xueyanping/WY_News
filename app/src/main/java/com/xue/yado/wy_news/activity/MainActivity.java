package com.xue.yado.wy_news.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioGroup;

import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.fragment.FMFragment;
import com.xue.yado.wy_news.fragment.SettingFragment;
import com.xue.yado.wy_news.fragment.XinWenFragment;
import com.xue.yado.wy_news.myView.ContentViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity{

    @BindView(R.id.viewpager)
    ContentViewPager viewPager;
    @BindView(R.id.content_radiogroup)
     RadioGroup radioGroup;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {

        fragmentList = new ArrayList<>();
        fragmentList.add(new XinWenFragment());
        fragmentList.add(new FMFragment());
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
    viewPager.setOffscreenPageLimit(3);
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
                case R.id.rb_Setting:
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    });
    radioGroup.check(R.id.rb_xinwen);
        }


}
