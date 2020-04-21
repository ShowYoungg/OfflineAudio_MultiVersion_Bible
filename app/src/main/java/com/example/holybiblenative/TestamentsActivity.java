package com.example.holybiblenative;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestamentsActivity extends AppCompatActivity {

    int position;
    String[] books;
    String[] all = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua ", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
            "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms ",
            "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
            "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum",
            "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark ",
            "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians",
            "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians",
            "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter",
            "1 John", "2 John", "3 John", "Jude", "Revelation"};

    Integer[] oldChapters = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,
            12,8,66,52,5,48,12,14,3,9,1,4,7,3,3,3,2,14,4};
    Integer[] newChapters = {28,16,24,21,28,16,16,13,6,6,4,4,5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};

    String[] oldTestament = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua ", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
            "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms ",
            "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
            "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum",
            "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi"};

    String[] newTestament = {"Matthew", "Mark ", "Luke", "John", "Acts", "Romans", "1 Corinthians",
            "2 Corinthians", "Galatians","Ephesians", "Philippians", "Colossians", "1 Thessalonians",
            "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James",
            "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testaments);

        //Intent i = getIntent();
        if (getIntent() != null){
            position = getIntent().getIntExtra("Position", 0);
            if (position == 0){
                books = all;
            } else if (position == 1){
                books = oldTestament;
            } else {
                books = newTestament;
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, books );
        ListView listView = findViewById(R.id.books_list2);
        listView.setAdapter(adapter);
    }
}
