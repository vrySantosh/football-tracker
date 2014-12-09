package com.example.joshuapancho.materialtest;

import java.util.*;
import android.os.AsyncTask;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progText = (TextView) findViewById(R.id.loading_text);
        progBar = (ProgressBar) findViewById(R.id.progbar);

        // UI stuff, set title
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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

    class AsyncTaskParseJson extends AsyncTask<String, String, ArrayList> {

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

