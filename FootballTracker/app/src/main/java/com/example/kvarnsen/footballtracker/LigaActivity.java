package com.example.kvarnsen.footballtracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

/*
    Activity to handle La Liga team selections
 */
public class LigaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liga);

        Toolbar toolbar = (Toolbar) findViewById(R.id.selector_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

    }

    public void onRadioButtonClicked(View v) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boolean checked = ((RadioButton) v).isChecked();

        String message = "";

        switch(v.getId()) {
            case R.id.radio_almeria:
                if (checked)
                    message = "almeria";
                break;
            case R.id.radio_bilbao:
                if (checked)
                    message = "athletic";
                break;
            case R.id.radio_atletico:
                if (checked)
                    message = "atletico";
                break;
            case R.id.radio_barcelona:
                if (checked)
                    message = "barcelona";
                break;
            case R.id.radio_celta:
                if (checked)
                    message = "celta";
                break;
            case R.id.radio_cordoba:
                if (checked)
                    message = "cordoba";
                break;
            case R.id.radio_deportivo:
                if (checked)
                    message = "deportivo";
                break;
            case R.id.radio_eibar:
                if (checked)
                    message = "eibar";
                break;
            case R.id.radio_elche:
                if (checked)
                    message = "elche";
                break;
            case R.id.radio_espanyol:
                if (checked)
                    message = "espanyol";
                break;
            case R.id.radio_getafe:
                if (checked)
                    message = "getafe";
                break;
            case R.id.radio_granada:
                if (checked)
                    message = "granada";
                break;
            case R.id.radio_levante:
                if (checked)
                    message = "levante";
                break;
            case R.id.radio_malaga:
                if (checked)
                    message = "malaga";
                break;
            case R.id.radio_vallecano:
                if (checked)
                    message = "rayo";
                break;
            case R.id.radio_madrid:
                if (checked)
                    message = "madrid";
                break;
            case R.id.radio_sociedad:
                if (checked)
                    message = "realsociedad";
                break;
            case R.id.radio_sevilla:
                if (checked)
                    message = "sevilla";
                break;
            case R.id.radio_valencia:
                if (checked)
                    message = "valencia";
                break;
            case R.id.radio_villareal:
                if (checked)
                    message = "villareal";
                break;
        }

        ((Globals) this.getApplication()).setTeam(message);
        ((Globals) this.getApplication()).setId("liga");

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
