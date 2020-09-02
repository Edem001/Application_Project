package com.example.applicationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {
    public static int MAX_PAGES = 1;
    public static int CUR_PAGE = 1;
    mFragment last_fragment, next_fragment;
    ViewPager viewPager;
    mAdapter adapter;
    SharedPreferences preferences;
    preferencesWorker pw;

    public static class mAdapter extends FragmentPagerAdapter {
        mAdapter(@NonNull FragmentManager fm, Context context){
            super(fm);
            this.context = context;
        }
    Context context;
        @Override
        public Fragment getItem(int position) {
            mFragment fragment;
            Bundle args = new Bundle();
                    fragment = new mFragment();
                    args.putInt("page", position+1);
                    fragment.setArguments(args);
                    fragment.setContext(context);
                    return  fragment;

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
        pw = new preferencesWorker(preferences);

        MAX_PAGES = pw.loadPreferences("MAX_PAGES");
        try {
            if (getIntent().getStringExtra("confirm").equals("YES"))
                CUR_PAGE = getIntent().getIntExtra("page", 1);
            else
                CUR_PAGE = pw.loadPreferences("CUR_PAGE");
        }catch (NullPointerException npe){
            CUR_PAGE = pw.loadPreferences("CUR_PAGE");
        }
        Log.d("SAVE", "MAX: "+ MAX_PAGES+ " ; CURRENT: "+ CUR_PAGE);


        adapter = new mAdapter(getSupportFragmentManager(), this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(CUR_PAGE-1, true);
        viewPager.setOffscreenPageLimit(MAX_PAGES);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
               float opacity = abs(abs(position) - 1);
               page.setAlpha(opacity);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CUR_PAGE = viewPager.getCurrentItem()+1;
                if (abs(MAX_PAGES - CUR_PAGE) < 2)
                    viewPager.setOffscreenPageLimit(1);
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    @Override
    protected void onDestroy() {
        pw.save("CUR_PAGE", CUR_PAGE);
        pw.save("MAX_PAGES", MAX_PAGES);
        super.onDestroy();
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
