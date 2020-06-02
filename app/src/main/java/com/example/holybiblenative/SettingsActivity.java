package com.example.holybiblenative;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import static com.example.holybiblenative.MainActivity.SHARED_PREFERENCE_NAME;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox themeA, themeB, themeC, defaultTheme;
    private SharedPreferences sharedPreferences;
    private String choiceTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        //choiceTheme = sharedPreferences.getInt("THEME", 0);

        defaultTheme = findViewById(R.id.themes1);
        themeA = findViewById(R.id.themes2);
        themeB = findViewById(R.id.themes3);
        themeC = findViewById(R.id.themes4);

        defaultTheme.setOnClickListener(this);
        themeA.setOnClickListener(this);
        themeB.setOnClickListener(this);
        themeC.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.themes1:
                //
                saveTheme(1);
                break;

            case R.id.themes2:
                //
                saveTheme(2);
                break;

            case R.id.themes3:
                //
                saveTheme(3);
                break;

            case R.id.themes4:
                //
                saveTheme(4);
                break;

            default:
                //
                saveTheme(0);
                break;
        }

    }

    private void saveTheme(int themeId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("THEME", themeId);
        editor.apply();
    }
}
