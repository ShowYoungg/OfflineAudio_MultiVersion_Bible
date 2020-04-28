package com.example.holybiblenative;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Created by SHOW on 8/24/2018.
 */

@Dao
public interface DataDao {

    @Query("SELECT * FROM Bible ORDER BY id")
    LiveData<List<DataObject>> loadAllData();

    @Query("SELECT * FROM Bible WHERE id IN (:listOfIds)")
    LiveData<List<DataObject>> loadAllData(int[] listOfIds);

    @Insert
    void insertData(DataObject dataObject);

    @Delete
    void deleteData(DataObject dataObject);

    @Query("SELECT * FROM Bible WHERE id= :id")
    DataObject  loadById(int id);

    @Query("SELECT * FROM Bible WHERE books= :name")
    DataObject loadByName(String name);
}
