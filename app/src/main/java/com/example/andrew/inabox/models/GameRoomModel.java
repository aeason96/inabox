package com.example.andrew.inabox.models;

/**
 * Created by kevin on 5/1/17.
 */

public class GameRoomModel {

    public int gameID;
    public String name, password;
    public Double longitude, latitude;

    public GameRoomModel(int gameID, String name, String password, Double longitude, Double latitude) {
        this.gameID = gameID;
        this.name = name;
        this.password = password;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
