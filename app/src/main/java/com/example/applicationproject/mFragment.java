package com.example.applicationproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class mFragment extends Fragment {
    private TextView currentPage;
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.frag_example,container,false);
        currentPage = view.findViewById(R.id.page_number);
        currentPage.setText(page+"");
        return view;

    }
}
