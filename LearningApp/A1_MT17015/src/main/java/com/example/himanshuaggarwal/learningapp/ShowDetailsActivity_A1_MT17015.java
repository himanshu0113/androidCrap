package com.example.himanshuaggarwal.learningapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class ShowDetailsActivity_A1_MT17015 extends AppCompatActivity {

    private static TextView content;
    private String title, text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        content = (TextView) findViewById(R.id.content);

        Bundle b = getIntent().getExtras();
        title = b.getString("title");
        text = b.getString("content");
        updateStrings();

    }

//    @Override
//    public boolean onNavigateUp() {
//        finish();
//        return super.onNavigateUp();
//    }

    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("old_locale", Locale.getDefault());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        Locale locale = (Locale) savedInstanceState.getSerializable("old_locale");
//        Locale.setDefault(locale);
//        Configuration conf = new Configuration();
//        conf.locale = locale;
//        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
//        updateStrings();
//    }

    private void updateStrings() {
        setTitle(title);
        content.setText(text);
    }
}
