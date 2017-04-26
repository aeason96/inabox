package com.example.andrew.inabox;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrew.inabox.fragments.CreateGame;
import com.example.andrew.inabox.fragments.HomeScreenFragment;
import com.example.andrew.inabox.fragments.JoinGame;
import com.example.andrew.inabox.fragments.TaskFragment;
import com.example.andrew.inabox.interfaces.HomeScreenInteraction;
import com.example.andrew.inabox.interfaces.RetainedFragmentInteraction;

public class Game extends AppCompatActivity implements HomeScreenInteraction {

    private Fragment homeScreenFragment, taskFragment, createGame, joinGame;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        fragmentManager = getSupportFragmentManager();

        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);

        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            homeScreenFragment = new HomeScreenFragment();
            // Set dashboard fragment to be the default fragment shown
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            fragmentManager.beginTransaction().replace(R.id.game, homeScreenFragment ).commit();
        } else {
            // Get references to the fragments if they existed, null otherwise
            createGame = fragmentManager.findFragmentByTag(CreateGame.TAG_CREATE_FRAGMENT);
            joinGame = fragmentManager.findFragmentByTag(JoinGame.TAG_JOIN_FRAGMENT);
        }
    }

    @Override
    public void changeFragment(String fragment_name) {

        Fragment fragment;
        Class fragmentClass = null;
        if(fragment_name.equals(CreateGame.TAG_CREATE_FRAGMENT)){
            fragmentClass = CreateGame.class;
        }
        else if(fragment_name.equals(JoinGame.TAG_JOIN_FRAGMENT)){
            fragmentClass = JoinGame.class;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();

                if (fragment.getClass().equals(CreateGame.class)) {
                    createGame = fragment;
                }
                else if (fragment.getClass().equals(JoinGame.class)) {
                    joinGame = fragment;
                }


                FragmentTransaction ft= fragmentManager.beginTransaction();

                ft.replace(R.id.game, fragment,
                        ((RetainedFragmentInteraction)taskFragment).getActiveFragmentTag());
                ft.addToBackStack(null);
                ft.commit();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
