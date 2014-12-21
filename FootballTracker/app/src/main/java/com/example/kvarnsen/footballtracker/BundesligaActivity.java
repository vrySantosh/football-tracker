package com.example.kvarnsen.footballtracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


public class BundesligaActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.kvarnsen.MESSAGE";
    public final static String EXTRA_ID = "com.example.kvarnsen.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundesliga);

        Toolbar toolbar = (Toolbar) findViewById(R.id.selector_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

    }

    public void onRadioButtonClicked(View v) {

        Intent intent = new Intent(this, MainActivity.class);

        boolean checked = ((RadioButton) v).isChecked();

        String message = "";

        switch(v.getId()) {
            case R.id.radio_augsburg:
                if (checked)
                    message = "augsburg";
                    break;
            case R.id.radio_leverkusen:
                if (checked)
                    message = "leverkusen";
                    break;
            case R.id.radio_bayern:
                if (checked)
                    message = "bayern";
                    break;
            case R.id.radio_dortmund:
                if (checked)
                    message = "dortmund";
                    break;
            case R.id.radio_gladbach:
                if (checked)
                    message = "mgladbach";
                    break;
            case R.id.radio_frankfurt:
                if (checked)
                    message = "frankfurt";
                    break;
            case R.id.radio_freiburg:
                if (checked)
                    message = "freiburg";
                    break;
            case R.id.radio_hamburger:
                if (checked)
                    message = "hsv";
                    break;
            case R.id.radio_hannover:
                if (checked)
                    message = "hannover";
                    break;
            case R.id.radio_hertha:
                if (checked)
                    message = "herthabsc";
                break;
            case R.id.radio_hoffenheim:
                if (checked)
                    message = "hoffenheim";
                break;
            case R.id.radio_koln:
                if (checked)
                    message = "koeln";
                break;
            case R.id.radio_mainz:
                if (checked)
                    message = "mainz";
                break;
            case R.id.radio_paderborn:
                if (checked)
                    message = "paderborn";
                break;
            case R.id.radio_schalke:
                if (checked)
                    message = "schalke";
                break;
            case R.id.radio_stuttgart:
                if (checked)
                    message = "stuttgart";
                break;
            case R.id.radio_bremen:
                if (checked)
                    message = "bremen";
                break;
            case R.id.radio_wolfsburg:
                if (checked)
                    message = "wolfsburg";
                break;
        }

        intent.putExtra(EXTRA_ID, "premier");
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_selector, menu);
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
}
