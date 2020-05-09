package com.example.holybiblenative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    public static ListView displayList;
    public static DisplayAdapter displayAdapter;
    public static ArrayList<DataObject> dataObjectsList;
    public ArrayList<DataObject> dataObjectArrayList;
    private DataObject dataObject;
    public static String content;
    public static String book;
    public static String que;
    private String dbName, database_toUse;
    public static int chapter;
    public static int verse;

    private SharedPreferences sharedPreferences;
    private DataViewModel dataViewModel;

    private static String SHARED_PREFERENCE_NAME = "SEARCH";

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

    }

//
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        dataObjectArrayList = savedInstanceState.getParcelableArrayList("LIST");
        displayAdapter = new DisplayAdapter(getApplicationContext(), dataObjectArrayList);
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

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        dataObject = new DataObject();
        dataObjectArrayList = new ArrayList<>();
        dataObjectsList = new ArrayList<>();
        content = "field5";

        displayList =findViewById(R.id.display_chapters);

        if (savedInstanceState == null){

            if(getIntent() != null && getIntent().hasExtra("Book")){
                book = getIntent().getStringExtra("Book");
                chapter = getIntent().getIntExtra("Chapter", 1);
                database_toUse = getIntent().getStringExtra("DATABASE_TO_USE");
                verse = 1;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Chapter", chapter);
                editor.putString("Book", book);
                editor.apply();
            }


            //dbName = book + ".db";

            if (Intent.ACTION_SEARCH.equals(getIntent().getAction())){
                handleIntent(getIntent());
            } else {
                //loadObservableData();
                loadData(content, book, chapter, verse, "");
            }
        }
    }

//    private void loadObservableData() {
//        dataViewModel.getData().observe(this, new Observer<ArrayList<DataObject>>() {
//            @Override
//            public void onChanged(@Nullable final ArrayList<DataObject> dat) {
//
//                if (dat != null){
//                    dataObjectsList = dat;
//                    String s = dat.get(0).getContent();
//                    Log.i("ONCHANGED", s);
//
//                    displayAdapter = new DisplayAdapter(getApplicationContext(), dat);
//                    displayList =findViewById(R.id.display_chapters);
//                    displayList.setAdapter(displayAdapter);
//                }
//                //displayAdapter.notifyDataSetChanged();
//                displayAdapter.setData(dataObjectsList);
//            }
//        });
//    }

    public static void bindView(Context context, ArrayList<DataObject> dataObjectsList){
        displayAdapter = new DisplayAdapter(context, dataObjectsList);
        //displayList =findViewById(R.id.display_chapters);
        displayList.setAdapter(displayAdapter);
        displayAdapter.setData(dataObjectsList);
    }

    private void loadData(String content, String book, int chapter, int verse, String query) {
        if (dataObjectArrayList.size() >= 1){
            dataObjectArrayList.clear();
            dataObjectArrayList = new ArrayList<>();
        }
        que = query;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (database_toUse.equals("OldTestament.db")){
                    dbName = "OldTestament.db";
                } else if (database_toUse.equals("NewTestament.db")){
                    dbName = "NewTestament.db";

                } else {
                    dbName = book + ".db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    dataObjectArrayList = db.queryFromDb_byBooks(chapter);
                    db.close();
                }

                Database db = new Database(getApplicationContext(), dbName);
                db.open();

                //dataObjectArrayList = db.test2(content, book, chapter, verse, query);
                dataObjectArrayList = db.queryFromDb_byBooks(chapter);
                db.close();
            }
        });

        try {
            Thread.sleep(850);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        displayAdapter = new DisplayAdapter(this, dataObjectArrayList);
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
            verse = 1;

            loadData(content, book, chapter, verse, query);
        }
    }
}
