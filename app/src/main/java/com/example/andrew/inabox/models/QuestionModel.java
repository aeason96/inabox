package com.example.andrew.inabox.models;

/**
 * Created by kevin on 5/1/17.
 */

public class QuestionModel {

    public String value;
    public PlayerModel player;
    public GameRoomModel gameRoom;

    public QuestionModel(String value, PlayerModel player, GameRoomModel gameRoom) {
        this.value = value;
        this.player = player;
        this.gameRoom = gameRoom;
    }
}
