package com.xue.yado.wy_news.fragment;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.vondear.rxtools.view.RxToast;
import com.xue.yado.wy_news.R;
import com.xue.yado.wy_news.activity.MainActivity;

/**
 * Created by Administrator on 2018/9/12.
 */

public class SettingFragment extends android.support.v4.app.Fragment {
    private DrawerLayout drawer_layout;
    private FrameLayout fl_container;
    private NavigationView navigation_view;
    private Toolbar toolbar;
    private Fragment currentFragment;
    private int currentPos = -1;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shezhi_view,container,false);
        initViews(view);
        initEvents();
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawer_layout, toolbar, 0, 0);
        drawerToggle.syncState();
        selectItem(0);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initViews(View view) {
        drawer_layout = view.findViewById(R.id.drawer_layout);
        fl_container = view.findViewById(R.id.fl_container);
        navigation_view = view.findViewById(R.id.navigation_view);
        toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar=((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.home);
        }

    }

    public void initEvents(){
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_navigation_0:
                        selectItem(0);
                        break;

                    case R.id.my_navigation_1:
                        selectItem(1);
                    break;
                    case R.id.my_navigation_2:
                        RxToast.info("夜间模式");
                        break;
                    case R.id.my_navigation_3:
                        selectItem(3);
                    break;
                    case R.id.my_navigation_4:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    break;
                }
                drawer_layout.closeDrawer(GravityCompat.START);//点击后关闭测滑菜单
            return true;
            }

        });
    }


    private void selectItem(int pos) {
        //点击的正是当前正在显示的，直接返回
        if (currentPos == pos) return;
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment != null) {
            //隐藏当前正在显示的fragment
            transaction.hide(currentFragment);
        }
        currentPos = pos;
        Fragment fragment = manager.findFragmentByTag(getTag(pos));
        //通过findFragmentByTag判断是否已存在目标fragment，若存在直接show，否则去add
        if (fragment != null) {
            currentFragment = fragment;
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fl_container, getFragment(pos), getTag(pos));
        }

        transaction.commitAllowingStateLoss();

    }

    private Fragment getFragment(int pos) {
        switch (pos){
            case 0:
                currentFragment = new Setting_MainFragment();
                break;
            case 1:
                currentFragment = new Function_SetFragment();
                break;

            case 3:
                currentFragment = new About_Fragment();
                break;

        }
        return currentFragment;
    }

    private String getTag(int pos) {
        return "fg_tag_" + pos;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);//点击后打开测滑菜单
                break;

        }
        return true;
    }
}
