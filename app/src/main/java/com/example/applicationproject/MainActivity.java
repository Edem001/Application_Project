package com.example.applicationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static int MAX_PAGES = 1;
    public static int CUR_PAGE = 1;
    FragmentManager fm;
    FragmentTransaction ft;
    mFragment last_fragment, current_fragment, next_fragment;
    Button button_minus, buton_plus, button_notify;
    ViewPager viewPager;
    mAdapter adapter;
    SharedPreferences preferences;

    public static class mAdapter extends FragmentPagerAdapter {
        mAdapter(@NonNull FragmentManager fm, Context context){
            super(fm);
            this.context = context;
        }
    Context context;
        @Override
        public Fragment getItem(int position) {
            mFragment previousFragment, currentFragment, nextFragment;
            Bundle args = new Bundle();
                    currentFragment = new mFragment();
                    args.putInt("page", CUR_PAGE);
                    currentFragment.setArguments(args);
                    currentFragment.setContext(context);
                    return  currentFragment;

        }

        @Override
        public int getCount() {
            return MAX_PAGES;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("PAGES_DATA", MODE_PRIVATE);
        preferencesWorker pw = new preferencesWorker(preferences);

        adapter = new mAdapter(getSupportFragmentManager(), this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CUR_PAGE = viewPager.getCurrentItem()+1;

            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_plus:
                if(CUR_PAGE == MAX_PAGES)
                {
                    MAX_PAGES++;
                    CUR_PAGE++;
                    switchPageOnButtonPress(CUR_PAGE-1, false);
                }
                else {
                    MAX_PAGES++;
                }

                break;
            case R.id.button_minus:
                if(MAX_PAGES > 1){
                    if (MAX_PAGES <= CUR_PAGE){
                        MAX_PAGES--;
                        CUR_PAGE--;
                        switchPageOnButtonPress(CUR_PAGE-1, true);
                    }
                    if (CUR_PAGE < MAX_PAGES){
                        MAX_PAGES--;
                    }
                    adapter.notifyDataSetChanged();
                }
        }
    }
    void switchPageOnButtonPress(int page, boolean reverse){
        Bundle args = new Bundle();
        args.putInt("page", page);
        adapter.notifyDataSetChanged();
        if (!reverse) {
            next_fragment = new mFragment();
            next_fragment.setArguments(args);
            viewPager.setCurrentItem(page);
        }
        else {
            last_fragment = new mFragment();
            last_fragment.setArguments(args);
            viewPager.setCurrentItem(page);
        }
    }
}
