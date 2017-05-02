package com.example.andrew.inabox.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;

public class CreateGameWaitFragment extends Fragment implements View.OnClickListener{
    public static final String TAG_CREATE_GAME_WAIT_FRAGMENT = "create_game_wait_fragment";
    Game game;

    public CreateGameWaitFragment(){

    }

    public static CreateGameWaitFragment newInstance() {
        CreateGameWaitFragment fragment = new CreateGameWaitFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_game_wait, container, false);

        return view;
    }
    @Override
    public void onClick(View v) {

    }
}
