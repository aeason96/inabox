package com.example.andrew.inabox.fragments;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;
import com.example.andrew.inabox.models.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevincianfarini on 5/3/17.
 */

public class AnswerWaitFragment extends Fragment {

    public static final String TAG_ANSWER_WAIT_FRAGMENT = "tag_answer_wait_fragment";


    private Game game;
    private TextView remainingPlayers;
    private Thread pollThread;
    private Handler handler;
    int totalPlayers;
    int answeredPlayers;

    int questionID = -1;


    public AnswerWaitFragment() {

    }

    public static AnswerWaitFragment newInstance() {
        return new AnswerWaitFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_answer_wait, container, false);

        remainingPlayers = (TextView) view.findViewById(R.id.remaining_players);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String url = Constants.BASE_URL + "question/" + game.getGameRoom().gameID;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            questionID = response.getInt("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
                game.getRequestQueue().add(request);
                return null;
            }
        };

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/players";
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        totalPlayers = response.length() - 1; // -1 because question asker doesn't count
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
                game.getRequestQueue().add(request);
                return null;
            }
        };
        return view;
    }

    @Override
    public void onResume() {
        pollForPlayers();
        super.onResume();
    }

    @Override
    public void onPause() {
        pollThread.interrupt();
        super.onPause();
    }

    public void pollForPlayers() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    remainingPlayers.setText(String.format("%d players haven't answered yet", totalPlayers - answeredPlayers));
                    if (totalPlayers - answeredPlayers == 0) {
                        pollThread.interrupt();
                        //everyone answered TODO change fragment
                    }
                }
            }
        };

        pollThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (questionID != -1) {
                            String url = Constants.BASE_URL + "answers/" + questionID;
                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    answeredPlayers = response.length();
                                    Message message = handler.obtainMessage(0);
                                    message.sendToTarget();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //
                                }
                            });
                            game.getRequestQueue().add(request);
                        }
                        Thread.sleep(500);
                    }
                    Looper.loop();
                } catch (InterruptedException e) {
                    Looper.loop();
                }
            }
        });
        pollThread.start();
    }

}
