package com.example.kvarnsen.footballtracker;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by joshuapancho on 24/12/14.
 */

/*
    Holds global vars team and id to share between activities.
 */

public class Globals extends Application {

    private String team;
    private String id;
    private ArrayList fixture, highlights;

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

    public ArrayList getFixture() {
        return fixture;
    }

    public void setFixture(ArrayList newFixture) {
        fixture = newFixture;
    }

    public ArrayList getHighlights() {
        return highlights;
    }

    public void setHighlights(ArrayList newHighlights) {
        highlights = newHighlights;
    }

}
