package com.example.leesangyoon.washerinhands;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class WasherRoom extends SugarRecord {
    @Unique
    User _host;
    ArrayList<User> members = new ArrayList<User>();
    String roomName;
    String address;

    public WasherRoom() {}

    @Override
    public Long getId() {
        return super.getId();
    }

    public WasherRoom(User host, String roomName, String address) {
        this._host = host;
        this.roomName = roomName;
        this.address = address;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public boolean is_existed(String name) {
        WasherRoom washerRoom = getWasherRoomByName(name);
        if(washerRoom == null)
            return false;
        else
            return true;
    }

    WasherRoom getWasherRoomByName(String name) {
        List<WasherRoom> washerRooms = WasherRoom.find(WasherRoom.class, "ROOM_NAME = ?", name);
        if(washerRooms.isEmpty())
            return null;
        return washerRooms.get(0);
    }

    public String getRoomName() {
        return this.roomName;
    }

    public String getAddress() {
        return this.address;
    }

    ArrayList<User> getMembers() {
        return this.members;
    }

    User get_host() {
        return this._host;
    }
}
