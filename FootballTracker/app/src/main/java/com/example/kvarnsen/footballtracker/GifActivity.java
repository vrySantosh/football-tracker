package com.example.kvarnsen.footballtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class GifActivity extends ActionBarActivity {

    TextView gifText;
    TeamMap myMap = new TeamMap();

    int id = 2;
    String team = "arsenal";    // NOTE: both id and team are placeholders until global vars are established

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        Toolbar toolbar = (Toolbar) findViewById(R.id.gif_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

        String teamKey = myMap.fetchSub(id, team);
        new AsyncRedditFetcher().execute(teamKey);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gif, menu);
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

    class AsyncRedditFetcher extends AsyncTask<String, String, ArrayList> {

        private JSONArray urlArray;
        private ArrayList listing = new ArrayList<String>();
        private String url;
        private String urlFirst = "http://www.reddit.com/r/";
        private String urlSecond = "/search.json?q=site%3Agfycat&sort=new&restrict_sr=on";

        @Override
        protected void onPreExecute() {}

        @Override
        protected ArrayList doInBackground(String... args) {

            URL myURL = null;
            HttpURLConnection urlConnection = null;
            String rawJSON;
            String jsonOutput;
            JSONArray rawJArr;
            JSONObject myJSON, curJSON;

            url = urlFirst + args[0] + urlSecond;

            try {
                myURL = new URL(url);
            } catch(MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                urlConnection = (HttpURLConnection) myURL.openConnection();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

            if(urlConnection == null) {
                return listing;
            }

            try {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder out = new StringBuilder();

                rawJSON = reader.readLine();
                reader.close();

                try {

                    myJSON = new JSONObject(rawJSON);
                    curJSON = myJSON.getJSONObject("data");
                    rawJArr = curJSON.getJSONArray("children");

                    for(int i=0; i < rawJArr.length(); i++) {
                        curJSON = rawJArr.getJSONObject(i).getJSONObject("data");
                        listing.add(curJSON.getString("url"));
                    }

                } catch(JSONException e) {
                    e.printStackTrace();
                    return listing;
                }

            } catch (IOException e) {
                return listing;
            } finally {
                urlConnection.disconnect();
            }

            return listing;

        }

        @Override
        protected void onPostExecute(ArrayList myList) {

            String url = (String) myList.get(0);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);

            /*
                SOME NOTES ABOUT THIS FUNCTIONALITY:

                The above code works in opening the gfy in the browser.

                Difficult to embed the video in the actual app (webview layout settings get overridden, and the webview
                becomes huge and becomes scrollable.

             */

        }
    }


}
