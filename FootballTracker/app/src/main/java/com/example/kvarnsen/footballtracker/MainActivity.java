package com.example.kvarnsen.footballtracker;

import java.util.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvarnsen.footballtracker.adapters.FixtureAdapter;
import com.example.kvarnsen.footballtracker.globals.Globals;
import com.example.kvarnsen.footballtracker.teamhandlers.LeagueSelectorActivity;
import com.example.kvarnsen.footballtracker.utility.JSONParser;

/*
    Main entry point for the app.

    Displays upcoming fixture for the designated team.
 */
public class MainActivity extends ActionBarActivity {

    public final static String PREFS_NAME = "FootballTrackerPrefs";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView progText;
    private ProgressBar progBar;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ArrayList fixture = null;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        LinearLayout curLayout = (LinearLayout) findViewById(R.id.fixtureButton);
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

        progText = (TextView) findViewById(R.id.loading_text);
        progBar = (ProgressBar) findViewById(R.id.progbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);  // ic_nav icon from Google's Material Design System Icons pack

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer, R.string.main);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /* RecyclerView setup code adapted from https://developer.android.com/training/material/lists-cards.html */
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onPause() {
        super.onPause();

        ((Globals) this.getApplication()).setFixture(fixture);
    }

    @Override
    public void onResume() {
        super.onResume();

        preferences = getSharedPreferences(PREFS_NAME, 0);

        String team = preferences.getString("curTeam", null);
        String id = preferences.getString("curId", null);

        fixture = ((Globals) this.getApplication()).getFixture();

        if(fixture != null) {
            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            mAdapter = new FixtureAdapter(fixture);
            mRecyclerView.setAdapter(mAdapter);
            return;
        }

        if (team == null) {
            progText.setText("Please select a team.");
            progBar.setVisibility(View.GONE);
        }
        else {

            switch (id) {
                case "bundesliga":
                    new AsyncTaskParser().execute(team, id);
                    break;
                case "liga":
                    new AsyncTaskParser().execute(team, id);
                    break;
                case "premier":
                    new AsyncTaskParser().execute(team, id);
                    break;
                default:
                    break;
            }


        }
    }

    // code adapted from http://stackoverflow.com/questions/6290599/prompt-user-when-back-button-is-pressed
    private void exit() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });

        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Do you want to quit?");
        alertDialog.setTitle("Football Tracker");
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {

        exit();

    }

    public void onSettingsClick(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onHighlightClick(View v) {
        Intent intent = new Intent(this, HighlightActivity.class);
        startActivity(intent);
    }

    public void onSelectTeamClick(View v) {
        Intent intent = new Intent(this, LeagueSelectorActivity.class);
        startActivity(intent);
    }

    public void onRefreshClick(View v) {

        progText.setVisibility(View.VISIBLE);
        progBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        preferences = getSharedPreferences(PREFS_NAME, 0);

        String team = preferences.getString("curTeam", null);
        String id = preferences.getString("curId", null);

        if (team == null) {
            progText.setText("Please select a team.");
            progBar.setVisibility(View.GONE);
        }
        else {

            switch (id) {
                case "bundesliga":
                    new AsyncTaskParser().execute(team, id);
                    break;
                case "liga":
                    new AsyncTaskParser().execute(team, id);
                    break;
                case "premier":
                    new AsyncTaskParser().execute(team, id);
                    break;
                default:
                    break;
            }


        }

    }

    /*
        Create ArrayList of Game instances for fixture use
     */
    class AsyncTaskParser extends AsyncTask<String, String, ArrayList> {

        @Override
        protected void onPreExecute() {
            progText.setClickable(false);
        }

        @Override
        protected ArrayList doInBackground(String... arg0) {

            JSONParser jParser = new JSONParser();

            // get json string from url
            ArrayList leagueArr = null;
            ArrayList clArr = null;
            ArrayList finalArr = null;

            switch (arg0[1]) {
                case "bundesliga":
                    leagueArr = jParser.getJSONFromUrl(0, arg0[0]);
                    clArr = jParser.getJSONFromUrl(3, arg0[0]);
                    break;
                case "liga":
                    leagueArr = jParser.getJSONFromUrl(1, arg0[0]);
                    clArr = jParser.getJSONFromUrl(3, arg0[0]);
                    break;
                case "premier":
                    leagueArr = jParser.getJSONFromUrl(2, arg0[0]);
                    clArr = jParser.getJSONFromUrl(3, arg0[0]);
                    break;
            }

            if(leagueArr != null && clArr != null)
                finalArr = jParser.createFinalArray(leagueArr, clArr);

            return finalArr;

        }

        @Override
        protected void onPostExecute(ArrayList myList) {

            fixture = myList;

            if(fixture == null) {

                progBar.setVisibility(View.GONE);
                progText.setText("Unable to connect to server, please try again later.");

            } else {

                // "remove" loading text and progress wheel
                progText.setVisibility(View.GONE);
                progBar.setVisibility(View.GONE);

                // specify an adapter (see also next example)
                mAdapter = new FixtureAdapter(myList);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setAdapter(mAdapter);

            }


        }
    }

}

