package com.example.andrew.inabox.models;

/**
 * Created by kevin on 5/1/17.
 */

public class PlayerModel {

    public GameRoomModel gameRoom;
    public String name;

    public PlayerModel(String name, GameRoomModel gameRoom) {
        this.name = name;
        this.gameRoom = gameRoom;
    }

}
