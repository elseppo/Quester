package com.sebastian.clausing.quester.screens;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sebastian.clausing.quester.helper.DataBaseHelper;
import com.sebastian.clausing.quester.R;

import java.io.IOException;

import static android.R.attr.value;

public class StartScreen extends AppCompatActivity {

    private SQLiteDatabase questerDB;
    private DataBaseHelper dbHelper = new DataBaseHelper();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        createDB();

        //Initialize Button Listener
        final Button btnStartQuest = (Button) findViewById(R.id.btn_New_Quest);
        btnStartQuest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startQuest();
            }
        });

    }

    private void startQuest(){

        Intent myIntent = new Intent(StartScreen.this, GameScreen.class);
        myIntent.putExtra("key", value); //Optional parameters
        StartScreen.this.startActivity(myIntent);

    }

    public void createDB(){

        try {
            dbHelper.createDatabaseIfNotExists(StartScreen.this);
        } catch (IOException e) {
            Log.d("StartScreen","DBHelper couldn't create the database!");
            e.printStackTrace();
        }

    }

    public void openDB(){

        questerDB = dbHelper.getStaticDb();

        Cursor c = questerDB.rawQuery("SELECT * FROM Actions", null);
        c.moveToFirst();
        Toast toast = Toast.makeText(StartScreen.this, "ID: " + c.getInt(0) + " | Name: " + c.getString(1), Toast.LENGTH_LONG);
        toast.show();
    }


}
