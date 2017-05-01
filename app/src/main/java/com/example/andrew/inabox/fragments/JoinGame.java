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

public class JoinGame extends Fragment implements View.OnClickListener {

    public static final String TAG_JOIN_FRAGMENT = "join_fragment";
    Game game;
    EditText gameName;
    EditText gamePassword;
    EditText playerName;
    Button joinButton;

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
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_join_game, container, false);

        gameName = (EditText) view.findViewById(R.id.game_name);
        gamePassword = (EditText) view.findViewById(R.id.game_password);
        playerName = (EditText) view.findViewById(R.id.player_name);
        joinButton = (Button) view.findViewById(R.id.join_button);

        return view;
    }

    @Override
    public void onClick(View v) {
        //Assumes you clicked join game
        String name = gameName.getText().toString();
        String password = gamePassword.getText().toString();
        String playerName = playerName.getText().toString();

        game.joinGame(name, password, playerName);
    }
}
