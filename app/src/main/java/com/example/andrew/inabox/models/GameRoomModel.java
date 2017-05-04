package com.example.andrew.inabox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 5/1/17.
 */

public class GameRoomModel implements Parcelable {

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

    protected GameRoomModel(Parcel in) {
        gameID = in.readInt();
        name = in.readString();
        password = in.readString();
    }

    public static final Creator<GameRoomModel> CREATOR = new Creator<GameRoomModel>() {
        @Override
        public GameRoomModel createFromParcel(Parcel in) {
            return new GameRoomModel(in);
        }

        @Override
        public GameRoomModel[] newArray(int size) {
            return new GameRoomModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameID);
        dest.writeString(name);
        dest.writeString(password);
    }
}
