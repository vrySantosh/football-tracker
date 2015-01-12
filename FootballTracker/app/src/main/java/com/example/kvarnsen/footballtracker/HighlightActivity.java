package com.example.kvarnsen.footballtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvarnsen.footballtracker.adapters.HighlightAdapter;
import com.example.kvarnsen.footballtracker.containers.Highlight;
import com.example.kvarnsen.footballtracker.globals.Globals;
import com.example.kvarnsen.footballtracker.teamhandlers.LeagueSelectorActivity;
import com.example.kvarnsen.footballtracker.utility.TeamMap;

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

/*
    Displays gfycat Highlights for the designated team.
 */

public class HighlightActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView progText;
    private ProgressBar progBar;
    private ArrayList urls;
    TeamMap myMap = new TeamMap();
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    SharedPreferences mySPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight);

        mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout curLayout = (LinearLayout) findViewById(R.id.highlightButton);
        curLayout.setClickable(false);
        curLayout.setBackgroundColor(getResources().getColor(R.color.grey));

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(1000);

                Toast toast = Toast.makeText(getApplicationContext(), v.getContentDescription(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                toast.show();
                return true;
            }
        };

        findViewById(R.id.refresh_button).setOnLongClickListener(listener);

        progText = (TextView) findViewById(R.id.gif_loading_text);
        progBar = (ProgressBar) findViewById(R.id.gif_progbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.gif_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);

        /* RecyclerView setup code adapted from https://developer.android.com/training/material/lists-cards.html */
        mRecyclerView = (RecyclerView) findViewById(R.id.gif_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.gif_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer, R.string.main);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        String team = preferences.getString("curTeam", null);
        String id = preferences.getString("curId", null);

        urls = ((Globals) this.getApplication()).getHighlights();

        if(urls != null) {
            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            mAdapter = new HighlightAdapter(urls);
            mRecyclerView.setAdapter(mAdapter);
            return;
        }

        if(team == null || id == null) {
            progText.setText("No team selected. Please select a team!");
            progBar.setVisibility(View.GONE);
        } else {
            String teamKey = myMap.fetchSub(id, team);

            if(teamKey == null) {
                progText.setClickable(false);
                progText.setText("Sorry, no highlights available for that team!");
                progBar.setVisibility(View.GONE);
            } else {
                new AsyncRedditFetcher().execute(teamKey);
            }

        }

    }

    @Override
    public void onPause() {
       super.onPause();

       ((Globals) this.getApplication()).setHighlights(urls);

    }

    public void onHighlightClick(View v) {

        int pos = v.getId();
        String url = ((Highlight) urls.get(pos)).url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }

    public void onSettingsClick(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onFixtureClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onGifSelectTeamClick(View v) {
        Intent intent = new Intent(this, LeagueSelectorActivity.class);
        startActivity(intent);
    }

    public void onHighlightRefreshClick(View v) {

        progText.setVisibility(View.VISIBLE);
        progBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        String team = preferences.getString("curTeam", null);
        String id = preferences.getString("curId", null);

        if(team == null || id == null) {
            progText.setText("No team selected. Please select a team!");
            progBar.setVisibility(View.GONE);
        } else {
            String teamKey = myMap.fetchSub(id, team);

            if(teamKey == null) {
                progText.setClickable(false);
                progText.setText("Sorry, no highlights available for that team!");
                progBar.setVisibility(View.GONE);
            } else {
                new AsyncRedditFetcher().execute(teamKey);
            }

        }



    }

    /*
        Responsible for contacting the Reddit API and grabbing new gfycat posts from a team's subreddit.

        Also handles creation of Highlight instances.
     */
    class AsyncRedditFetcher extends AsyncTask<String, String, ArrayList> {

        private ArrayList listing = new ArrayList<Highlight>();
        private String url;
        private String urlFirst = "http://www.reddit.com/r/";
        private String urlSecond = "/search.json?q=site%3Agfycat&sort=new&restrict_sr=on";

        @Override
        protected void onPreExecute() {
            progText.setClickable(false);
        }

        @Override
        protected ArrayList doInBackground(String... args) {

            URL myURL = null;
            HttpURLConnection urlConnection = null;
            String rawJSON;
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

                rawJSON = reader.readLine();
                reader.close();

                try {

                    int highlightMax;
                    Bitmap bitmap = null;
                    Highlight curHighlight;
                    myJSON = new JSONObject(rawJSON);
                    curJSON = myJSON.getJSONObject("data");
                    rawJArr = curJSON.getJSONArray("children");

                    String strHighlightNo =  mySPrefs.getString("pref_highlightNo", "notFound");

                    if(strHighlightNo.equals("notFound")) {
                        Log.w("FT", "shared pref not found");
                        return null;
                    }

                    highlightMax = Integer.parseInt(strHighlightNo);

                    for(int i=0; i < rawJArr.length(); i++) {

                        if(i == highlightMax) {
                            break;
                        }

                        curJSON = rawJArr.getJSONObject(i).getJSONObject("data");

                        String url = curJSON.getString("url");
                        String[] parts = url.split("//");
                        String gfylink;

                        String curLink = parts[1];
                        String[] newParts;

                        if(curLink.contains(".gif")) {

                            newParts = curLink.split(".gif");
                            curLink = newParts[0];

                            if(curLink.contains("#")) {
                                newParts = curLink.split("#");
                                curLink = newParts[0];
                            }

                            if(curLink.contains("www.")) {
                                newParts = curLink.split("www.");
                                curLink = newParts[1];
                            }

                            if(curLink.contains("giant.")) {
                                newParts = curLink.split("giant.");
                                curLink = newParts[1];
                            }

                            gfylink = curLink;

                        } else if(curLink.contains("#")) {

                            newParts = curLink.split("#");
                            gfylink = newParts[0];

                        } else if(curLink.contains("www.")) {

                            newParts = curLink.split("www.");
                            gfylink = newParts[1];

                        } else if(curLink.contains("giant.")) {

                            newParts = curLink.split("giant.");
                            gfylink = newParts[1];

                        } else {
                            gfylink = curLink;
                        }

                        String imageUrl = "http://thumbs." + gfylink + "-thumb100.jpg";

                        try {
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        curHighlight = new Highlight(curJSON.getString("title"), url, bitmap);

                        listing.add(curHighlight);

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

            urls = myList;

            if(urls == null || urls.size() == 0) {
                progText.setText("Sorry, no highlights available for that team!");
                progBar.setVisibility(View.GONE);
                return;
            }

            // "remove" loading text and progress wheel
            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            mAdapter = new HighlightAdapter(myList);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(mAdapter);


        }
    }


}
