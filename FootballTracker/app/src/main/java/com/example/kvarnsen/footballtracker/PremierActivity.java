package com.example.kvarnsen.footballtracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


public class PremierActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.kvarnsen.MESSAGE";
    public final static String EXTRA_ID = "com.example.kvarnsen.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premier);

        Toolbar toolbar = (Toolbar) findViewById(R.id.selector_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

    }

    public void onRadioButtonClicked(View v) {

        Intent intent = new Intent(this, MainActivity.class);

        boolean checked = ((RadioButton) v).isChecked();

        String message = "";

        switch(v.getId()) {
            case R.id.radio_arsenal:
                if (checked)
                    message = "arsenal";
                break;
            case R.id.radio_aston:
                if (checked)
                    message = "astonvilla";
                break;
            case R.id.radio_burnley:
                if (checked)
                    message = "burnley";
                break;
            case R.id.radio_chelsea:
                if (checked)
                    message = "chelsea";
                break;
            case R.id.radio_palace:
                if (checked)
                    message = "crystalpalace";
                break;
            case R.id.radio_everton:
                if (checked)
                    message = "everton";
                break;
            case R.id.radio_hull:
                if (checked)
                    message = "hull";
                break;
            case R.id.radio_leicester:
                if (checked)
                    message = "leicester";
                break;
            case R.id.radio_liverpool:
                if (checked)
                    message = "liverpool";
                break;
            case R.id.radio_city:
                if (checked)
                    message = "mancity";
                break;
            case R.id.radio_manu:
                if (checked)
                    message = "manutd";
                break;
            case R.id.radio_newcastle:
                if (checked)
                    message = "newcastle";
                break;
            case R.id.radio_qpr:
                if (checked)
                    message = "qpr";
                break;
            case R.id.radio_southampton:
                if (checked)
                    message = "southampton";
                break;
            case R.id.radio_stoke:
                if (checked)
                    message = "stoke";
                break;
            case R.id.radio_sunderland:
                if (checked)
                    message = "sunderland";
                break;
            case R.id.radio_swansea:
                if (checked)
                    message = "swansea";
                break;
            case R.id.radio_tottenham:
                if (checked)
                    message = "tottenham";
                break;
            case R.id.radio_westbrom:
                if (checked)
                    message = "westbrom";
                break;
            case R.id.radio_westham:
                if (checked)
                    message = "westham";
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

