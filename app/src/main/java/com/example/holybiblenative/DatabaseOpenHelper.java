package com.example.holybiblenative;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static String DB_NAME;
    //public String DB_NAME = "TEN_IN_ONE.db";
    public static final String DB_SUB_PATH = "/databases/" + DB_NAME;
    private static String APP_DATA_PATH = "";
    private SQLiteDatabase dataBase;
    private final Context context;

//    public void setDbName(String dbName){
//        DB_NAME = dbName;
//    }

    public DatabaseOpenHelper(Context context, String dbName){
        super(context, dbName, null, 1);
        APP_DATA_PATH = context.getApplicationInfo().dataDir;
        this.context = context;
        this.DB_NAME = dbName;
    }

    public boolean openDataBase() throws SQLException{
        //String mPath = APP_DATA_PATH + DB_SUB_PATH;
        String mPath = context.getDatabasePath(DB_NAME).getAbsolutePath();
        //String mPath = context.getDatabasePath("Bible.db").getAbsolutePath();
        //Note that this method assumes that the db file is already copied in place
        dataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
        return dataBase != null;
    }

    @Override
    public synchronized void close(){
        if(dataBase != null) {dataBase.close();}
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}