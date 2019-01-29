package com.android.fileexplorerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.HashMap;

public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Настройки");

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);


    }
}
