package com.example.andrew.inabox;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrew.inabox.fragments.AnswerListFragment;
import com.example.andrew.inabox.fragments.AnswerQuestionFragment;
import com.example.andrew.inabox.fragments.AnswerRevealFragment;
import com.example.andrew.inabox.fragments.AnswerWaitFragment;
import com.example.andrew.inabox.fragments.AskQuestionFragment;
import com.example.andrew.inabox.fragments.CreateGameFragment;
import com.example.andrew.inabox.fragments.CreateGameWaitFragment;
import com.example.andrew.inabox.fragments.GameRoomFragment;
import com.example.andrew.inabox.fragments.HomeScreenFragment;
import com.example.andrew.inabox.fragments.JoinGameFragment;
import com.example.andrew.inabox.fragments.JoinWaitFragment;
import com.example.andrew.inabox.fragments.TaskFragment;
import com.example.andrew.inabox.interfaces.HomeScreenInteraction;
import com.example.andrew.inabox.interfaces.RetainedFragmentInteraction;
import com.example.andrew.inabox.models.Constants;
import com.example.andrew.inabox.models.GameRoomModel;
import com.example.andrew.inabox.models.PlayerModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Game extends AppCompatActivity implements HomeScreenInteraction {

    private Fragment homeScreenFragment, taskFragment, createGame, joinGame, activeFragment;
    private FragmentManager fragmentManager;
    private int gameID;
    private double latitude, longitude;
    public String activeFragmentType = "";
    private RequestQueue queue;

    private GameRoomModel gameRoom;
    private PlayerModel player;
    public String question = "";
    public int questionID = -1;
    public boolean master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getLocation();

        fragmentManager = getSupportFragmentManager();

        queue = Volley.newRequestQueue(this);

        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            homeScreenFragment = new HomeScreenFragment();
            // Set dashboard fragment to be the default fragment shown
            ((RetainedFragmentInteraction) taskFragment).setActiveFragmentTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            activeFragment = homeScreenFragment;
            fragmentManager.beginTransaction().replace(R.id.game, homeScreenFragment).commit();
        } else {
            // Get references to the fragments if they existed, null otherwise
            createGame = fragmentManager.findFragmentByTag(CreateGameFragment.TAG_CREATE_FRAGMENT);
            joinGame = fragmentManager.findFragmentByTag(JoinGameFragment.TAG_JOIN_FRAGMENT);
        }

    }

    @Override
    public void onBackPressed() {
        switch (activeFragmentType) {
            case (HomeScreenFragment.TAG_HOME_FRAGMENT):
                finish();
                break;
            case (JoinGameFragment.TAG_JOIN_FRAGMENT):
                backInteraction();
                break;
            case (CreateGameFragment.TAG_CREATE_FRAGMENT):
                backInteraction();
                break;
            default:
                leaveGameNotify();
                break;
        }
    }

    private void backInteraction() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void leaveGameNotify() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Leaving Game")
                .setMessage("Are you sure you want to leave this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = Constants.BASE_URL + "player/" + getPlayer().id + "/delete";
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

                        getRequestQueue().add(stringRequest);
                        setPlayer(null);
                        setGameRoom(null);
                        changeFragment(HomeScreenFragment.TAG_HOME_FRAGMENT);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("gameRoom", gameRoom);
        savedInstanceState.putParcelable("player", player);
        savedInstanceState.putString("question", question);
        savedInstanceState.putInt("questionID", questionID);
        savedInstanceState.putString("activeFragment", activeFragmentType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle sis) {
        super.onRestoreInstanceState(sis);

        gameRoom = sis.getParcelable("gameRoom");
        player = sis.getParcelable("player");
        question = sis.getString("question");
        questionID = sis.getInt("questionID");
        activeFragmentType = sis.getString("activeFragment");
    }

    private void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }
    @Override
    public void changeFragment(String fragment_name) {

        Fragment fragment;
        Class fragmentClass = null;

        switch (fragment_name) {
            case AskQuestionFragment.TAG_ASK_QUESTION_FRAGMENT:
                fragmentClass = AskQuestionFragment.class;
                break;
            case CreateGameFragment.TAG_CREATE_FRAGMENT:
                fragmentClass = CreateGameFragment.class;
                break;
            case CreateGameWaitFragment.TAG_CREATE_GAME_WAIT_FRAGMENT:
                fragmentClass = CreateGameWaitFragment.class;
                break;
            case GameRoomFragment.TAG_GAME_ROOM_FRAGMENT:
                fragmentClass = GameRoomFragment.class;
                break;
            case HomeScreenFragment.TAG_HOME_FRAGMENT:
                fragmentClass = HomeScreenFragment.class;
                break;
            case JoinGameFragment.TAG_JOIN_FRAGMENT:
                fragmentClass = JoinGameFragment.class;
                break;
            case JoinWaitFragment.TAG_JOIN_WAIT_FRAGMENT:
                fragmentClass = JoinWaitFragment.class;
                break;
            case AnswerQuestionFragment.TAG_ANSWER_QUESTION_FRAGMENT:
                fragmentClass = AnswerQuestionFragment.class;
                break;
            case AnswerWaitFragment.TAG_ANSWER_WAIT_FRAGMENT:
                fragmentClass = AnswerWaitFragment.class;
                break;
            case AnswerListFragment.TAG_ANSWER_LIST_FRAGMENT:
                fragmentClass = AnswerListFragment.class;
                break;
            case AnswerRevealFragment.TAG_ANSWER_REVEAL_FRAGMENT:
                fragmentClass = AnswerRevealFragment.class;
                break;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();

                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.game, fragment,
                        ((RetainedFragmentInteraction) taskFragment).getActiveFragmentTag());
                ft.addToBackStack(null);
                activeFragment = fragment;
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createGame(final String gameName, final String gamePassword, final String playerName) {
        // Instantiate the RequestQueue.
        String url = Constants.BASE_URL + "gameroom/create/";
        JSONObject j = null;
        try {
            j = new JSONObject(String.format("{\"name\": \"%s\", \"password\": \"%s\"}", gameName, gamePassword));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        if (j != null) {
            Request request = new JsonObjectRequest(Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    joinGame(gameName, gamePassword, playerName, true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Your game room name was already taken!", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(request);
        }

    }

    public void joinGame(final String gameName, final String gamePassword, final String playerName, final boolean creator) {

        // Instantiate the RequestQueue.
        String url = Constants.BASE_URL + "player/create/";
        JSONObject j = null;
        try {
            j = new JSONObject(String.format("{\"name\": \"%s\", \"game_room\": {\"name\": \"%s\", \"password\": \"%s\"}}", playerName, gameName, gamePassword));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        if (j != null) {
            Request request = new JsonObjectRequest(Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String name = response.getString("name");
                        int playerID = response.getInt("id");
                        JSONObject gameInfo = response.getJSONObject("game_room");
                        gameRoom = new GameRoomModel(gameInfo.getInt("id"), gameInfo.getString("name"), gameInfo.getString("password"), null, null);
                        player = new PlayerModel(playerID, name, gameRoom);
                        if (creator) {
                            changeFragment(CreateGameWaitFragment.TAG_CREATE_GAME_WAIT_FRAGMENT);
                        } else {
                            changeFragment(JoinWaitFragment.TAG_JOIN_WAIT_FRAGMENT);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast.makeText(getApplicationContext(), new JSONObject(new String(error.networkResponse.data)).getString("detail"), Toast.LENGTH_LONG).show();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            queue.add(request);
        }
    }
    boolean idOfQ = false;
    public boolean whoAsksAQuestion() {
        String url = Constants.BASE_URL + "gameroom/" + gameRoom.gameID + "/questionmaster/";
        boolean result;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response: ", response.toString());
                        try {
                            JSONObject obj = response.getJSONObject("questionmaster");
                            if (obj.getInt("id") == player.id) {
                                idOfQ = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error: ", "In whoAsksAquestion");
                    }
                });
        return idOfQ;
    }


    public void setPlayer(PlayerModel player){
        this.player = player;
    }

    public void setGameRoom(GameRoomModel game){
        this.gameRoom = game;
    }

    public PlayerModel getPlayer() {
        return this.player;
    }

    public GameRoomModel getGameRoom() {
        return gameRoom;
    }

    public RequestQueue getRequestQueue() {
        return this.queue;
    }

}
