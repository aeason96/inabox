package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AnswerRevealFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_ANSWER_REVEAL_FRAGMENT = "answer_reveal_fragment";
    private Game game;
    private TextView textViewAnswersNames;
    private Button btnNextQuestion;

    public AnswerRevealFragment() {

    }

    public static AnswerRevealFragment newInstance() {
        AnswerRevealFragment fragment = new AnswerRevealFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_answer_list, container, false);

        textViewAnswersNames = (TextView) view.findViewById(R.id.textViewAnswersNames);
        btnNextQuestion = (Button) view.findViewById(R.id.btnNextQuestion);
        btnNextQuestion.setOnClickListener(this);

        String url = Constants.BASE_URL + "question/" + game.getGameRoom().gameID + "/";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                textViewAnswersNames.setText(response.toString());
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
    public void onClick(View v){
        if (v.equals(btnNextQuestion)){
            goOn();
        }
    }

    private void goOn(){
        String url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/questionmaster/";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {

                try {
                    if (response.getInt("id") == game.getPlayer().id) {
                        game.question = "";
                        game.master = true;
                        game.changeFragment(AskQuestionFragment.TAG_ASK_QUESTION_FRAGMENT);
                    } else {
                        game.question = "";
                        game.master = false;
                        game.changeFragment(AnswerQuestionFragment.TAG_ANSWER_QUESTION_FRAGMENT);
                    }
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
        game.getRequestQueue().add(req);
    }
}