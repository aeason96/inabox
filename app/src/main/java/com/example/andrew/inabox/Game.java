package com.example.andrew.inabox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrew.inabox.fragments.CreateGame;
import com.example.andrew.inabox.fragments.GameRoom;
import com.example.andrew.inabox.fragments.HomeScreenFragment;
import com.example.andrew.inabox.fragments.JoinGame;
import com.example.andrew.inabox.fragments.TaskFragment;
import com.example.andrew.inabox.interfaces.HomeScreenInteraction;
import com.example.andrew.inabox.interfaces.RetainedFragmentInteraction;
import com.example.andrew.inabox.models.Constants;
import com.example.andrew.inabox.models.GameRoomModel;
import com.example.andrew.inabox.models.PlayerModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Game extends AppCompatActivity implements HomeScreenInteraction {

    private Fragment homeScreenFragment, taskFragment, createGame, joinGame;
    private FragmentManager fragmentManager;
    private String gameName, gamePassword, playerName;
    private int gameID;
    private double latitude, longitude;

    private GameRoomModel gameRoom;
    private PlayerModel player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getLocation();

        fragmentManager = getSupportFragmentManager();

        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            homeScreenFragment = new HomeScreenFragment();
            // Set dashboard fragment to be the default fragment shown
            ((RetainedFragmentInteraction) taskFragment).setActiveFragmentTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            fragmentManager.beginTransaction().replace(R.id.game, homeScreenFragment).commit();
        } else {
            // Get references to the fragments if they existed, null otherwise
            createGame = fragmentManager.findFragmentByTag(CreateGame.TAG_CREATE_FRAGMENT);
            joinGame = fragmentManager.findFragmentByTag(JoinGame.TAG_JOIN_FRAGMENT);
        }

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
        if (fragment_name.equals(CreateGame.TAG_CREATE_FRAGMENT)) {
            fragmentClass = CreateGame.class;
        } else if (fragment_name.equals(GameRoom.TAG_GAME_ROOM_FRAGMENT)) {
            fragmentClass = GameRoom.class;
        } else if (fragment_name.equals(JoinGame.TAG_JOIN_FRAGMENT)) {
            fragmentClass = JoinGame.class;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();

                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.game, fragment,
                        ((RetainedFragmentInteraction) taskFragment).getActiveFragmentTag());
                ft.addToBackStack(null);
                ft.commit();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createGame(String gameName, String gamePassword) {
        setGameName(gameName);
        setGamePassword(gamePassword);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.BASE_URL + "gameroom/create/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    //mTextView.setText("Response is: "+ response.substring(0,500));
                    Log.d("Server", "Server was hit and sent a response");
                    Log.d("Server", "Response is: "+ response);
                    try {
                        JSONObject gameInfo = new JSONObject(response);
                        int id = gameInfo.getInt("id");
                        setGameID(id);
                        Log.d("Game", "ID = " + id);
                        changeFragment(GameRoom.TAG_GAME_ROOM_FRAGMENT);
                    } catch (JSONException e) {
                        Log.d("Server Response Error", "JSON Conversion Error");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error contacting server!", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", getGameName());
                params.put("password", getGamePassword());
                return params;
            }



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void joinGame(final String gameName, final String gamePassword, final String playerName) {

        setGameName(gameName);
        setGamePassword(gamePassword);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.BASE_URL + "player/create/";
        JSONObject j = null;
        try {
            j = new JSONObject(String.format("{\"name\": \"%s\", \"game_room\": {\"name\": \"%s\", \"password\": \"%s\"}", playerName, gameName, gamePassword));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        if (j != null) {
            new JsonObjectRequest(url, j, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String name = response.getString("name");
                        int playerID = response.getInt("id");
                        JSONObject gameInfo = response.getJSONObject("game_room");
                        GameRoomModel game = new GameRoomModel(gameInfo.getInt("id"), gameInfo.getString("name"), gameInfo.getString("password"), null, null);
//                        player = new PlayerModel()
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    public String getGameName() {
        return gameName;
    }

    private void setGameName(String name) {
        gameName = name;
    }

    public String getGamePassword() { return gamePassword; }

    private void setGamePassword(String name) {
        gamePassword = name;
    }

    private void setGameID(int id) { gameID = id; }
    public int getGameID() { return gameID; }
    public String getPlayerName() {
        return playerName;
    }
    private void setPlayerName(String name) {
        playerName = name;
    }

    private PlayerModel getPlayer() {
        return this.player;
    }

    private GameRoomModel getGameRoom() {
        return gameRoom;
    }

}
