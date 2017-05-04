package com.example.andrew.inabox.fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;
import com.example.andrew.inabox.models.Constants;

import org.json.JSONArray;

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

        textViewAnswers = (TextView) view.findViewById(R.id.textViewAnswers);
        textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        textViewQuestion.setText(game.question);

        String url = Constants.BASE_URL + "answers/" + game.questionID + "/";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                textViewAnswers.setText(response.toString());
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

    }
}
