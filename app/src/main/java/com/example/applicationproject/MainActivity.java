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
    public void onClick(){

    }

}
