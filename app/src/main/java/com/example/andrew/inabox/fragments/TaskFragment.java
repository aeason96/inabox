package com.example.andrew.inabox.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.interfaces.RetainedFragmentInteraction;
import com.example.andrew.inabox.service.BackgroundService;

/**
 * Created by Andrew on 4/24/2017.
 */
public class TaskFragment extends Fragment implements RetainedFragmentInteraction {

    public static final String TAG_TASK_FRAGMENT = "task_fragment";
    private String mActiveFragmentTag;
    Game game;
    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = (Game) getActivity();
        setRetainInstance(true);
        if (!isBackgroundServiceRunning()) {
            startBackgroundServiceNeeded();
        }

    }
    public TaskFragment() {
        // Required empty public constructor
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    public String getActiveFragmentTag() {
        return mActiveFragmentTag;
    }

    public void setActiveFragmentTag(String s) {
        mActiveFragmentTag = s;
    }

    // checks if the background service is running
    public boolean isBackgroundServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d("background_service", "Checking");
            if (BackgroundService.class.getName().equals(service.service.getClassName())) {
                Log.d("background_service", "BackgroundService is already running!");
                return true;
            }
        }
        return false;
    }


    @Override
    public void startBackgroundServiceNeeded() {

        // check if the background service is running, if not then start it
        if (!isBackgroundServiceRunning()) {
            Intent intent = new Intent(getActivity(), BackgroundService.class);
            Bundle data = intent.getExtras();
            //data.putString("player_id", );
            getActivity().startService(intent);
            Log.d("background_service", "BackgroundService  TOLD TO START!");

        }

    }

}

