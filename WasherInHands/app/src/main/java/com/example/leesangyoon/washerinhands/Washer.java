package com.example.leesangyoon.washerinhands;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * Created by daddyslab on 2016. 9. 5..
 */
@Table
public class Washer extends SugarRecord {
    @Unique
    WasherRoom washerRoom;
    String module;
    int runTime;
    boolean isTrouble;
    float x;
    float y;

    public void Wahser() {}

    public void Washer(WasherRoom washerRoom, float x, float y) {
        this.washerRoom = washerRoom;
        this.runTime = 0;
        this.isTrouble = false;
        this.x = x;
        this.y = y;
    }

}
