package com.example.kvarnsen.footballtracker;

import java.util.*;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

        new AsyncTaskParser().execute();

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

    class AsyncTaskParser extends AsyncTask<String, String, ArrayList> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList doInBackground(String... arg0) {

            // instantiate our json parser
            JSONParser jParser = new JSONParser();

            // get json string from url
            ArrayList deArr, clArr, finalArr;

            deArr = jParser.getJSONFromUrl(0);
            clArr = jParser.getJSONFromUrl(1);

            finalArr = jParser.createFinalArray(deArr, clArr);

            return finalArr;

        }

        @Override
        protected void onPostExecute(ArrayList myList) {

            progText.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(myList);
            mRecyclerView.setAdapter(mAdapter);


        }
    }

}

