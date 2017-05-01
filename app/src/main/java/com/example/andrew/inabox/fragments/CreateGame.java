package com.example.andrew.inabox.fragments;

import android.app.Activity;
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

public class CreateGame extends Fragment implements View.OnClickListener {

    public static final String TAG_CREATE_FRAGMENT = "create_fragment";
    Game game;
    EditText gameName;
    EditText gamePassword;
    Button startButton;

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
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_game, container, false);

        gameName = (EditText) view.findViewById(R.id.game_name);
        gamePassword = (EditText) view.findViewById(R.id.game_password);
        startButton = (Button) view.findViewById(R.id.start_game);

        startButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        //Assumes you clicked start game
        if (!gameName.getText().toString().equals("") &&
                !gamePassword.getText().toString().equals("")) {
            //Start Game
            game.createGame(gameName.getText().toString(), gamePassword.getText().toString());
        }
        else {
            Toast noGameName = Toast.makeText(game.getApplicationContext(),
                    "Game Name and Password Cannot Be Left Blank.",
                    Toast.LENGTH_SHORT);
            noGameName.show();
        }
    }
}
