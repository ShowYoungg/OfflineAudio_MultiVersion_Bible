package com.example.holybiblenative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DisplayActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,
        BibleFragment.OnChapterClickListener {

    public static ListView displayList;
    public static DisplayAdapter displayAdapter;
    public static ArrayList<DataObject> dataObjectsList;
    public ArrayList<DataObject> dataObjectArrayList;
    private DataObject dataObject;
    public String content;
    public String book;
    public String que;
    private String dbName, database_toUse;
    public int chapter;
    public int verse;
    private int number_of_chapters;
    private boolean twoPane = false;

    private TextToSpeech textToSpeech;
    private SharedPreferences sharedPreferences;

    private String SHARED_PREFERENCE_NAME = "SEARCH";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.version_menus, menu);

        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        dataObjectArrayList.clear();

        switch (item.getItemId()){
            //Navigation through action bar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.akjv:
                //
                content = "field13";
                loadData(content, book, chapter, verse,"");
                return true;

            case R.id.kjv:
                //
                content = "field5";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.bbe:
                //
                content = "field14";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.wbt:
                //
                content = "field10";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.ylt:
                //
                content = "field12";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.darby:
                //
                content = "field8";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.drb:
                //
                content = "field7";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.asv:
                //
                content = "field6";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.erv:
                //
                content = "field9";
                loadData(content, book, chapter, verse, "");
                return true;

            case R.id.web:
                //
                content = "field11";
                loadData(content, book, chapter, verse, "");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (number_of_chapters == 0){
            content = "field5";
            book = sharedPreferences.getString("Book", "Genesis.db");
            chapter = sharedPreferences.getInt("Chapter", 0);
            database_toUse = sharedPreferences.getString("DATATOUSE", book + ".db");
            number_of_chapters = sharedPreferences.getInt("Number of Chapters", 0);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        dataObjectArrayList = savedInstanceState.getParcelableArrayList("LIST");
        displayAdapter = new DisplayAdapter(getApplicationContext(), dataObjectArrayList, content);
        displayList.setAdapter(displayAdapter);
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if ((displayAdapter.getDataObjects()) != null) {
            savedInstanceState.putParcelableArrayList("LIST", new ArrayList<DataObject>(displayAdapter.getDataObjects()));
        } else{
            savedInstanceState.putParcelableArrayList("LIST", new ArrayList<DataObject>(dataObjectArrayList));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        textToSpeech = new TextToSpeech(getApplicationContext(), this, "com.google.android.tts");
        textToSpeech.setLanguage(Locale.ENGLISH);

        dataObject = new DataObject();
        dataObjectArrayList = new ArrayList<>();
        dataObjectsList = new ArrayList<>();
        content = "field5";

        displayList =findViewById(R.id.display_chapters);
        final BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(getmNavigate());

        if (findViewById(R.id.bible_frag) != null){
            twoPane = true;
        }

        if (savedInstanceState == null){

            if(getIntent() != null && getIntent().hasExtra("Book")){
                book = getIntent().getStringExtra("Book");
                if (!twoPane){
                    chapter = getIntent().getIntExtra("Chapter", 1);
                } else {
                    chapter = 1;
                }
                database_toUse = getIntent().getStringExtra("DATABASE_TO_USE");
                number_of_chapters = getIntent().getIntExtra("Number of Chapters", 0);
                verse = 1;
                Bundle b = new Bundle();
                b.putInt("Number of Chapters", number_of_chapters);

                if (twoPane){
                    BibleFragment bibleFragment = new BibleFragment();
                    bibleFragment.setArguments(b);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.bible_frag, bibleFragment).commit();
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Chapter", chapter);
                editor.putString("Book", book);
                editor.putString("DATATOUSE", database_toUse);
                editor.putInt("Number of Chapters", number_of_chapters);
                editor.apply();
            }

            if (Intent.ACTION_SEARCH.equals(getIntent().getAction())){
                handleIntent(getIntent());
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                loadData(content, book, chapter, verse, "");
            }
        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener getmNavigate(){
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_left:
                        //
                        if (chapter == 1){
                            Toast.makeText(DisplayActivity.this, "Move to the next chapter", Toast.LENGTH_SHORT).show();
                        } else {
                            chapter -=1;
                            loadData(content, book, chapter, verse, "");
                        }
                        return true;

                    case R.id.navigation_home:
                        //
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        return true;

                    case R.id.navigation_right:
                        //
                        if (chapter == number_of_chapters){
                            Toast.makeText(DisplayActivity.this, "Move to the previous chapter", Toast.LENGTH_SHORT).show();
                        } else {
                            chapter +=1;
                            loadData(content, book, chapter, verse, "");
                        }
                        return true;
                }
                return false;
            }
        };
    }

    private void loadData(String content, String book, int chapter, int verse, String query) {
        if (dataObjectArrayList.size() >= 1){
            dataObjectArrayList.clear();
            dataObjectArrayList = new ArrayList<>();
        }
        que = query;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            public void run() {
                if (database_toUse.equals("OldTestament.db")){
                    dbName = "OldTestament.db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    dataObjectArrayList = db.test2(content, book, chapter, verse, query);
                    db.close();
                } else if (database_toUse.equals("NewTestament.db")){
                    dbName = "NewTestament.db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    dataObjectArrayList = db.test2(content, book, chapter, verse, query);
                    db.close();

                } else {
                    dbName = book + ".db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    dataObjectArrayList = db.queryFromDb_byBooks(chapter, query);
                    db.close();
                }
            }
        });

        try {
            Thread.sleep(850);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        displayAdapter = new DisplayAdapter(this, dataObjectArrayList, content);
        displayList.setAdapter(displayAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            //String qs = "'%" + query_string + "%'";
            //String query = "'%" + intent.getStringExtra(SearchManager.QUERY) + "%'";
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Use the query to search your data somehow
            Log.i("SEARCH_QUERY", query);

            content = "field5";
            book = sharedPreferences.getString("Book", "Genesis.db");
            chapter = sharedPreferences.getInt("Chapter", 0);
            database_toUse = sharedPreferences.getString("DATATOUSE", book + ".db");
            verse = 1;

            loadData(content, book, chapter, verse, query);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            Set<String> a=new HashSet<>();
            a.add("male");//here you can give male if you want to select male voice.
            Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),
                    400,200,true,a);
            textToSpeech.setPitch(0.8f);
            textToSpeech.setVoice(v);
            textToSpeech.setSpeechRate(0.8f);
        }

        String sv = textToSpeech.getVoice().getName();
        Log.i("TEXT_TO_SPEECH", sv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onStepListSelected(int chapter) {
        this.chapter = chapter;
        loadData(content, book, chapter, verse, "");
    }
}
