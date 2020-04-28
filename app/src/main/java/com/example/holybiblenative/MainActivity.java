package com.example.holybiblenative;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private AppDatabase mDb;
    private DatabaseCopier mDb;
    private DataObject dataObject;
    private SharedPreferences sharedPreferences;
    private LinearLayout progressBar;
    private List<String> ll;
    private String[] words;

    private int databaseStatus;
    public static final String SHARED_PREFERENCE_NAME = "Database";

    private String[] oldNewAll = {"All Books", "Old Testament", "New Testament"};
    private String welcomeWord = "For God so love the world that he gave his only Begotten Son" +
            " that whosoever believe in him shall have not perish but have everlasting life";

    @Override
    protected void onStart() {
        super.onStart();


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (databaseStatus == 0){
                    progressBar.setVisibility(View.VISIBLE);

                    try{
                        //copyDatabase();
                        //readTextFile(ll);
                        Thread.sleep(3000);
                        //dataObject = mDb.dataDao().loadById(31000);
                        if (AppDatabase.dataDao() != null) {
                            dataObject= AppDatabase.dataDao().loadById(2);

                            Thread.sleep(3000);
                            Log.i("DATABASE_QUERY", dataObject.getContent() + " " + dataObject.getBooks());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(MainActivity.this, "Database already Created", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataObject = new DataObject();
        //mDb = AppDatabase.getInstance(getApplicationContext());
        mDb = DatabaseCopier.getInstance(getApplicationContext());

        progressBar = findViewById(R.id.progress_bar_layout);


        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        databaseStatus = sharedPreferences.getInt("DATABASE", 0);



        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, oldNewAll );
        ListView listView = findViewById(R.id.books_list);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,
                        TestamentsActivity.class).putExtra("Position", position);
                startActivity(intent);
            }
        });
    }

    public void readTextFile(List<String> l) throws IOException{
        String s = "";
        InputStream is = this.getResources().openRawResource(R.raw.bible);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while(true){
            try{
                if((s=reader.readLine()) == null) break;
            }catch (IOException e){
                e.printStackTrace();
            }
            joinString(s);
        }
        is.close();
    }

    private void joinString(String s){
        StringBuilder stringBuilder = new StringBuilder();
        words = s.split("\\s+");
        String[] stl = new String[3];
        if (words[0].equals("1") || words[0].equals("2") || words[0].equals("3")){
            stl[0] = words[0] + " " + words[1];
            stl[1] = words[2];
            Log.i("CCCC1", stl[0] + " /" + words[2]);

            for(int i = 3; i <= words.length-1; i++){
                stringBuilder.append(words[i]).append(" ");
            }
            stl[2] = stringBuilder.toString();
        } else if(words[0].equals("Song") && words[1].equals("of") ){
            stl[0] = words[0] + " " + words[1] + " " + words[2];
            stl[1] = words[3];

            Log.i("CCCC3", stl[0] + " /" + words[3]);

            for(int i = 4; i <= words.length-1; i++){
                stringBuilder.append(words[i]).append(" ");
            }
            stl[2] = stringBuilder.toString();

        } else {
            stl[0] = words[0];
            stl[1] = words[1];

            Log.i("CCCC2", stl[0] + " /" + words[1]);

            for(int i = 2; i <= words.length-1; i++){
                stringBuilder.append(words[i]).append(" ");
            }
            stl[2] = stringBuilder.toString();
        }


        dataObject.setBooks(stl[0]);
        dataObject.setContent(stl[2]);
        //stl[1] is written as string (Chapter:Verse) i.e 12:10; hence, the need for separation.
        String[] sg = stl[1].split(":");
        dataObject.setChapter(Integer.parseInt(sg[0]));
        dataObject.setVerse(Integer.parseInt(sg[1]));

        //mDb.dataDao().insertData(dataObject);
        AppDatabase.dataDao().insertData(dataObject);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("DATABASE", 1);
        editor.apply();
        
        Log.i("CHAPTERANDVERSE", sg[0] + " " + sg[1]);
        Log.i("LINEINFOR", stl[0] + " " + stl[1] + " " + stl[2]);
    }

    private void copyDatabase() throws IOException{
        InputStream inputStream = getApplicationContext().getAssets().open("Bible.db");
        //InputStream inputStream = this.getResources().openRawResource(R.raw.bible2);
        //private static String DB_NAME = bible.sqlite or bible.db
        //Hard coded DB_PATH is "data/data" + context.getPackageName() + "/databases/"
        String DB_PATH = getApplicationContext().getDatabasePath("Bible.db").getAbsolutePath();
        //String outputFile = DB_PATH + DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer))> 0){
            outputStream.write(buffer, 0, length);
        }

        Log.i("DATABASE_PATH", DB_PATH);

        outputStream.flush();
        outputStream.close();
        inputStream.close();
        progressBar.setVisibility(View.GONE);

    }
//    private boolean checkDatabase(){
//        File databaseFile = new File(DB_PATH + DATABASE_NAME);
//        return databaseFile.exists();
//    }
}
