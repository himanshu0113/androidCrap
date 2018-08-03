package com.example.himanshuaggarwal.learningapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class ShowButtons_A1_MT17015 extends AppCompatActivity {

    private static Button about_but, program_but, admission_but;
//    private static Locale locale;

    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_buttons);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle b = getIntent().getExtras();
        Locale locale = (Locale) b.getSerializable("locale");

        about_but = (Button) findViewById(R.id.button2);
        program_but = (Button) findViewById(R.id.button3);
        admission_but = (Button) findViewById(R.id.button4);

        setLocale(locale);

    }

    public void onClickAbout(View view) {
        Intent intent = new Intent(this, ShowDetailsActivity_A1_MT17015.class);
        Bundle b = new Bundle();
        b.putString("title", getString(R.string.about));
        b.putString("content", getString(R.string.large_text_about));
        intent.putExtras(b);
        startActivity(intent);
    }

    public void onClickProgram(View view) {
        Intent intent = new Intent(this, ShowDetailsActivity_A1_MT17015.class);
        Bundle b = new Bundle();
        b.putString("title", getString(R.string.programs));
        b.putString("content", getString(R.string.large_text_program));
        intent.putExtras(b);
        startActivity(intent);
    }

    public void onClickAdmission(View view) {
        Intent intent = new Intent(this, ShowDetailsActivity_A1_MT17015.class);
        Bundle b = new Bundle();
        b.putString("title", getString(R.string.admission));
        b.putString("content", getString(R.string.large_text_admission));
        intent.putExtras(b);
        startActivity(intent);
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        editor.putString(Locale_KeyValue, locale.getLanguage());
        editor.commit();
        Configuration conf = new Configuration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        updateStrings();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//         outState.putSerializable("old_locale", Locale.getDefault());
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
        about_but.setText(R.string.about);
        program_but.setText(R.string.programs);
        admission_but.setText(R.string.admission);
    }
}
