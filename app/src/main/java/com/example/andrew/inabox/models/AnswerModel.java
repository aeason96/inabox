package com.example.andrew.inabox.models;

/**
 * Created by kevin on 5/1/17.
 */

public class AnswerModel {

    public String value;
    public PlayerModel creator;
    public QuestionModel qustion;

    public AnswerModel(String value, PlayerModel creator, QuestionModel qustion) {
        this.value = value;
        this.creator = creator;
        this.qustion = qustion;
    }
}
