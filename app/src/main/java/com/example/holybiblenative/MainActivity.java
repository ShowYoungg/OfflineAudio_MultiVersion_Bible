package com.example.holybiblenative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private SavedChaptersAndVerses savedChaptersAndVerses;
    private SharedPreferences sharedPreferences;
    private LinearLayout progressBarLayout;
    private List<String> ll;
    private String[] words;
    private ArrayList<DataObject> dl;
    private String dbName;
    private int databaseStatus;
    public static int themeId;
    private TextView privacy;
    private ListView listView;
    private FloatingActionButton fab;
    public static String content_field;
    //public static int themeNumber;

    public static final String SHARED_PREFERENCE_NAME = "Database";
    public static ArrayList<DataObject> savedVerses;


    private String[] all = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
            "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms",
            "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
            "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum",
            "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark",
            "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians",
            "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians",
            "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter",
            "1 John", "2 John", "3 John", "Jude", "Revelation"};

    private String[] oldNewAll = {"All Books", "Old Testament", "New Testament", "Saved Verses"};
    private String welcomeWord = "For God so love the world that he gave his only Begotten Son" +
            " that whosoever believe in him shall have not perish but have everlasting life";

    int checkerA = 0;
    int checkerB = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.version_menus_main, menu);
        return true;
    }


    private void saveVersionOption(String field){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VERSION", field);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.akjv:
                //
                content_field = "field13";
                saveVersionOption(content_field);
                Toast.makeText(this, "American King James version saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.kjv:
                //
                content_field = "field5";
                saveVersionOption(content_field);
                Toast.makeText(this, "King James version saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.bbe:
                //
                content_field = "field14";
                saveVersionOption(content_field);
                Toast.makeText(this, "Basic Bible English version saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.wbt:
                //
                content_field = "field10";
                saveVersionOption(content_field);
                Toast.makeText(this, "Webster Bible version saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ylt:
                //
                content_field = "field12";
                saveVersionOption(content_field);
                Toast.makeText(this, "Young's Literal Translation saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.darby:
                //
                content_field = "field8";
                saveVersionOption(content_field);
                Toast.makeText(this, "Darby Bible Translation saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.drb:
                //
                content_field = "field7";
                saveVersionOption(content_field);
                Toast.makeText(this, "Douay-Rheims Bible saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.asv:
                //
                content_field = "field6";
                saveVersionOption(content_field);
                Toast.makeText(this, "American Standard Version saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.erv:
                //
                content_field = "field9";
                saveVersionOption(content_field);
                Toast.makeText(this, "English Revised Version saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.web:
                //
                content_field = "field11";
                saveVersionOption(content_field);
                Toast.makeText(this, "World English Bible saved", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        progressBarLayout.setVisibility(View.VISIBLE);
//        listView.setVisibility(View.GONE);

        if (databaseStatus == 0){
            listView.setVisibility(View.GONE);
            progressBarLayout.setVisibility(View.VISIBLE);
            monitorFileCopy();

            copyAllBooks();
        } else {
            Log.i("DatabaseStatus", "books already copied");
            loadSavedVerses();
        }
    }

    private void monitorFileCopy() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkCopyStatus()){
                    progressBarLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    monitorFileCopy();
                }
            }
        }, 400);
    }

    private boolean checkCopyStatus(){
        String DB_PATH_CHECK1 = getApplicationContext().getDatabasePath("Revelation.db").getAbsolutePath();
        String DB_PATH_CHECK2 = getApplicationContext().getDatabasePath("Matthew.db").getAbsolutePath();
        String DB_PATH_CHECK3 = getApplicationContext().getDatabasePath("OldTestament.db").getAbsolutePath();
        File file1 = new File(DB_PATH_CHECK1);
        File file2 = new File(DB_PATH_CHECK2);
        File file3 = new File(DB_PATH_CHECK3);
        if (file1.exists() && file2.exists() && file3.exists()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.BibleAppTheme1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = new ArrayList<>();
        savedChaptersAndVerses = new SavedChaptersAndVerses();

        progressBarLayout = findViewById(R.id.progress_bar_layout);

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        databaseStatus = sharedPreferences.getInt("DATABASE", 0);
        themeId = sharedPreferences.getInt("THEME", 0);

        listView = findViewById(R.id.books_list);
//        fab = findViewById(R.id.floatingActionButton3);
        privacy = findViewById(R.id.privacy_policy);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                themeNumber = 1;
////                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.chapter_list, oldNewAll );
////                listView.setAdapter(adapter);
//
//                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(intent);
//
//            }
//        });

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, oldNewAll );
        listView.setAdapter(adapter);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://htmlpreview.github.io/?https://github.com/ShowYoungg/HolyBibleNative/blob/master/app/privacy_policy.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3){
                    Intent intent = new Intent(MainActivity.this,
                            DisplayActivity.class).putExtra("Position", position);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this,
                            TestamentsActivity.class).putExtra("Position", position);
                    startActivity(intent);
                }
            }
        });
    }

    private void copyAllBooks() {
        for (String s: all) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    dbName = s + ".db";

                    if (s.equals("Galatians") || s.equals("Mark") || s.equals("Ephesians") || s.equals("3 John")
                            || s.equals("Philippians") || s.equals("Colossians") || s.equals("1 Thessalonians")
                            || s.equals("2 Thessalonians")|| s.equals("Jude") || s.equals("1 Timothy")
                            || s.equals("2 Timothy") || s.equals("Titus") || s.equals("Philemon")
                            || s.equals("Hebrews") || s.equals("James") || s.equals("1 Peter")
                            || s.equals("2 Peter") || s.equals("1 John") || s.equals("2 John")) {

                        if (checkerA == 0){
                            try {
                                copyDatabase("NewTestament.db");
                                checkerA = 1;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (s.equals("Ruth") || s.equals("Song of Solomon") || s.equals("Lamentations")
                            || s.equals("Joel") || s.equals("Amos") || s.equals("Obadiah")
                            || s.equals("Jonah") || s.equals("Micah") || s.equals("Nahum")
                            || s.equals("Habakkuk") || s.equals("Zephaniah") || s.equals("Hosea")
                            || s.equals("Haggai") || s.equals("Malachi") || s.equals("Joshua")){

                        if (checkerB == 0){
                            try {
                                copyDatabase("OldTestament.db");
                                checkerB = 1;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            copyDatabase(dbName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void copyDatabase(String dbName) throws IOException{
        InputStream inputStream = getApplicationContext().getAssets().open(dbName);
        String DB_PATH = getApplicationContext().getDatabasePath(dbName).getAbsolutePath();
        OutputStream outputStream = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer))> 0){
            outputStream.write(buffer, 0, length);
        }
        Log.i("DATABASE_PATH", DB_PATH);

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        if (dbName.equals("Revelation.db")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("DATABASE", 1);
            editor.apply();
        }
    }

    private void loadSavedVerses() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getDataObj().observe(this, new Observer<List<DataObject>>() {
            @Override
            public void onChanged(@Nullable List<DataObject> dataObjects) {
                //savedVerses = new ArrayList<>();
                if (dataObjects != null){
                    savedVerses = new ArrayList<>(dataObjects);
                }
            }
        });
    }
}
