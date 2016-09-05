package com.example.leesangyoon.washerinhands;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * Created by daddyslab on 2016. 9. 5..
 */

@Table
public class WasherRoom extends SugarRecord {
    @Unique
    User _host;
    String roomName;
    String address;

    public WasherRoom() {

    }

    public WasherRoom(User host, String roomName, String address) {
        this._host = host;
        this.roomName = roomName;
        this.address = address;
    }

}
