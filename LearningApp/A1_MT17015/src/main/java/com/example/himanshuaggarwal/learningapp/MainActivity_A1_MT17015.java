package com.example.himanshuaggarwal.learningapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity_A1_MT17015 extends AppCompatActivity {
    private static Button hindi_but, english_but, proceed_but;
    private static TextView heading;
    private static Locale locale;

    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        language = "en_EN";

        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        heading = (TextView) findViewById(R.id.heading);
        proceed_but = (Button) findViewById(R.id.proceed_button);
        hindi_but = (Button) findViewById(R.id.hindi_button);
        english_but = (Button) findViewById(R.id.english_button);
        setListners();
        updateLocale(language);
        loadNewLocale();

    }

    private void setListners() {

        hindi_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language = "hi_HI";
                updateLocale(language);
            }
        });
        english_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language = "en_EN";
                updateLocale(language);
            }
        });

    }

    private void updateLocale(String language) {
        if(language.equalsIgnoreCase(""))
            return;
        locale = new Locale(language);
        editor.putString(Locale_KeyValue, language);
        editor.commit();
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        updateStrings();
    }

    private void updateStrings() {
        hindi_but.setText(R.string.hindi);
        english_but.setText(R.string.english);
        heading.setText(R.string.IIITD);
        proceed_but.setText(R.string.Proceed);

    }

    public void loadNewLocale() {
        String lang = sharedPreferences.getString(Locale_KeyValue, "");
        updateLocale(lang);
    }

    public void proceed(View view) {
        Intent intent = new Intent(this, ShowButtons_A1_MT17015.class);
        Bundle b = new Bundle();
        b.putSerializable("locale", locale);
        intent.putExtras(b);
        startActivity(intent);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("locale_language", language);
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        language = savedInstanceState.getString("locale_language");
//        updateLocale(language);
//    }
}
