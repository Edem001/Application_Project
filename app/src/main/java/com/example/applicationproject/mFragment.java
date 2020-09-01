package com.example.applicationproject;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class mFragment extends Fragment {
    private TextView currentPage;
    private int page = 0;
    private Context appContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.frag_example,container,false);
        currentPage = view.findViewById(R.id.page_number);
        currentPage.setText(page+"");
        Button buttonMinus = view.findViewById(R.id.button_minus);
        Button buttonNotify = view.findViewById(R.id.button_notify);

        buttonNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNotification(appContext, page);
            }
        });

        if (page < 2)
            buttonMinus.setEnabled(false);
        else
            buttonMinus.setEnabled(true);
        return view;

    }
    void callNotification(Context context, int currentPage){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, "myAppChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle("APP")
                .setContentText("TEST NOTIFICATION")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();
        notificationManager.notify(1, notification);
    }
    void setContext(Context context){
        appContext = context;
    }
}
