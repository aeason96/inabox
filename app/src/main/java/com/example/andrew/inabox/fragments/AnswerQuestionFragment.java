package com.example.andrew.inabox.fragments;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;
import com.example.andrew.inabox.models.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class AnswerQuestionFragment extends Fragment {

    public static final String TAG_ANSWER_QUESTION_FRAGMENT = "answer_question_fragment";

    private Game game;
    private EditText answer;
    private TextView question;
    private int questionID;
    private Handler handler;
    private String questionText;
    private Thread pollThread;

    public AnswerQuestionFragment(){

    }

    public static AnswerQuestionFragment newInstance() {
        AnswerQuestionFragment fragment = new AnswerQuestionFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        game = (Game) getActivity();
        View view = inflater.inflate(R.layout.fragment_answer_question, container, false);


        answer = (EditText) view.findViewById(R.id.answer_edit_text);

        answer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitAnswer();
                    return true;
                }
                return false;
            }
        });

        question = (TextView) view.findViewById(R.id.question_text);

        pollForQuestion();

        return view;
    }


    public void pollForQuestion() {

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) { //set the player stuff
                    question.setText(questionText);
                } else if (msg.what == 1) { //kill the thread
                    pollThread.interrupt(); //we got the question, stop polling
                }
            }
        };

        pollThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String url = Constants.BASE_URL + "gameroom/" + game.getGameRoom().gameID + "/current_question";
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("accepting_answers")) {
                                        questionID = response.getInt("id");
                                        questionText = response.getString("value");
                                        Message message = handler.obtainMessage(0);
                                        message.sendToTarget();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // something should probably go here
                            }
                        });
                        game.getRequestQueue().add(request);
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    Looper.loop();
                }
            }
        });
        pollThread.start();
    }


    public void submitAnswer() {
        String answerText = answer.getText().toString();
        String url = Constants.BASE_URL + "answer/create";
        if (!answerText.equals("")) {
            JSONObject j = null;
            try {
                j = new JSONObject(String.format("{\"value\": \"%s\", \"creator\": \"%d\", \"question\": \"%d\"}", answerText, game.getPlayer().id, questionID));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    game.changeFragment(AnswerWaitFragment.TAG_ANSWER_WAIT_FRAGMENT);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //TODO something should probably go here
                }
            });
            game.getRequestQueue().add(request);
        }
        Toast.makeText(game.getApplicationContext(), "Your answer cannot be empty!", Toast.LENGTH_SHORT).show();
    }
}
