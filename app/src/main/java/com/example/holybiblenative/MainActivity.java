package com.example.holybiblenative;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] oldNewAll = {"All Books", "Old Testament", "New Testament"};
    String welcomeWord = "For God so love the world that he gave his only Begotten Son" +
            " that whosoever believe in him shall have not perish but have everlasting life";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, welcomeWord, Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.books_list_view, oldNewAll );
        ListView listView = findViewById(R.id.books_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,
                        TestamentsActivity.class).putExtra("Position", position);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "id" +  String.valueOf(id) + " and "+ " position " +
//                        String.valueOf(position), Toast.LENGTH_LONG).show();
//                Log.i("INFOR", "id" +  String.valueOf(id) + " and "+ " position " +
//                        String.valueOf(position));
            }
        });
    }
}
