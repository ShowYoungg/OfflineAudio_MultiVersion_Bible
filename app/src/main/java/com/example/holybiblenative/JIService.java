package com.example.holybiblenative;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import static com.example.holybiblenative.ChaptersActivity.objects;

public class JIService extends JobIntentService {

    final Handler mHandler = new Handler();
    private static final String TAG = "JIService";
    private static final int JOB_ID = 2;
    //private static ArrayList<DataObject> objects;
    private String dbName;
    private String database_toUse;
    private String book;
    public int chapter;

    public int verse;



    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, JIService.class, JOB_ID, intent);
    }



    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if(intent != null && intent.hasExtra("Book")){
            book = intent.getStringExtra("Book");
            chapter = intent.getIntExtra("Chapter", 1);
            database_toUse = intent.getStringExtra("DATABASE_TO_USE");
        }

        loadData(chapter, database_toUse, book);
    }


    private ArrayList<DataObject> loadData(int chapter, String database_toUse, String books){
        objects = new ArrayList<>();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            public void run() {
                if (database_toUse.equals("OldTestament.db")){
                    dbName = "OldTestament.db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    objects = db.test2("field5", books, chapter, 1, "");
                    db.close();
                } else if (database_toUse.equals("NewTestament.db")){
                    dbName = "NewTestament.db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    objects = db.test2("field5", books, chapter, 1, "");
                    db.close();

                } else {
                    dbName = books + ".db";
                    Database db = new Database(getApplicationContext(), dbName);
                    db.open();
                    objects = db.queryFromDb_byBooks(chapter, "");
                    db.close();
                }
            }
        });
        return objects;
    }

}
