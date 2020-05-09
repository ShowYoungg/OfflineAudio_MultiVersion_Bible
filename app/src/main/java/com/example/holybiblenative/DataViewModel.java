package com.example.holybiblenative;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import static com.example.holybiblenative.DisplayActivity.book;
import static com.example.holybiblenative.DisplayActivity.chapter;
import static com.example.holybiblenative.DisplayActivity.content;
import static com.example.holybiblenative.DisplayActivity.que;
import static com.example.holybiblenative.DisplayActivity.verse;

public class DataViewModel extends AndroidViewModel {

    private LiveData liveData;

    public DataViewModel(@NonNull Application application) {
        super(application);

        liveData = new DatabaseLiveData(application, content, book, chapter, verse, que);
    }

    public LiveData<ArrayList<DataObject>> getData(){
        return liveData;
    }

}
