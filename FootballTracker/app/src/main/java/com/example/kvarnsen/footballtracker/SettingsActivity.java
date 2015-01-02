package com.example.kvarnsen.footballtracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kvarnsen.footballtracker.ui.SettingsFragment;

/**
 * Created by joshuapancho on 31/12/14.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.prefs_with_actionbar, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        Toolbar mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.prefs_toolbar);
        mToolBar.setTitle("Football Tracker");
        mToolBar.setNavigationIcon(R.drawable.ic_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getFragmentManager().beginTransaction()
                .replace(R.id.prefs_frame, new SettingsFragment())
                .commit();
    }



}
