package com.example.holybiblenative;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


/**
 * Created by SHOW on 8/24/2018.
 */

@Database(entities = {DataObject.class}, version = 1, exportSchema = false)
//@TypeConverter(ArrayListConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "Bible";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
///////////////////////******************QUERIES SHOULD NEVER BE MADE ON MAIN THREAD************************************//////////////////
                        //.allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

//        static final Migration MIGRATION_1_2 = new Migration(1,2) {
//            @Override
//            public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//            }
//        };


    public abstract DataDao dataDao();

}

