package com.example.andrew.inabox.fragments;

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

public class CreateGameFragment extends Fragment implements View.OnClickListener {

    public static final String TAG_CREATE_FRAGMENT = "create_fragment";
    Game game;
    EditText gameName;
    EditText gamePassword;
    EditText username;
    Button startButton;

    public CreateGameFragment() {
        // Required empty public constructor
    }

    public static CreateGameFragment newInstance() {
        CreateGameFragment fragment = new CreateGameFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_game, container, false);
        game.activeFragmentType = this.TAG_CREATE_FRAGMENT;
        gameName = (EditText) view.findViewById(R.id.game_name);
        gamePassword = (EditText) view.findViewById(R.id.game_password);
        username = (EditText) view.findViewById(R.id.username);
        startButton = (Button) view.findViewById(R.id.start_game);

        startButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.equals(startButton)) {
            if (!gameName.getText().toString().equals("") &&
                    !gamePassword.getText().toString().equals("") && !username.getText().toString().equals("")) {
                //Start Game
                game.createGame(gameName.getText().toString(), gamePassword.getText().toString(), username.getText().toString());
            } else {
                Toast noGameName = Toast.makeText(game.getApplicationContext(),
                        "Game Name and Password Cannot Be Left Blank.",
                        Toast.LENGTH_SHORT);
                noGameName.show();
            }
        }
    }
}
