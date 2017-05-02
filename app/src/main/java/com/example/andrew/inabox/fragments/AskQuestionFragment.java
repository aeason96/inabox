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
import android.widget.Toast;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;


public class AskQuestionFragment extends Fragment implements View.OnClickListener {
    Game game;
    EditText editTextQuestion;
    Button btnSubmit;

    public AskQuestionFragment(){

    }

    public static AskQuestionFragment newInstance() {
        AskQuestionFragment fragment = new AskQuestionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_game, container, false);

        btnSubmit= (Button) view.findViewById(R.id.btnSubmit);
        editTextQuestion = (EditText) view.findViewById(R.id.editTextQuestion);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnSubmit)){
            if (editTextQuestion.getText().toString().length() == 0){
                Toast.makeText(game.getApplicationContext(), "You're question can't be empty!!", Toast.LENGTH_SHORT);
            }
            else {
                String request = "question/" + game.getGameID();
                //value: text
                //creator: person foreign key
                //game_room: integer
            }
        }
    }
}
