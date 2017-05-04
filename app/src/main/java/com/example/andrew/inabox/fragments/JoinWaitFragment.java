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
import org.json.JSONException;
import org.json.JSONObject;

public class JoinWaitFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_JOIN_WAIT_FRAGMENT = "join_wait_fragment";

    Game game;
    TextView textViewPlayersInGame;
    Thread pollThread;
    int numPlayers;
    Handler mHandler;
    Button btnCancelJoinGame;

    public JoinWaitFragment(){

    }

    public static JoinWaitFragment newInstance() {
        JoinWaitFragment fragment = new JoinWaitFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_join_wait, container, false);
        game.activeFragmentType = this.TAG_JOIN_WAIT_FRAGMENT;
        btnCancelJoinGame = (Button) view.findViewById(R.id.btnCancelJoinGame);
        textViewPlayersInGame = (TextView) view.findViewById(R.id.textViewPlayersInGame);

        btnCancelJoinGame.setOnClickListener(this);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if(message.what == 0){
                    if (numPlayers == 1){
                        textViewPlayersInGame.setText("1 player is in the game");
                    }
                    else{
                        textViewPlayersInGame.setText("" + numPlayers + " players are in the game");
                    }
                }
                else if (message.what == 1){
                    pollThread.interrupt();
                    game.getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                        @Override
                        public boolean apply(Request<?> request) {
                            return true;
                        }
                    });
                    game.changeFragment(AnswerQuestionFragment.TAG_ANSWER_QUESTION_FRAGMENT);
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
                                            Message message = mHandler.obtainMessage(0);
                                            message.sendToTarget();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //
                                }
                            });
                            game.getRequestQueue().add(jsonArrayRequest);
                            String url2 = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID;

                            // Request a string response from the provided URL.
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (!response.getBoolean("accepting_players")) {
                                                    Message message = mHandler.obtainMessage(1);
                                                    message.sendToTarget();
                                                }
                                            }
                                            catch(JSONException ex){
                                                ex.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //
                                }
                            });
                            game.getRequestQueue().add(jsonObjectRequest);
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
    public void onClick(View v){
        if (v.equals(btnCancelJoinGame)){
            pollThread.interrupt();
            game.getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });

            String url = Constants.BASE_URL + "player/" + game.getPlayer().id + "/delete";
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
            game.setPlayer(null);
            game.setGameRoom(null);
            game.changeFragment(JoinGameFragment.TAG_JOIN_FRAGMENT);
        }

    }
}
