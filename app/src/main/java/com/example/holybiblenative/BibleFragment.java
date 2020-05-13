package com.example.holybiblenative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BibleFragment extends Fragment {

    private DisplayAdapter displayAdapter;
    private ListView displayList;
    public String content;
    private String[] c;
    private int numbers_in_chapter;
    private DataObject dataObject;
    private ArrayList<DataObject> dataObjectArrayList;
    private ArrayList<DataObject> dataObjectsList;
    private static String SHARED_PREFERENCE_NAME = "SEARCH";
    private OnChapterClickListener mCallback;

    private TextToSpeech textToSpeech;
    private SharedPreferences sharedPreferences;

    public interface OnChapterClickListener{
        void onStepListSelected(int chapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnChapterClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onStepListClickListener");
        }
    }

    public BibleFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_chapters, container, false);

        if (getArguments() != null){
            numbers_in_chapter = getArguments().getInt("Number of Chapters");
        }

        c = new String[numbers_in_chapter];
        for (int i = 0; i<=numbers_in_chapter-1; i++){
            c[i] = "Chapter " + (i + 1);
        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.books_list_view, c );
        ListView listView = rootView.findViewById(R.id.books_list3);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onStepListSelected(position + 1);
            }
        });

        return rootView;
    }


}
