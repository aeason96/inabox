package com.example.andrew.inabox.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andrew.inabox.R;
import com.example.andrew.inabox.interfaces.HomeScreenInteraction;

/**
 * Created by Andrew on 4/24/2017.
 */

public class HomeScreenFragment extends Fragment implements View.OnClickListener {


    public static final String TAG_HOME_FRAGMENT = "home_fragment";

    private Button joinGame, createGame;
    private HomeScreenInteraction activity;

    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        return fragment;
    }

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeScreenInteraction) {
            activity = (HomeScreenInteraction) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeScreenInteraction");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        createGame = (Button)view.findViewById(R.id.createGame);
        joinGame = (Button)view.findViewById(R.id.joinGame);

        createGame.setOnClickListener(this);
        joinGame.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        System.out.println("Create id = " + R.id.createGame);
        System.out.println("Join id = " + R.id.joinGame);
        System.out.println("View v = " + v.getId());
        if(v.equals(createGame)){
            System.out.println("CREATE GAME PRESSED");
            activity.changeFragment(CreateGame.TAG_CREATE_FRAGMENT);
        }
        if(v.equals(joinGame)){
            System.out.println("JOIN GAME PRESSED");
            activity.changeFragment(JoinGameFragment.TAG_JOIN_FRAGMENT);
        }
    }
}
