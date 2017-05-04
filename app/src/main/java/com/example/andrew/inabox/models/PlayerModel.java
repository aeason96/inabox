package com.example.andrew.inabox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 5/1/17.
 */

public class PlayerModel implements Parcelable {

    public GameRoomModel gameRoom;
    public String name;
    public int id;

    public PlayerModel(int id, String name, GameRoomModel gameRoom) {
        this.id = id;
        this.name = name;
        this.gameRoom = gameRoom;
    }

    protected PlayerModel(Parcel in) {
        gameRoom = in.readParcelable(GameRoomModel.class.getClassLoader());
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<PlayerModel> CREATOR = new Creator<PlayerModel>() {
        @Override
        public PlayerModel createFromParcel(Parcel in) {
            return new PlayerModel(in);
        }

        @Override
        public PlayerModel[] newArray(int size) {
            return new PlayerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(gameRoom, flags);
        dest.writeString(name);
        dest.writeInt(id);
    }
}
