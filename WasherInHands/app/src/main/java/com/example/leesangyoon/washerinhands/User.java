package com.example.leesangyoon.washerinhands;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class User extends SugarRecord{
    @Unique
    String userId;
    String password;
    String userName;
    boolean isAdmin;
    static ArrayList<WasherRoom> washerRooms = new ArrayList<WasherRoom>();

    public User() {}

    @Override
    public Long getId() {
        return super.getId();
    }

    public User(String userId, String password, String userName, boolean isAdmin) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.isAdmin = isAdmin;
    }

    public static void addWasherRoom(WasherRoom washerRoom) {
        washerRooms.add(washerRoom);
    }

    public void deleteWasherRoom(WasherRoom washerRoom) {
        int index = this.washerRooms.indexOf(washerRoom.getClass());
        this.washerRooms.remove(index);
        super.save();
    }

    public void showWaserRoom() {
        for(int i = 0; i < this.washerRooms.size(); i++) {
            Log.e("asdf", this.washerRooms.get(i).getRoomName());
        }
    }

    public boolean authenticate(String id, String pw) {
        User user = getUserById(id);

        if(user != null) {
            if(user.password.equals(pw))
                return true;
            else {
                return false;
            }
        }
        else
            return false;
    }

    public boolean is_existed(String id) {
        User user = getUserById(id);
        if(user == null)
            return false;
        else
            return true;
    }

    User getUserById(String id) {
        List<User> user = User.find(User.class, "USER_ID = ?", id);
        if(user.isEmpty())
            return null;
        return user.get(0);
    }

    ArrayList<WasherRoom> getWasherRooms() {
        return this.washerRooms;
    }

    public String getUserName() {
        return this.userName;
    }
}
