package com.example.kvarnsen.footballtracker;

import java.util.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.*;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView progText;
    private ProgressBar progBar;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progText = (TextView) findViewById(R.id.loading_text);
        progBar = (ProgressBar) findViewById(R.id.progbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer, R.string.main);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /* RecyclerView setup code adapted from https://developer.android.com/training/material/lists-cards.html */
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        String message = intent.getStringExtra(BundesligaActivity.EXTRA_MESSAGE);
        String id = intent.getStringExtra(BundesligaActivity.EXTRA_ID);

        if (message == null) {
            progText.setText("Please select a team.");
            progBar.setVisibility(View.GONE);
        }
        else if (message.equals(" ")) {
            progText.setText("Please select a team.");
            progBar.setVisibility(View.GONE);
        }
        else {

            if(id.equals("bundesliga"))
                new AsyncTaskParser().execute(message, id);
            else if(id.equals("liga"))
                new AsyncTaskParser().execute(message, id);
            else
                new AsyncTaskParser().execute(message, id);


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

    public void onClick(View v) {
        Intent intent = new Intent(this, LeagueSelectorActivity.class);
        startActivity(intent);
    }

    class AsyncTaskParser extends AsyncTask<String, String, ArrayList> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList doInBackground(String... arg0) {

            // instantiate our json parser
            JSONParser jParser = new JSONParser();

            // get json string from url
            ArrayList leagueArr = null;
            ArrayList clArr = null;
            ArrayList finalArr = null;

            if(arg0[1].equals("bundesliga")) {
                leagueArr = jParser.getJSONFromUrl(0, arg0[0]);
                clArr = jParser.getJSONFromUrl(3, arg0[0]);
            } else if(arg0[1].equals("liga")) {
                leagueArr = jParser.getJSONFromUrl(1, arg0[0]);
                clArr = jParser.getJSONFromUrl(3, arg0[0]);
            } else if(arg0[1].equals("premier")) {
                leagueArr = jParser.getJSONFromUrl(2, arg0[0]);
                clArr = jParser.getJSONFromUrl(3, arg0[0]);
            }

            if(leagueArr != null && clArr != null)
                finalArr = jParser.createFinalArray(leagueArr, clArr);

            return finalArr;

        }

        @Override
        protected void onPostExecute(ArrayList myList) {

            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            // specify an adapter (see also next example)
            mAdapter = new MainAdapter(myList);
            mRecyclerView.setAdapter(mAdapter);


        }
    }

}

