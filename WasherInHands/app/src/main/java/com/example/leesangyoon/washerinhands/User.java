package com.example.leesangyoon.washerinhands;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

public class User {

    private String mainRoomName= "";
    private String userId=null;
    private String password=null;
    private String userName=null;
    private boolean isAdmin;
    String token = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private ArrayList<WasherRoom> washerRooms = new ArrayList<WasherRoom>();

    private User() {}

    private static class Singleton {
        private static final User user = new User();
    }

    public static User getInstance () {
        Log.e("development","create singleton instance : User");
        return Singleton.user;
    }

    ArrayList<WasherRoom> getWasherRooms() {
        return this.washerRooms;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }

    public String getMainRoomName() {
        return mainRoomName;
    }

    public String getUserName() {
        return this.userName;
    }

    public Boolean getAdmin() {
        return this.isAdmin;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public void setMainRoomName(String mainRoomName){ this.mainRoomName = mainRoomName;}
}
