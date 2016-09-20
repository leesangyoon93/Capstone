package com.example.leesangyoon.washerinhands;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class WasherRoom {

    int id;
    User _host;
    ArrayList<User> members = new ArrayList<User>();
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

    public int getId() {
        return id;
    }

    public User get_host() {
        return _host;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void set_host(User _host) {
        this._host = _host;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addMembers(User user){
        members.add(user);
    }
}
