package com.example.andrew.inabox.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.andrew.inabox.interfaces.RetainedFragmentInteraction;

/**
 * Created by Andrew on 4/24/2017.
 */
public class TaskFragment extends Fragment implements RetainedFragmentInteraction {

    public static final String TAG_TASK_FRAGMENT = "task_fragment";
    private String mActiveFragmentTag;

    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


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

}

