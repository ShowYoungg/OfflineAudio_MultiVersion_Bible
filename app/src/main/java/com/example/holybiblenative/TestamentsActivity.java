package com.example.holybiblenative;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class TestamentsActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private ArrayList<DataObject> dl;
    private int testament;
    private String[] books;
    private Integer[] chapters;

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

    private Integer[] allChapters = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,
            12,8,66,52,5,48,12,14,3,9,1,4,7,3,3,3,2,14,4,28,16,24,21,28,16,16,13,6,6,4,4,
            5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};

    private Integer[] oldChapters = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,
            12,8,66,52,5,48,12,14,3,9,1,4,7,3,3,3,2,14,4};

    private Integer[] newChapters = {28,16,24,21,28,16,16,13,6,6,4,4,5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};

    private String[] oldTestament = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
            "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms",
            "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
            "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum",
            "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi"};

    private String[] newTestament = {"Matthew", "Mark", "Luke", "John", "Acts", "Romans",
            "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians",
            "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy",
            "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter",
            "1 John", "2 John", "3 John", "Jude", "Revelation"};

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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Use the query to search your data somehow
            Log.i("SEARCH_QUERY", "Weeee");
            if (query != null) {
                if (query.equals("")){
                    Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show();
                } else {
                    String[] ss = new String[books.length-1];
                    for (String s: books) {
                        if (s.equals(query) || s.startsWith(query)){
                            ss[0] = s;
                        }
                        books = ss;
                    }
                }
            }
        }
        //finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testaments);

        dl = new ArrayList<>();
        mDb = AppDatabase.getInstance(this);

        if (getIntent() != null){
            testament = getIntent().getIntExtra("Position", 0);
            if (testament == 0){
                books = all;
                chapters = allChapters;
            } else if (testament == 1){
                books = oldTestament;
                chapters = oldChapters;
            } else {
                books = newTestament;
                chapters = newChapters;
            }

            handleIntent(getIntent());
        }

        displayBooks();
    }

    private void displayBooks() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, books );
        ListView listView = findViewById(R.id.books_list2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //confirmDb(books[position]);
                Intent intent = new Intent(TestamentsActivity.this,
                        ChaptersActivity.class).putExtra("DATABASE_TO_USE", confirmDb(books[position]))
                        .putExtra("Position", books[position])
                        .putExtra("Number of Chapters", chapters[position]);
                startActivity(intent);
//                Toast.makeText(TestamentsActivity.this, books[position], Toast.LENGTH_SHORT).show();
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Database db = new Database(getApplicationContext(), "TEN_IN_ONE.db");
//                        db.open();
//                        dl = db.testing(books[position]);
//                        db.close();
//
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
////                        for (DataObject d: dl) {
////                            mDb.dataDao().insertData(d);
////                        }
//                        mDb.dataDao().insertAllData(dl);
//
//                    }
//                });

            }
        });
    }

    private String database_toUse;
    private String confirmDb(String book){
        String s = book;
        if (s.equals("Galatians") || s.equals("Mark") || s.equals("Ephesians") || s.equals("3 John")
            || s.equals("Philippians") || s.equals("Colossians") || s.equals("1 Thessalonians")
            || s.equals("2 Thessalonians")|| s.equals("Jude") || s.equals("1 Timothy")
            || s.equals("2 Timothy") || s.equals("Titus") || s.equals("Philemon")
            || s.equals("Hebrews") || s.equals("James") || s.equals("1 Peter")
            || s.equals("2 Peter") || s.equals("1 John") || s.equals("2 John")) {

            database_toUse = "NewTestament.db";
        } else if (s.equals("Ruth") || s.equals("Song of Solomon") || s.equals("Lamentations")
                || s.equals("Joel") || s.equals("Amos") || s.equals("Obadiah")
                || s.equals("Jonah") || s.equals("Micah") || s.equals("Nahum")
                || s.equals("Habakkuk") || s.equals("Zephaniah") || s.equals("Hosea")
                || s.equals("Haggai") || s.equals("Malachi") || s.equals("Joshua")){

            database_toUse = "OldTestament.db";
        } else {
            database_toUse = "Appropriate";
        }
        return database_toUse;
    }
}
