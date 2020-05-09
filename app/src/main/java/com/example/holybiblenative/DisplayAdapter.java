package com.example.holybiblenative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.core.widget.TextViewCompat;

public class DisplayAdapter extends ArrayAdapter<DataObject> {
    private ArrayList<DataObject> dataObjects;
    private String[] strings;

    public DisplayAdapter(Context context, ArrayList<DataObject> keys) {
        super(context, 0, keys);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final DataObject k = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_list, parent, false);
        }

        final TextView title = convertView.findViewById(R.id.chapter_verse);
        final TextView content = convertView.findViewById(R.id.display_content);
        final TextView save = convertView.findViewById(R.id.save);


        if (k != null){
            title.setText(k.getBooks() + " " + k.getChapter() + ":" + k.getVerse());
            strings = (k.getContent()).split("/");
            //content.setText(k.getContent());
            content.setText(strings[0]);
        }

        notifyDataSetChanged();
        return convertView;
    }

    public void setData(ArrayList<DataObject> dataObjects){
        this.dataObjects = dataObjects;
        notifyDataSetChanged();
    }

    public ArrayList<DataObject> getDataObjects(){
        return dataObjects;
    }


}
