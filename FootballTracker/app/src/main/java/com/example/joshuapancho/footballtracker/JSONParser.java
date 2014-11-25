package com.example.joshuapancho.footballtracker;

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
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

import android.util.Log;

/**
 * Created by joshuapancho on 21/11/14.
 */
public class JSONParser {

    static JSONObject jObj = null;
    static String json;
    public String myStr;
    public static int finalRound = 34;

    public String getJSONFromUrl(String url) {

        URL myURL = null;
        HttpURLConnection urlConnection = null;
        String json = null;
        JSONObject myJSON;

        try {
            myURL = new URL("http://footballdb.herokuapp.com/api/v1/event/de.2014_15/rounds?callback=?");
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
            return json;
        } else {
            try {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder out = new StringBuilder();

                while ((json = reader.readLine()) != null) {
                    out.append(json);
                }

                myStr = out.toString();
                myStr = myStr.substring(1);
                myStr = myStr.substring(1);
                reader.close();

                try {
                    myJSON = new JSONObject(myStr);
                    JSONObject curJSON = null;
                    JSONArray myJArr = myJSON.getJSONArray("rounds");
                    Date date = null;
                    ArrayList posList = new ArrayList();
                    URL curURL = null;

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String fDate = sdf.format(c.getTime());
                    Date curDate = null;

                    try {
                        curDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(fDate);
                    } catch(ParseException pe) {
                        pe.printStackTrace();
                    }

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

                    for(int i=0; i < posList.size(); i++) {

                        String curString = (String)posList.get(i);
                        StringBuilder roundURL = new StringBuilder("http://footballdb.herokuapp.com/api/v1/event/de.2014_15/round/?callback=?");
                        roundURL.insert(62, curString);

                        String strURL = roundURL.toString();

                        printGame(strURL);

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

        return myStr;

    }

    public void printGame(String strURL) {

        JSONArray myJArray = null;
        JSONObject myJSON;
        URL myURL = null;
        HttpURLConnection urlConnection = null;
        String jStr, str, curGame;

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

                str = out.toString();
                str = str.substring(1);
                str = str.substring(1);
                reader.close();

                try {
                    myJSON = new JSONObject(str);
                    JSONObject curJSON = null;
                    myJArray = myJSON.getJSONArray("games");

                    for(int i=0; i < myJArray.length(); i++) {

                        curJSON = myJArray.getJSONObject(i);

                        if(curJSON.getString("team1_key").equals("bayern") || curJSON.getString("team2_key").equals("bayern")) {

                            curGame = curJSON.getString("play_at") + ": " + curJSON.getString("team1_title") + " vs. " + curJSON.getString("team2_title");
                            Log.w("game", curGame);

                        }

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


    }

}
