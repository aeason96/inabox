package com.example.andrew.inabox.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;

/**
 * Created by kevincianfarini on 5/2/17.
 */

public class AnswerQuestionFragment extends Fragment {

    public static final String TAG_ANSWER_QUESTION_FRAGMENT = "answer_question_fragment";

    private Game game;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_answer_question, container, false);
        return view;
    }
}
