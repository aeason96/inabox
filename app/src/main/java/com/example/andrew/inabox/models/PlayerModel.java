package com.example.andrew.inabox.models;

/**
 * Created by kevin on 5/1/17.
 */

public class PlayerModel {

    public GameRoomModel gameRoom;
    public String name;
    public int id;

    public PlayerModel(int id, String name, GameRoomModel gameRoom) {
        this.id = id;
        this.name = name;
        this.gameRoom = gameRoom;
    }

}
