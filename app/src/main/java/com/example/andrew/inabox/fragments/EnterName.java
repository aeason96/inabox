package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;

public class EnterName extends Fragment implements View.OnClickListener {
    public static final String TAG_ENTER_NAME_FRAGMENT = "enter_name";
    Game game;
    TextView welcomeText;
    Button continueButton;
    EditText username;

    public EnterName() {

    }

    public static EnterName newInstance() {
        EnterName fragment = new EnterName();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_name, container, false);
        game = (Game) getActivity();

        welcomeText = (TextView) view.findViewById(R.id.game_name);
        welcomeText.setText("Joining Game: " + game.getGameName());
        continueButton = (Button) view.findViewById(R.id.continue_button);
        username = (EditText) view.findViewById(R.id.username);

        continueButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (username.getText().toString().equals("")) {
            Toast.makeText(game.getApplicationContext(), "Invalid Username!", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}