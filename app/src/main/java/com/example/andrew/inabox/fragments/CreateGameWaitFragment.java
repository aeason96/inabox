package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;
import com.example.andrew.inabox.models.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

public class CreateGameWaitFragment extends Fragment implements View.OnClickListener{
    public static final String TAG_CREATE_GAME_WAIT_FRAGMENT = "create_game_wait_fragment";
    Game game;
    Button btnDoneAcceptingPlayers;
    TextView textViewPlayersJoined;
    Thread pollThread;
    boolean done;
    int numPlayers;

    public CreateGameWaitFragment(){

    }

    public static CreateGameWaitFragment newInstance() {
        CreateGameWaitFragment fragment = new CreateGameWaitFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_game_wait, container, false);

        btnDoneAcceptingPlayers = (Button) view.findViewById(R.id.btnDoneAcceptingPlayers);
        textViewPlayersJoined = (TextView) view.findViewById(R.id.textViewPlayersJoined);

        btnDoneAcceptingPlayers.setOnClickListener(this);
        pollThread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    if (!done) {
                        String url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/players";

                        // Request a string response from the provided URL.
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        numPlayers = response.length();
                                        if (numPlayers == 1){
                                            textViewPlayersJoined.setText("1 player is in the game");
                                        }
                                        else{
                                            textViewPlayersJoined.setText("" + numPlayers + " players are in the game");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //
                            }
                        });
                    }
                    else {
                        return;
                    }
                    try {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException ex){
                        continue;
                    }
                }
            }
        });
        pollThread.start();

        return view;
    }

    @Override
    public void onPause(){
        done = true;
        try {
            pollThread.join();
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnDoneAcceptingPlayers)){
            Toast.makeText(game.getApplicationContext(), "Done Accepting Players!", Toast.LENGTH_LONG).show();
        }
    }
}
