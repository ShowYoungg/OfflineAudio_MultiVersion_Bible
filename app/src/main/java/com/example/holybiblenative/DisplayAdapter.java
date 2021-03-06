package com.example.holybiblenative;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.example.holybiblenative.MainActivity.savedVerses;


public class DisplayAdapter extends ArrayAdapter<DataObject> implements TextToSpeech.OnInitListener, Filterable {
    private ArrayList<DataObject> dataObjects;
    private ArrayList<DataObject> dataObjects2;
    private String[] strings;
    private String contents;
    private boolean positionStatus = false;
    private boolean bottom = false;
    private TextToSpeech textToSpeech;
    private AppDatabase mDb;
    private Context context;

    private ArrayList<Boolean> sa;


    public DisplayAdapter(Context context, ArrayList<DataObject> keys, String contents) {
        super(context, 0, keys);
        this.context = context;
        this.contents = contents;
        mDb = AppDatabase.getInstance(context);

        dataObjects = keys;
        if (dataObjects != null){
            dataObjects2 = new ArrayList<>(dataObjects);
        }
    }

    public boolean onBottomReached(){
        return bottom;
    }

    public boolean isDisplayed(){
        return positionStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final DataObject k = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_list, parent, false);
        }

        if (position == 1) positionStatus = true;
        if (position == dataObjects.size() - 1) bottom = true;

        final TextView title = convertView.findViewById(R.id.chapter_verse);
        final TextView delete = convertView.findViewById(R.id.delete);
        final TextView content = convertView.findViewById(R.id.display_content);
        final ImageView audio = convertView.findViewById(R.id.audio1);
        final ImageView saveVerse = convertView.findViewById(R.id.save_verse);
        String text;

        textToSpeech = new TextToSpeech(getContext(), this, "com.google.android.tts");
        textToSpeech.setLanguage(Locale.ENGLISH);

        if (k != null){

            if (savedVerses != null){
                for (DataObject d: savedVerses) {
                    if (d.getBooks().equals(k.getBooks()) && d.getChapter()==k.getChapter() && d.getVerse() == k.getVerse()){
                        //saveVerse.setImageResource(R.drawable.ic_sd_storage_red_24dp);
                        dataObjects.get(position).setSelected(true);
                    }
                }
            }

            title.setText(k.getBooks() + " " + k.getChapter() + ":" + k.getVerse());

            if(k.getContent().contains("/")){
                strings = new String[9];
                strings = (k.getContent()).split("/");
                switch (contents){

                    case "field6":
                        text = strings[1];
                        //content.setText(strings[1]);
                        break;

                    case "field7":
                        text = strings[2];
                        //content.setText(strings[2]);
                        break;

                    case "field8":
                        text = strings[3];
                        //content.setText(strings[3]);
                        break;

                    case "field9":
                        text = strings[4];
                        //content.setText(strings[4]);
                        break;

                    case "field10":
                        text = strings[5];
                        //content.setText(strings[5]);
                        break;

                    case "field11":
                        text = strings[6];
                        //content.setText(strings[6]);
                        break;

                    case "field12":
                        text = strings[7];
                        //content.setText(strings[7]);
                        break;

                    case "field13":
                        text = strings[8];
                        //content.setText(strings[8]);
                        break;

                    case "field14":
                        text = strings[9];
                        //content.setText(strings[9]);
                        break;

                    default:
                        text = strings[0];
                        break;
                }
            } else {
                text = k.getContent();
                if (contents.equals("Saved Verses")){
                    saveVerse.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                }
            }

            content.setText(text);

            if (k.isSelected()){
                saveVerse.setImageResource(R.drawable.ic_sd_storage_red_24dp);
            } else {
                saveVerse.setImageResource(R.drawable.ic_sd_storage_black_24dp);
            }

            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                }
            });

            saveVerse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataObjects.get(position).isSelected()){
                        DataObject d = new DataObject(k.getId(), k.getBooks(), k.getChapter(), k.getVerse(), text, false);
                        deleteAlert(d, position, mDb, v, saveVerse);

                        for (int i = 0; i<= savedVerses.size()-1; i++) {
                            DataObject ds = savedVerses.get(i);
                            if (ds.getBooks().equals(k.getBooks()) && ds.getChapter() == k.getChapter()
                                    && ds.getVerse() == k.getVerse()){
                                savedVerses.remove(i--);
                            }
                        }
                    } else {
                        saveVerse.setImageResource(R.drawable.ic_sd_storage_red_24dp);
                        dataObjects.get(position).setSelected(true);
                        savedVerses.add(dataObjects.get(position));
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            public void run() {
                                DataObject d = new DataObject(k.getId(), k.getBooks(), k.getChapter(), k.getVerse(), text, true);
                                mDb.dataDao().insertData(d);
                            }
                        });
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                DataObject d = new DataObject(k.getId(), k.getBooks(), k.getChapter(), k.getVerse(), text, false);
                @Override
                public void onClick(View v) {
                    deleteAlert(d, -1, mDb, v, saveVerse);
                }
            });
        }

        notifyDataSetChanged();
        return convertView;
    }

    public void setData(ArrayList<DataObject> dataObjects){
        this.dataObjects = dataObjects;
        notifyDataSetChanged();
    }

    public void getTTS(){
        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

    }

    public ArrayList<DataObject> getDataObjects(){
        return dataObjects;
    }

    public void deleteAlert(DataObject d, int position, AppDatabase mDb, View view, ImageView saveVerse){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setMessage("Do you want to delete this verse?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (saveVerse != null && position != -1){
                            saveVerse.setImageResource(R.drawable.ic_sd_storage_black_24dp);
                            dataObjects.get(position).setSelected(false);
                        }
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            public void run() {
                                mDb.dataDao().deleteData(d);

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete Verse");
        alertDialog.show();
    }
    

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS){
            Set<String> a=new HashSet<>();
            a.add("male");//here you can give male if you want to select male voice.
            Voice v=new Voice("en-us-x-sfg#male_1-local",new Locale("en","US"),
                    400,200,true,a);
            textToSpeech.setPitch(0.8f);
            textToSpeech.setVoice(v);
            textToSpeech.setSpeechRate(0.8f);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DataObject> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataObjects2);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DataObject t : dataObjects2) {
                    if (t.getContent().toLowerCase().contains(filterPattern)) {
                        filteredList.add(t);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataObjects.clear();
            dataObjects.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
