package com.example.kvarnsen.footballtracker;

import android.os.Bundle;
import android.preference.PreferenceFragment;

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
