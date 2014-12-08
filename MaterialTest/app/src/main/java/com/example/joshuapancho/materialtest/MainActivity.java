package com.example.joshuapancho.materialtest;

import org.json.JSONArray;
import java.util.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.*;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = (TextView) findViewById(R.id.content);

        // UI stuff, set title
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

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
            content.setText("Please wait...");
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

            String finalRes = "";
            Game curGame;

            if(myList.isEmpty()) {
                content.setText("Oops, something went wrong!");
            }

            for(int i=0; i < myList.size(); i++) {

                curGame = (Game) myList.get(i);

                finalRes = finalRes + curGame.dateStr + "\n" + curGame.details + "\n\n";
            }

            content.setText(finalRes);

        }
    }

}

