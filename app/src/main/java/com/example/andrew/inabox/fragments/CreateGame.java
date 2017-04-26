package com.example.andrew.inabox.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrew.inabox.R;

public class CreateGame extends Fragment {

    public static final String TAG_CREATE_FRAGMENT = "create_fragment";

    public CreateGame() {
        // Required empty public constructor
    }

    public static CreateGame newInstance() {
        CreateGame fragment = new CreateGame();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_game, container, false);
    }
}
