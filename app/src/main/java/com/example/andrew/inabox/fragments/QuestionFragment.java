package com.example.andrew.inabox.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;


public class QuestionFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_QUESTION_FRAGMENT = "question_fragment";
    Game game;

    public QuestionFragment(){

    }

    public static QuestionFragment newInstance() {
        QuestionFragment fragment = new QuestionFragment();
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
