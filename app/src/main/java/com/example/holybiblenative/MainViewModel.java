package com.example.holybiblenative;

import android.app.Application;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


/**
 * Created by SHOW on 9/4/2018.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<DataObject>> dataObj;
    private static ExecutorService executorService;
    //private static AppDatabase mDb;
    private static DatabaseCopier mDb;

    public MainViewModel(@NonNull Application application) {
        super(application);

        //mDb = AppDatabase.getInstance(this.getApplication());
        mDb = DatabaseCopier.getInstance(this.getApplication());
        //dataObj = mDb.dataDao().loadAllData();
        dataObj = AppDatabase.dataDao().loadAllData();
    }

    public LiveData<List<DataObject>> getDataObj() {
        return dataObj;
    }

    public static void deleteMovie(final DataObject data){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //smDb.dataDao().deleteData(data);
                AppDatabase.dataDao().deleteData(data);
            }
        });
    }
}
