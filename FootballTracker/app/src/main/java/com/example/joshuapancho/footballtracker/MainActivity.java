package com.example.joshuapancho.footballtracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {

    TextView heading;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heading = (TextView) findViewById(R.id.heading);
        content = (TextView) findViewById(R.id.content);

        new AsyncTaskParseJson().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        // Page apparently doesn't exist...?
        String yourJsonStringUrl = "http://footballdb.herokuapp.com/api/v1/event/de.2014_15/rounds?callback=?";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {
            heading.setText("Fixture");
            content.setText("Please wait...");
        }

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
                content.setText("Oops, something went wrong!");
            }

            content.setText(strFromDoInBg);
        }
    }

}



