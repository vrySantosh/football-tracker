package com.example.kvarnsen.footballtracker.teamhandlers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import com.example.kvarnsen.footballtracker.Globals;
import com.example.kvarnsen.footballtracker.MainActivity;
import com.example.kvarnsen.footballtracker.R;

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

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("curTeam", message);
        editor.putString("curId", "liga");
        editor.commit();

        ((Globals) this.getApplication()).setFixture(null);
        ((Globals) this.getApplication()).setHighlights(null);

        startActivity(intent);

    }

}
