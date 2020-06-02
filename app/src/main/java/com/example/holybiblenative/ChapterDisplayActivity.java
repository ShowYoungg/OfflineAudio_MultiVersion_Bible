package com.example.holybiblenative;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.example.holybiblenative.MainActivity.SHARED_PREFERENCE_NAME;
import static com.example.holybiblenative.MainActivity.content_field;

public class ChapterDisplayActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private int testament;
    private String books, database_toUse;
    private int numberOfChapters;
    private String[] c;
    private SharedPreferences sharedPreferences;
    //private String SHARED_PREFERENCE_NAME = "SEARCH";
    private String dbName;
    public static ArrayList<DataObject> objects;

    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private TextToSpeech textToSpeech;
    public ListView displayList;
    public DisplayAdapter displayAdapter;
    public ArrayList<DataObject> dataObjectsList;
    public ArrayList<DataObject> dataObjectArrayList;
    private DataObject dataObject;
    public String content;
    public String que;
    public int chapter;
    public int verse;
    private FrameLayout displayFrame;
    private FrameLayout chapterListFrame;
    private Menu mMenu;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.version_menus, menu);
        mMenu = menu;
        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        ImageView imageView = searchView.findViewById(androidx.appcompat.R.id.search_button);
        imageView.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        searchView.setQueryHint("Search by words");
        searchView.setSubmitButtonEnabled(false);

        //All menus are hidden when showing saved verses
        if (getIntent().hasExtra("Position") || findViewById(R.id.display_frame).getVisibility() != View.VISIBLE){
            for (int i = 0; i <= menu.size()-1; i++) {
                menu.getItem(i).setVisible(false);
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                displayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        dataObjectArrayList.clear();
        switch (id){
            case android.R.id.home:
                if (chapterListFrame.getVisibility() == View.VISIBLE){
                    if (id == android.R.id.home) {
                        NavUtils.navigateUpFromSameTask(this);
                    }
                    return super.onOptionsItemSelected(item);
                } else {
                    displayFrame.setVisibility(View.GONE);
                    chapterListFrame.setVisibility(View.VISIBLE);
                    //All menus are hidden when showing saved verses
                    if (findViewById(R.id.display_frame).getVisibility() != View.VISIBLE){
                        for (int i = 0; i <= mMenu.size()-1; i++) {
                            mMenu.getItem(i).setVisible(false);
                        }
                    }
                    return true;
                }

            case R.id.akjv:
                //
                content = "field13";
                loadData(content, books, chapter, verse,"");
                return true;

            case R.id.kjv:
                //
                content = "field5";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.bbe:
                //
                content = "field14";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.wbt:
                //
                content = "field10";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.ylt:
                //
                content = "field12";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.darby:
                //
                content = "field8";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.drb:
                //
                content = "field7";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.asv:
                //
                content = "field6";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.erv:
                //
                content = "field9";
                loadData(content, books, chapter, verse, "");
                return true;

            case R.id.web:
                //
                content = "field11";
                loadData(content, books, chapter, verse, "");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(getmNavigate());

        //This will lock orientation to portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        if (getIntent().hasExtra("Position")){
//            bottomNavigationView.setVisibility(View.GONE);
//
//            //This will lock orientation to portrait
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        displayList.setOnScrollListener(new AbsListView.OnScrollListener() {
            int lastScrollVisibleItem;
            int lastScrollItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && lastScrollVisibleItem == lastScrollItem ) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if(scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastScrollVisibleItem = view.getLastVisiblePosition();
                lastScrollItem = totalItemCount - 1;
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), this, "com.google.android.tts");
        textToSpeech.setLanguage(Locale.ENGLISH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_display);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        content_field = sharedPreferences.getString("VERSION", "");


        if (getIntent() != null){
            //testament = getIntent().getIntExtra("Testament", 0);
            database_toUse = getIntent().getStringExtra("DATABASE_TO_USE");
            books = getIntent().getStringExtra("Book");
            numberOfChapters = getIntent().getIntExtra("Number of Chapters", 0);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Book", books);
            editor.putString("DATATOUSE", database_toUse);
            editor.putInt("Number of Chapters", numberOfChapters);
            editor.apply();

            c = new String[numberOfChapters];
            for (int i = 0; i<=numberOfChapters-1; i++){
                c[i] = String.valueOf(i + 1);
            }
        }

        objects = new ArrayList<>();
        dataObject = new DataObject();
        dataObjectArrayList = new ArrayList<>();
        dataObjectsList = new ArrayList<>();

        if (content_field == null || content_field.equals("")){
            content = "field5";
        } else {
            content = content_field;
        }

        //sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        displayList = findViewById(R.id.display_chapters);
        progressBarLayout = findViewById(R.id.layout_progress_bar);
        progressBar = findViewById(R.id.progress_bar);
        displayFrame = findViewById(R.id.display_frame);
        chapterListFrame = findViewById(R.id.books_list3_frame);

//        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.chapter_list, c );
//        GridView listView = findViewById(R.id.books_list3);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, c );
        GridView listView = findViewById(R.id.books_list3);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chapterListFrame.setVisibility(View.GONE);
                displayFrame.setVisibility(View.VISIBLE);
                chapter = position + 1;
                loadData(content, books, chapter, verse, "");
                //All menus are enabled when showing saved verses
                if (findViewById(R.id.display_frame).getVisibility() == View.VISIBLE){
                    for (int i = 0; i <= mMenu.size()-1; i++) {
                        mMenu.getItem(i).setVisible(true);
                    }
                }
            }
        });

    }

    public BottomNavigationView.OnNavigationItemSelectedListener getmNavigate(){
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_left:
                        //
                        if (chapter == 1){
                            Toast.makeText(ChapterDisplayActivity.this, "Move to the next chapter", Toast.LENGTH_SHORT).show();
                        } else {
                            chapter -=1;
                            loadData(content, books, chapter, verse, "");
                        }
                        return true;

                    case R.id.navigation_home:
                        //
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //finish();
                        return true;

                    case R.id.navigation_right:
                        //
                        if (chapter == numberOfChapters){
                            Toast.makeText(ChapterDisplayActivity.this, "Move to the previous chapter", Toast.LENGTH_SHORT).show();
                        } else {
                            chapter +=1;
                            loadData(content, books, chapter, verse, "");
                        }
                        return true;
                }
                return false;
            }
        };
    }

    private void loadData(String content, String book, int chapter, int verse, String query) {
        //This method stops and shutdown Google TTS engine being played if any
        if (displayAdapter != null){
            displayAdapter.getTTS();
        }

        monitorProgressBar();

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
        displayAdapter = new DisplayAdapter(ChapterDisplayActivity.this, dataObjectArrayList, content);
        displayList.setAdapter(displayAdapter);
    }

    private void monitorProgressBar(){
        progressBarLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (displayAdapter.isDisplayed()){
                    progressBarLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    monitorProgressBar();
                }
            }
        }, 100);
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
//        String sv = textToSpeech.getVoice().getName();
//        Log.i("TEXT_TO_SPEECH", sv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null){
            textToSpeech.stop();
        }
        if (displayAdapter != null){
            displayAdapter.getTTS();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
        displayAdapter.getTTS();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
