package com.example.holybiblenative;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Database {
    private String dbName;
    private final Context context;
    private SQLiteDatabase database;
    private DatabaseOpenHelper dbHelper;

    public Database(Context context, String dbName){
        this.context = context;
        this.dbName = dbName;

        dbHelper = new DatabaseOpenHelper(context, dbName);
    }

    public Database open() throws SQLException
    {
        //Toast.makeText(context, "Database Opened", Toast.LENGTH_SHORT).show();
        dbHelper.openDataBase();
        dbHelper.close();
        database = dbHelper.getReadableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public ArrayList<DataObject> queryFromDb_byBooks(int chpt, String query_string){
        ArrayList<DataObject> dList = new ArrayList<>();
        String query;
        if (query_string.equals("")){

            query ="SELECT * FROM Bible WHERE chapter= ?";
        } else {
            query ="SELECT * FROM Bible WHERE chapter= ? AND "
                    +  "content LIKE '%"+query_string+ "%'";
        }

        try{

            Cursor cursor = database.rawQuery(query,  new String[]{String.valueOf(chpt)});
            if (cursor.moveToFirst()){
                do {
                    DataObject dataObject = new DataObject();
                    int id = cursor.getInt(0);
                    String b = cursor.getString(1);
                    int chapter = cursor.getInt(2);
                    int verse = cursor.getInt(3);
                    String content = cursor.getString(4);
                    dataObject.setId(id);
                    dataObject.setBooks(b);
                    dataObject.setChapter(chapter);
                    dataObject.setVerse(verse);
                    dataObject.setContent(content);
                    dList.add(dataObject);
                    Log.i("DB_QUERY", dataObject.getContent());
//                    Log.i("DB_QUERY", sss[1] + "/" + sss.length);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            //handle
        }
        return dList;
    }

    public ArrayList<DataObject> testing(String book){
        ArrayList<DataObject> dataObjectArrayList = new ArrayList<>();
        try{
            String query ="SELECT * FROM Bible" +
                    " WHERE field2= ?";
            Cursor cursor = database.rawQuery(query,  new String[]{book});
            if (cursor.moveToFirst()){
                do{
                    DataObject dataObject = new DataObject();
                    int id = cursor.getInt(0);
                    String b = cursor.getString(1);
                    int chapter = cursor.getInt(2);
                    int verse = cursor.getInt(3);
                    String content1 = cursor.getString(4);
                    String content2 = cursor.getString(5);
                    String content3 = cursor.getString(6);
                    String content4 = cursor.getString(7);
                    String content5 = cursor.getString(8);
                    String content6 = cursor.getString(9);
                    String content7 = cursor.getString(10);
                    String content8 = cursor.getString(11);
                    String content9 = cursor.getString(12);
                    String content10 = cursor.getString(13);

                    String[] stl = new String[]{content1, content2, content3, content4,
                            content5, content6, content7, content8, content9, content10};

                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : stl) {
                        stringBuilder.append(s).append("/");
                    }
                    String content = stringBuilder.toString();
                    dataObject.setBooks(b);
                    dataObject.setChapter(chapter);
                    dataObject.setVerse(verse);
                    dataObject.setContent(content);
                    dataObject.setId(id);

                    dataObjectArrayList.add(dataObject);
                }while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            //handle
        }
        return dataObjectArrayList;
    }

    public ArrayList<DataObject> test2(String version_content, String book,
                                       int chapter, int verse, String query_string){
        ArrayList<DataObject> dataObjectArrayList = new ArrayList<>();
        //dataObjectArrayList.clear();
        try{
            String query;
            if (query_string.equals("")){

                query = "SELECT " + version_content + ", " +
                        "field2, field3, field4, field1 FROM Bible" +
                        " WHERE field2= ? AND field3= ? AND field4 >= ?";
            } else {
                query ="SELECT " + version_content + ", " +
                        "field2, field3, field4, field1 FROM Bible" +
                        " WHERE field2= ? AND field3= ? AND field4 >= ? AND"
                        + " " + version_content + " LIKE '%"+query_string+ "%'";
            }

            Cursor cursor = database.rawQuery(query,  new String[]{book,
                    String.valueOf(chapter), String.valueOf(verse)});

            if (cursor.moveToFirst()){
                do{
                    DataObject dataObject = new DataObject();
                    String value = cursor.getString(0);
                    String value1 = cursor.getString(1);
                    int value2 = cursor.getInt(2);
                    int value3 = cursor.getInt(3);
                    int value4 = cursor.getInt(4);
                    dataObject.setBooks(value1);
                    dataObject.setChapter(value2);
                    dataObject.setVerse(value3);
                    dataObject.setContent(value);
                    dataObject.setId(value4);
                    dataObjectArrayList.add(dataObject);

                    Log.d("DATABASE_QUERY2", value1 + " " +
                            value2 + ":" + value3 + " " + value);
                }while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            //handle
            e.printStackTrace();
        }
        return dataObjectArrayList;
    }
}