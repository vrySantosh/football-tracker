package com.example.kvarnsen.footballtracker;

import android.app.Application;

/**
 * Created by joshuapancho on 24/12/14.
 */

public class Globals extends Application {

    private String team;
    private String id;

    public String getTeam() {
        return team;
    }

    public void setTeam(String myTeam) {
        team = myTeam;
    }

    public String getId() {
        return id;
    }

    public void setId(String myId) {
        id = myId;
    }

}
