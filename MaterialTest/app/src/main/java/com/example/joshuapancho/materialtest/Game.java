package com.example.joshuapancho.materialtest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by joshuapancho on 8/12/14.
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
