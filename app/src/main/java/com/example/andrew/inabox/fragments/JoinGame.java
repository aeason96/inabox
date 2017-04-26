package com.example.andrew.inabox.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrew.inabox.R;

public class JoinGame extends Fragment {
    public static final String TAG_JOIN_FRAGMENT = "join_fragment";

    public JoinGame() {
        // Required empty public constructor
    }
    public static JoinGame newInstance() {
        JoinGame fragment = new JoinGame();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_game, container, false);
    }
}
