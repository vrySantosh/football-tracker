package com.example.kvarnsen.footballtracker;

import java.util.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView progText;
    private ProgressBar progBar;
    private String PREFS_NAME = "team_prefs";
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ArrayList fixture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout curLayout = (LinearLayout) findViewById(R.id.fixtureButton);
        curLayout.setClickable(false);
        curLayout.setBackgroundColor(getResources().getColor(R.color.grey));

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

        String team = ((Globals) this.getApplication()).getTeam();
        String id = ((Globals) this.getApplication()).getId();
        fixture = ((Globals) this.getApplication()).getFixture();

        if(fixture != null) {
            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            mAdapter = new MainAdapter(fixture);
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
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("Football Tracker");
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {

        exit();

    }

    public void onSelectorClick(View v) {
        Intent intent = new Intent(this, LeagueSelectorActivity.class);
        startActivity(intent);
    }

    public void onHighlightClick(View v) {
        Intent intent = new Intent(this, GifActivity.class);
        startActivity(intent);
    }

    /*
        Create ArrayList of Game instances for fixture use
     */
    class AsyncTaskParser extends AsyncTask<String, String, ArrayList> {

        @Override
        protected void onPreExecute() {}

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

            // "remove" loading text and progress wheel
            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            // specify an adapter (see also next example)
            mAdapter = new MainAdapter(myList);
            mRecyclerView.setAdapter(mAdapter);


        }
    }

}

