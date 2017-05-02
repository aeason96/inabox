package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;

public class GameRoomFragment extends Fragment {

    public static final String TAG_GAME_ROOM_FRAGMENT = "game_room";
    TextView gameName;
    Game game;
    public GameRoomFragment() {
        // Required empty public constructor
    }

    public static GameRoomFragment newInstance() {
        GameRoomFragment fragment = new GameRoomFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_game_room, container, false);

        gameName = (TextView) view.findViewById(R.id.game_name);
        gameName.setText(game.getGameRoom().name + "  :  " + game.getGameRoom().gameID);
        return view;
    }

}