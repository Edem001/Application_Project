package com.example.applicationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static int MAX_PAGES = 1;
    public static int CUR_PAGE = 1;
    FragmentManager fm;
    FragmentTransaction ft;
    mFragment last_fragment, current_fragment, next_fragment;
    Button button_minus, buton_plus, button_notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = new Bundle();
        args.putInt("page", 1);

        current_fragment = new mFragment();
        current_fragment.setArguments(args);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.displayed_fragment, current_fragment);
        ft.commit();

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_plus:
                if(CUR_PAGE == MAX_PAGES)
                {
                    MAX_PAGES++;
                    CUR_PAGE++;
                    switchPage(CUR_PAGE, false);
                }
                else {
                    MAX_PAGES++;
                }

                break;
            case R.id.button_minus:
                if(MAX_PAGES > 1){
                    if (MAX_PAGES == CUR_PAGE){
                        MAX_PAGES--;
                        CUR_PAGE--;
                        switchPage(CUR_PAGE, true);
                    }
                    if (CUR_PAGE < MAX_PAGES){
                        MAX_PAGES--;
                    }

                }
        }
    }
    void switchPage(int page, boolean reverse){
        Bundle args = new Bundle();
        args.putInt("page", page);

        if (!reverse) {
            next_fragment = new mFragment();
            next_fragment.setArguments(args);

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.displayed_fragment, next_fragment);
            ft.commit();

            last_fragment = current_fragment;
            current_fragment = next_fragment;
            next_fragment = null;
        }
        else {
            last_fragment = new mFragment();
            last_fragment.setArguments(args);

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.displayed_fragment, last_fragment);
            ft.commit();

            next_fragment = current_fragment;
            current_fragment = last_fragment;
            last_fragment = null;

        }
    }
}
