package com.example.kvarnsen.footballtracker.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

import com.example.kvarnsen.footballtracker.containers.Game;

/**
 * Created by kvarnsen on 21/11/14.
 */

/*
    Handles connection to Open Football API and parsing of JSON objects.

    Code adapted with alterations from https://www.codeofaninja.com/2013/11/android-json-parsing-tutorial.html
 */

public class JSONParser {

    public String myStr;
    public ArrayList gameList;

    /*
        URLs that hold JSON objects for all rounds of each respective league
     */
    public String deURL = "http://footballdb.herokuapp.com/api/v1/event/de.2014_15/rounds?callback=?";
    public String ligaURL = "http://footballdb.herokuapp.com/api/v1/event/es.2014_15/rounds?callback=?";
    public String engURL = "http://footballdb.herokuapp.com/api/v1/event/en.2014_15/rounds?callback=?";
    public String clURL = "http://footballdb.herokuapp.com/api/v1/event/cl.2014_15/rounds?callback=?";

    /*
        Strings that form the basic template for each round
     */
    public String deBuilder = "http://footballdb.herokuapp.com/api/v1/event/de.2014_15/round/?callback=?";
    public String ligaBuilder = "http://footballdb.herokuapp.com/api/v1/event/es.2014_15/round/?callback=?";
    public String engBuilder = "http://footballdb.herokuapp.com/api/v1/event/en.2014_15/round/?callback=?";
    public String clBuilder = "http://footballdb.herokuapp.com/api/v1/event/cl.2014_15/round/?callback=?";

    /*
        Handles URL opening and connection, and creation of the JSON object.
     */
    public ArrayList getJSONFromUrl(int selector, String team) {

        URL myURL = null;
        HttpURLConnection urlConnection = null;
        String json;
        JSONObject myJSON;

        gameList = new ArrayList();

        try {

            switch (selector) {

                case 0:
                    myURL = new URL(deURL);
                    break;
                case 1:
                    myURL = new URL(ligaURL);
                    break;
                case 2:
                    myURL = new URL(engURL);
                    break;
                case 3:
                    myURL = new URL(clURL);
                    break;

            }

        } catch(MalformedURLException mue) {
            mue.printStackTrace();
        }

        try {
            urlConnection = (HttpURLConnection) myURL.openConnection();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        if(urlConnection == null) {
            Log.w("URL", "URL Connection was null");
            return gameList;
        } else {
            try {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder out = new StringBuilder();

                while ((json = reader.readLine()) != null) {
                    out.append(json);
                }

                /*
                    Removes junk characters that are present in the Open Football JSON strings
                 */
                myStr = out.toString();
                myStr = myStr.substring(1);
                myStr = myStr.substring(1);
                reader.close();

                try {
                    myJSON = new JSONObject(myStr);
                    JSONObject curJSON;
                    JSONArray myJArr = myJSON.getJSONArray("rounds");
                    Date date;
                    ArrayList posList = new ArrayList();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String fDate = sdf.format(c.getTime());
                    Date curDate = null;

                    try {
                        curDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(fDate);
                    } catch(ParseException pe) {
                        pe.printStackTrace();
                    }

                    // step through the JSONArray of rounds and compare against current date.
                    // if a round's date is after the current date, add that position to posList
                    for(int i=0; i < myJArr.length(); i++) {

                        curJSON = myJArr.getJSONObject(i);

                        try {
                            date = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(curJSON.getString("start_at"));

                            if(date.after(curDate)) {
                                posList.add(curJSON.getString("pos"));
                            }

                        } catch (ParseException pe) {
                            pe.printStackTrace();
                        }

                    }

                    // step through posList and use the position attribute (curString) to fetch the required games
                    for(int i=0; i < posList.size(); i++) {

                        String curString = (String)posList.get(i);
                        StringBuilder roundURL = null;

                        switch (selector) {

                            case 0:
                                roundURL = new StringBuilder(deBuilder);
                                break;
                            case 1:
                                roundURL = new StringBuilder(ligaBuilder);
                                break;
                            case 2:
                                roundURL = new StringBuilder(engBuilder);
                                break;
                            case 3:
                                roundURL = new StringBuilder(clBuilder);
                                break;

                        }

                        if(roundURL != null)
                            roundURL.insert(62, curString);

                        String strURL = roundURL.toString();

                        addGame(strURL, team);

                    }

                } catch(JSONException je) {
                    je.printStackTrace();
                }



            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        }

        return gameList;

    }

    /*
        Creates instances of Game objects and adds them to an ArrayList
     */
    public void addGame(String strURL, String team) {

        JSONArray myJArray;
        JSONObject myJSON;
        URL myURL = null;
        HttpURLConnection urlConnection = null;
        String jStr, str;

        try {
            myURL = new URL(strURL);
        } catch(MalformedURLException mue) {
            mue.printStackTrace();
        }

        try {
            urlConnection = (HttpURLConnection) myURL.openConnection();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        if(urlConnection == null) {
            Log.w("URL", "URL Connection was null");
        } else {
            try {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder out = new StringBuilder();

                while ((jStr = reader.readLine()) != null) {
                    out.append(jStr);
                }

                // removes junk characters from JSON string
                str = out.toString();
                str = str.substring(1);
                str = str.substring(1);
                reader.close();

                try {
                    myJSON = new JSONObject(str);
                    JSONObject curJSON;
                    myJArray = myJSON.getJSONArray("games");

                    // step through each game and add games with user's team to gameList
                    for (int i = 0; i < myJArray.length(); i++) {

                        curJSON = myJArray.getJSONObject(i);

                        if (curJSON.getString("team1_key").equals(team) || curJSON.getString("team2_key").equals(team)) {

                            gameList.add(new Game(curJSON.getString("play_at"), (curJSON.getString("team1_title") + " vs. " + curJSON.getString("team2_title"))));

                        }

                    }


                } catch (JSONException je) {
                    je.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        }


    }

    /*
        Sorts the Champions League and national league ArrayLists into one, according to date
     */
    public ArrayList createFinalArray(ArrayList arr1, ArrayList arr2) {

        ArrayList combined = arr1;
        combined.addAll(arr2);

        Collections.sort(combined, new Comparator<Game>() {
            @Override
            public int compare(Game firstGame, Game secondGame) {
                return firstGame.date.compareTo(secondGame.date);
            }
        });

        return combined;
    }

}
