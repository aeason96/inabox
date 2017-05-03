package com.example.andrew.inabox.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andrew on 5/3/2017.
 */

public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // happens when service starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // We need to initialize an intent filter that will recognize 'ACTION_TIME_TICK'
        IntentFilter filter = new IntentFilter(
                Intent.ACTION_TIME_TICK);

        // We need to register our local broadcast receiver
        registerReceiver(receiver, filter);

        Log.d("background_service", "BackgroundService Started!");

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.d("background_service", "BackgroundService Stopped!");

        //inside onDestroy you need to 'unregister' the broadcast receiver
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    // BroadcastRecevier receiver
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // extracing the string that the action is bringin in
            String action = intent.getAction();


            Log.d("broadcast_service", "action received:" + action.toString());

            update();

        }

    };

    private void update() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int second = c.get(Calendar.SECOND);
                if (second % 4 == 0) {
                    System.out.println("TIME TICK!!!");
                }
            }

        },0,1000);//Update every second
    }
}
