package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    int numPlayers;
    Handler mHandler;

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

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (numPlayers == 1){
                    textViewPlayersJoined.setText("1 player is in the game");
                }
                else{
                    textViewPlayersJoined.setText("" + numPlayers + " players are in the game");
                }
            }
        };

        pollThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (game.getGameRoom() != null) {
                            String url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/players";

                            // Request a string response from the provided URL.
                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            numPlayers = response.length();
                                            Message message = mHandler.obtainMessage();
                                            message.sendToTarget();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //
                                }
                            });
                            game.getRequestQueue().add(jsonArrayRequest);
                            Thread.sleep(500);
                        }
                    }
                    Looper.loop();
                }
                catch (InterruptedException ex)
                {
                    Looper.loop();
                }
            }

        });
        return view;
    }

    @Override
    public void onResume(){
        pollThread.start();
        super.onResume();
    }

    @Override
    public void onPause(){
        pollThread.interrupt();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnDoneAcceptingPlayers)){
            if (numPlayers >= 3) {
                game.getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });

                pollThread.interrupt();

                String url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/close";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
                game.getRequestQueue().add(stringRequest);

                game.changeFragment(AskQuestionFragment.TAG_ASK_QUESTION_FRAGMENT);
            }
            else {
                Toast.makeText(game.getApplicationContext(), "You must have 3 or more players in your game room to continue!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
