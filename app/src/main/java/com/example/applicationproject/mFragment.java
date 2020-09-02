package com.example.applicationproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
        Button buttonPlus = view.findViewById(R.id.button_plus);
        Button buttonNotify = view.findViewById(R.id.button_notify);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) // "Foreground bug" fix on API lower, than 24
            fixForeground(buttonMinus,buttonPlus);

        buttonNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pg = getPage();
                callNotification(appContext, pg);
            }
        });

        if (page < 2) // Enabling or disabling "minus" button depending on page number
        {
            buttonMinus.setEnabled(false);
            buttonMinus.setAlpha(0);
        }
        else
        {
            buttonMinus.setEnabled(true);
            buttonMinus.setAlpha(1);
        }
        return view;

    }
    private int getPage(){
        return page;
    }

    private void fixForeground(Button minus, Button plus){
        minus.setText("-");
        plus.setText("+");
    }
/*----------------------------------------------------------------
                        Notifications function
 ---------------------------------------------------------------*/
    private void callNotification(Context context, int currentPage){

        String CHANNEL_ID = "ApplicationAppID", CHANNEL_NAME = "ApplicationApp";

        Intent startIntent = new Intent(context, MainActivity.class); // Notification intent
        startIntent.putExtra("page", page);
        startIntent.putExtra("confirm", "YES");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

         // Notification builder
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID);
                notification.setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentTitle("You created a notification")
                .setContentText("Notification " + currentPage)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.enableLights(true);
            channel.setShowBadge(true);
            notification.setChannelId(CHANNEL_ID);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                notificationManager.createNotificationChannel(channel);
            }catch (NullPointerException npe)
            {
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setMessage("Error occurred during creating notification channel")
                        .setTitle("Error")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int i) {}
                                })
                        .create();
                dialog.show();
            }
            notificationManager.notify(0, notification.build());
        }else {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, notification.build());
        }

    }
    void setContext(Context context){
        appContext = context;
    }
}
