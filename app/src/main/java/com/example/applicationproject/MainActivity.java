package com.example.applicationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {
    public static int MAX_PAGES = 1;
    public static int CUR_PAGE = 1;
    mFragment lastFragment, nextFragment;
    ViewPager viewPager;
    mAdapter adapter;
    SharedPreferences preferences;
    PreferencesWorker pw;

    /*--------------------------------------------------------------
                Inner FragmentPagerAdapter class
     -------------------------------------------------------------*/
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
    /*--------------------------------------------------------
                MainActivity's onCreate method
     -------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("PAGES_DATA", MODE_PRIVATE); // Initializing PreferencesWorker class to access saved pages data
        pw = new PreferencesWorker(preferences);

        MAX_PAGES = pw.loadPreferences("MAX_PAGES");
        try {
            if (getIntent().getStringExtra("confirm").equals("YES")) // In case of notification push intent containing string value to identify as intent  from notification
                CUR_PAGE = getIntent().getIntExtra("page", 1); // Notification intent contains current page number
            else
                CUR_PAGE = pw.loadPreferences("CUR_PAGE");  // In case of manual application start current page loading from saved data
        }catch (NullPointerException npe){
            CUR_PAGE = pw.loadPreferences("CUR_PAGE");
        }

        if (CUR_PAGE > MAX_PAGES)  // If notification requested non-existing page
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setMessage("Requested page is not existing")
                    .setTitle("Error")
                    .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int i) {}
                        })
                    .create();
            dialog.show();
            CUR_PAGE = MAX_PAGES;
        }

        adapter = new mAdapter(getSupportFragmentManager(), this); // Initializing FragmentPagerAdapter
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(CUR_PAGE-1, true); // Setting last saved page as current
        viewPager.setOffscreenPageLimit(MAX_PAGES - CUR_PAGE+1); // Temporary set offset OffscreenPageLimit as distance between max page and current to prevent application crash
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() { // Setting PageTransformer to add some visual effects at scrolling
            @Override
            public void transformPage(@NonNull View page, float position) {
               float opacity = abs(abs(position) - 1);
               page.setAlpha(opacity);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CUR_PAGE = viewPager.getCurrentItem()+1; // On scrolling pages refreshing current page's variable
                if (abs(MAX_PAGES - CUR_PAGE) < 2) // Setting default offset in OffscreenPageLimit when current page stands before or on maximum page
                    viewPager.setOffscreenPageLimit(1);
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }
/*--------------------------------------------------
           MainActivity's onDestroy method
 -------------------------------------------------*/
    @Override
    protected void onDestroy() { // Saving pages data
        pw.save("CUR_PAGE", CUR_PAGE);
        pw.save("MAX_PAGES", MAX_PAGES);
        super.onDestroy();
    }
/*------------------------------------------------------------
        "Plus" and "minus" buttons onClick function
----------------------------------------------------------- */
    public void onClick(View view){ // If user is on last page (current == max), app switching pages as them adding/deleting
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
                    adapter.notifyDataSetChanged();
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
            nextFragment = new mFragment();
            nextFragment.setArguments(args);
            viewPager.setCurrentItem(page);
        }
        else {
            lastFragment = new mFragment();
            lastFragment.setArguments(args);
            viewPager.setCurrentItem(page);
        }
    }
}
