package com.example.holybiblenative;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChaptersActivity extends AppCompatActivity {

    private int testament;
    private String books, database_toUse;
    private int numberOfChapters;
    private String[] c;
    private SharedPreferences sharedPreferences;
    private String SHARED_PREFERENCE_NAME = "SEARCH";

//    Integer[] chapters;
//    Integer[] allChapters = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,
//            12,8,66,52,5,48,12,14,3,9,1,4,7,3,3,3,2,14,4,28,16,24,21,28,16,16,13,6,6,4,4,
//            5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};
//
//    Integer[] oldChapters = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,
//            12,8,66,52,5,48,12,14,3,9,1,4,7,3,3,3,2,14,4};
//
//    Integer[] newChapters = {28,16,24,21,28,16,16,13,6,6,4,4,5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);

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
                c[i] = "Chapter " + (i + 1);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, c );
        ListView listView = findViewById(R.id.books_list3);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChaptersActivity.this,
                        DisplayActivity.class).putExtra("Book", books)
                        .putExtra("Chapter", position + 1)
                        .putExtra("DATABASE_TO_USE", database_toUse)
                        .putExtra("Number of Chapters", numberOfChapters);
                startActivity(intent);
            }
        });
    }
}
