package com.example.applicationproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    private int page = 0;
    private Context appContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.frag_example,container,false);
        TextView currentPage = view.findViewById(R.id.page_number);
        currentPage.setText(page+"");
        Button buttonMinus = view.findViewById(R.id.button_minus);
        Button buttonNotify = view.findViewById(R.id.button_notify);

        buttonNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pg = getPage();
                callNotification(appContext, pg);
            }
        });

        if (page < 2)
            buttonMinus.setEnabled(false);
        else
            buttonMinus.setEnabled(true);
        return view;

    }
    private int getPage(){
        return page;
    }

    private void callNotification(Context context, int currentPage){
        Intent startIntent = new Intent(context, MainActivity.class);
        startIntent.putExtra("page", page);
        startIntent.putExtra("confirm", "YES");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, "myAppChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle("You create a notification")
                .setContentText("Notification "+ currentPage)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }
    void setContext(Context context){
        appContext = context;
    }
}
