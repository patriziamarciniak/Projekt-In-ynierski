package com.example.hp.firstapp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


public class Settings extends PreferenceActivity {

    Context context;
    SharedPreferences preferences;
    EditTextPreference userNameEditText, messageEditText;
    Resources res;
    String messageText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        addPreferencesFromResource(R.xml.settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        res = getResources();
        messageText = res.getString(R.string.message_text_summary)+ " " +
                 res.getString(R.string.user_name_summary);

        userNameEditText = (EditTextPreference) findPreference("userName");
        userNameEditText.setSummary(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("userName", res.getString(R.string.user_name_summary)));
        userNameEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                userNameEditText.setSummary(newValue.toString());
                userNameEditText.setText(newValue.toString());
                messageText = res.getString(R.string.message_text_summary, res.getString(R.string.event_localization_end)) + " " +
                        newValue.toString();
                messageEditText.setSummary(messageText);
                return false;
            }
        });

        messageEditText = (EditTextPreference) findPreference("message");
        messageEditText.setSummary(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("message", messageText));
        messageEditText.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("message", messageText));
        messageEditText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                messageEditText.setSummary(newValue.toString());
                messageEditText.setText(newValue.toString());
                return false;
            }
        });
        }

}

