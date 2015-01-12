package com.example.kvarnsen.footballtracker.teamhandlers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import com.example.kvarnsen.footballtracker.R;

/*
    Activity to display league options to a user
 */
public class LeagueSelectorActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_selector);

        Toolbar toolbar = (Toolbar) findViewById(R.id.league_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Football Tracker");

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onLeagueButtonClicked(View v) {

        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {

            case R.id.bundesliga:
                if (checked) {
                    Intent intent = new Intent(this, BundesligaActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.premier:
                if (checked) {
                    Intent intent = new Intent(this, PremierActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.laliga:
                if (checked) {
                    Intent intent = new Intent(this, LigaActivity.class);
                    startActivity(intent);
                    break;
                }

        }

        
    }

}
