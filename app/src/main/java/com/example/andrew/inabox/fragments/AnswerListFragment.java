package com.example.andrew.inabox.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.v7.app.AlertDialog;
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
import org.json.JSONException;
import org.json.JSONObject;

public class AnswerListFragment extends Fragment implements View.OnClickListener{
    public static final String TAG_ANSWER_LIST_FRAGMENT = "answer_list_fragment";

    private Game game;
    private TextView textViewAnswers;
    private TextView textViewQuestion;
    private Button btnContinue;

    public AnswerListFragment(){

    }

    public static AnswerListFragment newInstance() {
        AnswerListFragment fragment = new AnswerListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_answer_list, container, false);
        game.activeFragmentType = this.TAG_ANSWER_LIST_FRAGMENT;
        textViewAnswers = (TextView) view.findViewById(R.id.textViewAnswers);
        textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        textViewQuestion.setText(game.question);

        String url = Constants.BASE_URL + "answers/" + game.questionID + "/";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                StringBuilder builder = new StringBuilder();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject j = response.getJSONObject(i);
                        builder.append(String.format("%s\n\n", j.getString("value")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textViewAnswers.setText(builder.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });
        game.getRequestQueue().add(request);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnContinue)) {
            if (game.master) {
                String url = Constants.BASE_URL + "question/" + game.questionID + "/unlock/";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
                game.getRequestQueue().add(request);

                url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/questionmaster/";
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });
                game.getRequestQueue().add(req);

                goOn();
            }
            else{
                //check that game master says its ok
                String url = Constants.BASE_URL + "question/" + game.questionID + "/question/";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("unlocked")) {
                                goOn();
                            }
                            else{
                                Toast.makeText(game.getApplicationContext(), "Question asker must continue first", Toast.LENGTH_SHORT).show();
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
                game.getRequestQueue().add(request);

            }


        }

    }

    public void goOn(){
        game.changeFragment(AnswerRevealFragment.TAG_ANSWER_REVEAL_FRAGMENT);
    }
}
