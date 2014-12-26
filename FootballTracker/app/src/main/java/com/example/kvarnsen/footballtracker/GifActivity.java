package com.example.kvarnsen.footballtracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView progText;
    private ProgressBar progBar;
    private ArrayList urls;
    TeamMap myMap = new TeamMap();
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        LinearLayout curLayout = (LinearLayout) findViewById(R.id.highlightButton);
        curLayout.setClickable(false);
        curLayout.setBackgroundColor(getResources().getColor(R.color.grey));

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

        String team = ((Globals) this.getApplication()).getTeam();
        String id = ((Globals) this.getApplication()).getId();

        if(team == null || id == null) {
            progText.setText("No team selected. Please select a team!");
            progBar.setVisibility(View.GONE);
        } else {
            String teamKey = myMap.fetchSub(id, team);

            if(teamKey == null) {
                progText.setText("Sorry, no highlights available for that team!");
                progBar.setVisibility(View.GONE);
            } else {
                new AsyncRedditFetcher().execute(teamKey);
            }

        }

    }

    public void onHighlightClick(View v) {

        int pos = v.getId();
        String url = ((Highlight) urls.get(pos)).url;

        Log.w("URL", url);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }

    public void onSelectorClick(View v) {
        Intent intent = new Intent(this, LeagueSelectorActivity.class);
        startActivity(intent);
    }

    public void onFixtureClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        protected void onPreExecute() {}

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

                    Bitmap bitmap = null;
                    Highlight curHighlight;
                    myJSON = new JSONObject(rawJSON);
                    curJSON = myJSON.getJSONObject("data");
                    rawJArr = curJSON.getJSONArray("children");

                    for(int i=0; i < 5; i++) {
                        curJSON = rawJArr.getJSONObject(i).getJSONObject("data");

                        String url = curJSON.getString("url");
                        String[] parts = url.split("//");
                        String imageUrl = "http://thumbs." + parts[1] + "-poster.jpg";

                        try {
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        if(bitmap != null) {
                            curHighlight = new Highlight(curJSON.getString("title"), url, bitmap);
                            listing.add(curHighlight);
                        }
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

            // "remove" loading text and progress wheel
            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            mAdapter = new GifRecyclerAdapter(myList);
            mRecyclerView.setAdapter(mAdapter);


        }
    }


}
