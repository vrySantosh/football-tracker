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

import android.util.Log;

/**
 * Created by joshuapancho on 21/11/14.
 */
public class JSONParser {

    static JSONObject jObj = null;
    static String json;
    public String myStr;

    public String getJSONFromUrl(String url) {

        URL myURL = null;
        HttpURLConnection urlConnection = null;
        String json = null;
        JSONObject myJSON;

        try {
            myURL = new URL("http://footballdb.herokuapp.com/api/v1/event/de.2014_15/round/4?callback=?");
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
                    JSONArray myJArr = myJSON.getJSONArray("games");

                    JSONObject curJObj = myJArr.getJSONObject(0);
                    Log.w("jObj", curJObj.getString("team1_title")); // this works!



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

}
