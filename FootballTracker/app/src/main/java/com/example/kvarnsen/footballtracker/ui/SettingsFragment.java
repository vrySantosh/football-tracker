package com.example.kvarnsen.footballtracker.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.kvarnsen.footballtracker.R;

/**
 * Created by joshuapancho on 31/12/14.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
