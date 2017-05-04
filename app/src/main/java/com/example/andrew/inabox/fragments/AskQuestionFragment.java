package com.example.andrew.inabox.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;
import com.example.andrew.inabox.models.Constants;
import com.example.andrew.inabox.models.GameRoomModel;
import com.example.andrew.inabox.models.QuestionModel;

import org.json.JSONException;
import org.json.JSONObject;


public class AskQuestionFragment extends Fragment implements View.OnClickListener {
    public static final String TAG_ASK_QUESTION_FRAGMENT = "ask_question_fragment";
    Game game;
    EditText editTextQuestion;
    Button btnSubmit;

    public AskQuestionFragment(){

    }

    public static AskQuestionFragment newInstance() {
        AskQuestionFragment fragment = new AskQuestionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_ask_question, container, false);

        btnSubmit= (Button) view.findViewById(R.id.btnSubmit);
        editTextQuestion = (EditText) view.findViewById(R.id.editTextQuestion);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnSubmit)){
            if (editTextQuestion.getText().toString().length() == 0){
                Toast.makeText(game.getApplicationContext(), "Your question can't be empty!!", Toast.LENGTH_SHORT);
            }
            else {
                int person_id = game.getPlayer().id;
                int game_room = game.getGameRoom().gameID;
                String value = editTextQuestion.getText().toString();
                QuestionModel q = new QuestionModel(value, game.getPlayer(), game.getGameRoom());
                String url = Constants.BASE_URL + "question/create/";
                JSONObject j = null;
                try {
                    j = new JSONObject(String.format("{\"value\": \"%s\", \"creator\": \"%d\", \"game_room\": \"%d\"}", value, person_id, game_room));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Request a string response from the provided URL.
                if (j != null) {
                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                game.changeFragment(AnswerWaitFragment.TAG_ANSWER_WAIT_FRAGMENT);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    game.getRequestQueue().add(req);

                }
            }
        }
    }
}
