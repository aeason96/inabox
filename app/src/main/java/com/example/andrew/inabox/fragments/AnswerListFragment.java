package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;

public class AnswerListFragment extends Fragment {
    public static final String TAG_ANSWER_LIST_FRAGMENT = "answer_list_fragment";

    private Game game;
    private Button btnContinue;

    public AnswerListFragment(){

    }

    public static AnswerListFragment newInstance() {
        AnswerListFragment fragment = new AnswerListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_answer_list, container, false);


        return view;
    }
}
