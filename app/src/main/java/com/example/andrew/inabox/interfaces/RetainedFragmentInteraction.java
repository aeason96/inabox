package com.example.andrew.inabox.interfaces;

import android.app.Activity;

/**
 * Created by Andrew on 4/24/2017.
 */

public interface RetainedFragmentInteraction {
    public String getActiveFragmentTag();
    public void setActiveFragmentTag(String s);
    public void startBackgroundServiceNeeded();
}
