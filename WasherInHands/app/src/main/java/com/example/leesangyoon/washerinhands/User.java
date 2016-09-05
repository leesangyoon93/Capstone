package com.example.leesangyoon.washerinhands;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

@Table
public class User extends SugarRecord {
    @Unique
    String userId;
    String password;
    String username;
    boolean isAdmin;
    List<WasherRoom> washerRooms;

    public User() {

    }

    public User(String userId, String password, String username, boolean isAdmin) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public void addWasherRoom(WasherRoom washerRoom) {
        washerRooms.add(washerRoom);
    }
}
