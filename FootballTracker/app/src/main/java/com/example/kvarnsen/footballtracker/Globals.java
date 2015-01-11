package com.example.kvarnsen.footballtracker;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by joshuapancho on 24/12/14.
 */

/*
    Holds Fixture and Highlight ArrayLists, which persists for lifetime of the app
 */

public class Globals extends Application {

    private ArrayList fixture, highlights;

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
