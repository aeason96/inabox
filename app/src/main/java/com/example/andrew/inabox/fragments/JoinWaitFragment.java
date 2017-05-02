package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;

public class JoinWaitFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_JOIN_WAIT_FRAGMENT = "join_wait_fragment";

    Game game;

    public JoinWaitFragment(){

    }

    public static JoinWaitFragment newInstance() {
        JoinWaitFragment fragment = new JoinWaitFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        return view;
    }
    @Override
    public void onClick(View v) {

    }
}
