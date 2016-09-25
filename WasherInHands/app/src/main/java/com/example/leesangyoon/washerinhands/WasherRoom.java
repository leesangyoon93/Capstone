package com.example.leesangyoon.washerinhands;

import android.util.Log;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class WasherRoom {

    String roomName;
    String address;

    private WasherRoom() {}

    private static class Singleton {
        private static final WasherRoom washerRoom = new WasherRoom();
    }

    public static WasherRoom getInstance () {
        Log.e("development","create singleton instance : WasherRoom");
        return Singleton.washerRoom;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
