package com.example.andrew.inabox.fragments;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andrew.inabox.Game;
import com.example.andrew.inabox.R;
import com.example.andrew.inabox.models.Constants;
import com.example.andrew.inabox.service.BackgroundService;

import org.json.JSONException;
import org.json.JSONObject;


public class AnswerQuestionFragment extends Fragment implements View.OnClickListener, SensorEventListener {

    public static final String TAG_ANSWER_QUESTION_FRAGMENT = "answer_question_fragment";

    private Game game;
    private EditText answer;
    private TextView question;
    private int questionID;
    private Handler handler;
    private String questionText;
    private Thread pollThread;
    private Button submitButton;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;

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
        game.master = false;
        View view = inflater.inflate(R.layout.fragment_answer_question, container, false);
        mSensorManager = (SensorManager) game.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        answer = (EditText) view.findViewById(R.id.answer_edit_text);

        question = (TextView) view.findViewById(R.id.question_text);
        if (!game.question.equals("")) {
            question.setText(game.question);
        }

        submitButton = (Button) view.findViewById(R.id.submit_answer);

        submitButton.setOnClickListener(this);
        question.setText("");
        return view;
    }

    @Override
    public void onPause() {
        pollThread.interrupt();
        super.onPause();
    }

    @Override
    public void onResume() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) { //set the player stuff
                    question.setText(questionText);

                    pollThread.interrupt();
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
                            String url = Constants.BASE_URL + "question/" + game.getGameRoom().gameID;
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getBoolean("active")) {
                                            if (response.getInt("id") != game.questionID) {
                                                questionID = response.getInt("id");
                                                game.questionID = questionID;
                                                questionText = response.getString("value");
                                                game.question = questionText;
                                                Message message = handler.obtainMessage(0);
                                                message.sendToTarget();
                                            }
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
                        }
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    Looper.loop();
                }
            }
        });
        pollThread.start();
        super.onResume();
    }


    public void onClick(View v) {
        if (v == submitButton) {
            String answerText = answer.getText().toString();
            String url = Constants.BASE_URL + "answer/create/";
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
            } else {
                Toast.makeText(game.getApplicationContext(), "Your answer cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isBackgroundServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d("background_service", "Checking");
            if (BackgroundService.class.getName().equals(service.service.getClassName())) {
                Log.d("background_service", "BackgroundService is already running!");
                return true;
            }
        }
        return false;
    }


    public void startBackgroundServiceNeeded() {

        // check if the background service is running, if not then start it
        if (!isBackgroundServiceRunning()) {
            Intent intent = new Intent(getActivity(), BackgroundService.class);
            Bundle data = intent.getExtras();
            //data.putString("player_id", );
            getActivity().startService(intent);
            Log.d("background_service", "BackgroundService  TOLD TO START!");

        }

    }

    private float x1=0, x2=0, x3=0;
    @Override
    public void onSensorChanged(SensorEvent e) {


        //Get x,y and z values
        float ERROR = (float) 35.0;
        float x,y,z;
        x = e.values[0];
        y = e.values[1];
        z = e.values[2];


        float diffX = Math.abs(x1 - x);
        float diffY = Math.abs(x2 - y);
        float diffZ = Math.abs(x3 - z);

        //Handling ACCELEROMETER Noise
        if (diffX < ERROR) {

            diffX = (float) 0.0;
        }
        if (diffY < ERROR) {
            diffY = (float) 0.0;
        }
        if (diffZ < ERROR) {

            diffZ = (float) 0.0;
        }


        x1 = x;
        x2 = y;
        x3 = z;


        //Horizontal Shake Detected!
        if (diffX > diffY) {

            answer.setText("");
            Log.d("Sensor: ", " " + diffX);
            //Toast.makeText(game, "Shake Detected!", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
