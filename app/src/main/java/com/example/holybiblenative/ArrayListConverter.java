package com.example.holybiblenative;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

/**
 * Created by SHOW on 8/24/2018.
 */

public class ArrayListConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<DataObject> stringToObjectList(String data){
        if (data ==null) {
            return null;
        }

        Type listType = new TypeToken<ArrayList<DataObject>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String objectListToString(ArrayList<DataObject> arrayList){
        return gson.toJson(arrayList);
    }
}
