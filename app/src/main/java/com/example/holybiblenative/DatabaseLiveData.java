package com.example.holybiblenative;

import android.content.Context;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

public class DatabaseLiveData extends LiveData<ArrayList<DataObject>> {

    private Context context;
    private String content, book, query = "";
    private int chapter, verse;
    private ArrayList<DataObject> dataObjects = new ArrayList<>();
    public DatabaseLiveData(Context context, String content, String book,
                            int chapter, int verse, String query){

        this.context = context;
        this.content = content;
        this.book = book;
        this.query = query;
        this.chapter = chapter;
        this.verse = verse;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Database db = new Database(context, "Esther.db.db");
                db.open();
                dataObjects = db.test2(content, book, chapter, verse, "");
                db.close();
            }
        });

        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //DisplayActivity.bindView(context,dataObjects);
    }
}
