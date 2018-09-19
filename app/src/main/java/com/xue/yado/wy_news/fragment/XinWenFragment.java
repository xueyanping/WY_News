package com.xue.yado.wy_news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xue.yado.wy_news.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2018/9/12.
 */

public class XinWenFragment extends android.support.v4.app.Fragment {

    private ViewPager xinwen_viewpager;
    private List<Fragment> list_frag;

    View view;
    private RadioGroup radioGroup;
    private RadioButton xinwen_rb1,xinwen_rb2,xinwen_rb3,xinwen_rb4;
    private TextView xinwen_indicator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        list_frag = new ArrayList<>();
        NewsRecyclerFragment fragment1 = new NewsRecyclerFragment();
        Bundle toutiao = new Bundle();
        toutiao.putString("xinwen","toutiao");
        fragment1.setArguments(toutiao);
        list_frag.add(fragment1);

        NewsRecyclerFragment fragment2 = new NewsRecyclerFragment();
        Bundle yule = new Bundle();
        yule.putString("xinwen","yule");
        fragment2.setArguments(yule);
        list_frag.add(fragment2);

        NewsRecyclerFragment fragment3 = new NewsRecyclerFragment();
        Bundle junshi = new Bundle();
        junshi.putString("xinwen","junshi");
        fragment3.setArguments(junshi);
        list_frag.add(fragment3);

        NewsRecyclerFragment fragment4 = new NewsRecyclerFragment();
        Bundle tiyu = new Bundle();
        tiyu.putString("xinwen","tiyu");
        fragment4.setArguments(tiyu);
        list_frag.add(fragment4);
    }

    private void initView() {
        xinwen_viewpager = view.findViewById(R.id.xinwen_viewpager);
        radioGroup = view.findViewById(R.id.xinwen_radiogroup);
        xinwen_rb1 = view.findViewById(R.id.xinwen_rb1);
        xinwen_rb2 = view.findViewById(R.id.xinwen_rb2);
        xinwen_rb3 = view.findViewById(R.id.xinwen_rb3);
        xinwen_rb4 = view.findViewById(R.id.xinwen_rb4);
        xinwen_rb1.setChecked(true);
        xinwen_indicator = view.findViewById(R.id.xinwen_indicator);
        xinwen_viewpager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list_frag.get(position);
            }

            @Override
            public int getCount() {
                return list_frag.size();
            }
        });

        xinwen_viewpager.setOffscreenPageLimit(4);
        xinwen_viewpager.setCurrentItem(0);
        xinwen_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels/radioGroup.getChildCount();         // 屏幕宽度（像素）
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xinwen_indicator
                        .getLayoutParams();

                params.leftMargin = (int) ((position + positionOffset) * width)+xinwen_rb1.getWidth()/6;
                xinwen_indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

                RadioButton rb = (RadioButton) radioGroup.getChildAt(position);
                rb.setChecked(true);
                xinwen_viewpager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //选中的RadioButton播放动画
                ScaleAnimation sAnim = new ScaleAnimation(1, 1.1f, 1, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                sAnim.setDuration(500);
                sAnim.setFillAfter(true);
                //遍历所有的RadioButton
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioBtn = (RadioButton) group.getChildAt(i);
                    if (radioBtn.isChecked()) {
                        radioBtn.startAnimation(sAnim);
                    } else {
                        radioBtn.clearAnimation();
                    }
                }

                switch (checkedId){
                    case R.id.xinwen_rb1:
                        xinwen_viewpager.setCurrentItem(0);
                        break;
                    case R.id.xinwen_rb2:
                        xinwen_viewpager.setCurrentItem(1);
                        break;
                    case R.id.xinwen_rb3:
                        xinwen_viewpager.setCurrentItem(2);
                        break;
                    case R.id.xinwen_rb4:
                        xinwen_viewpager.setCurrentItem(3);
                        break;
                }
            }
        });
  }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.xinwen_view,container,false);
         initView();
        return view;
    }
}
