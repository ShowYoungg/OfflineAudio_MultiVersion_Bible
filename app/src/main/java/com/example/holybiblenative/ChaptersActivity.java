package com.example.holybiblenative;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChaptersActivity extends AppCompatActivity {

    int testament;
    int position;
    int numberOfChapters;
    String[] c;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        if (getIntent() != null){
            testament = getIntent().getIntExtra("Testament", 0);
            position = getIntent().getIntExtra("Position", 0);
            numberOfChapters = getIntent().getIntExtra("Number of Chapters", 0);

            c = new String[numberOfChapters];
            for (int i = 0; i<=numberOfChapters-1; i++){
                c[i] = "Chapter " + (i + 1);
            }

//            if (testament == 0){
////                chapters = allChapters;
//            } else if (testament == 1){
////                chapters = oldChapters;
//            } else {
////                chapters = newChapters;
//            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, c );
        ListView listView = findViewById(R.id.books_list3);
        listView.setAdapter(adapter);

    }
}
