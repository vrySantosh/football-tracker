package com.example.kvarnsen.footballtracker.containers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kvarnsen on 8/12/14.
 */

/*
    Holds information about an individual game, with a string that represents date, and a Date object for comparison usage
 */
public class Game {

    public String dateStr;
    public Date date;
    public String details;

    public Game(String new_date, String new_details) {
        dateStr = new_date;
        details = new_details;

        try {
            date = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(dateStr);
        } catch(ParseException e) {
            e.printStackTrace();
        }

    }


}
