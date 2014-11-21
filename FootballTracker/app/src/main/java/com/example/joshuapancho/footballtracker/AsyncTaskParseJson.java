package com.example.joshuapancho.footballtracker;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by joshuapancho on 21/11/14.
 */
public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

    final String TAG = "AsyncTaskParseJson.java";

    // set your json string url here
        // Page apparently doesn't exist...?
    String yourJsonStringUrl = "http://footballdb.herokuapp.com/api/v1/event/de.2014_15/round/4?callback=?";

    // contacts JSONArray
    JSONArray dataJsonArr = null;

    @Override
    protected void onPreExecute() {}

    @Override
    protected String doInBackground(String... arg0) {

        // instantiate our json parser
        JSONParser jParser = new JSONParser();

        // get json string from url
        String json = null;

        json = jParser.getJSONFromUrl(yourJsonStringUrl);

        if(json == null) {
            return "Null JSON Object";
        } else {
            try {
                return json;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {

        if(strFromDoInBg == null) {
            Log.w("Result", "String was null");
        }

        Log.w("ope", strFromDoInBg);
    }
}
