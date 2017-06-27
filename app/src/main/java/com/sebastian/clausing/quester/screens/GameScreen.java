package com.sebastian.clausing.quester.screens;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sebastian.clausing.quester.helper.DataBaseHelper;
import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.questGen.Quest;

public class GameScreen extends AppCompatActivity {

    private SQLiteDatabase questerDB;
    private DataBaseHelper dbHelper = new DataBaseHelper();
    private Quest quest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createQuest();

        //Initialize Button Listener
        final Button btnStartQuest = (Button) findViewById(R.id.btn_Start_Quest);
        btnStartQuest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                extendQuest();
            }
        });

    }



    public void openDB(){
        questerDB = dbHelper.getStaticDb();
        Cursor c = questerDB.rawQuery("SELECT Count(*) FROM Knowledge", null);
        c.moveToFirst();
        Toast toast = Toast.makeText(GameScreen.this, "#Rows: " + c.getInt(0), Toast.LENGTH_LONG);
        toast.show();
    }



    private void createQuest(){
        Log.d("GameScreen", "createQuest Start");


        quest = new Quest();
        String o = "";

        for(int c = 0; c < quest.getQuestList().size(); c++)
        {
            o = o + quest.getQuestList().get(c).getActionName() + " ";
        }
        TextView txtVQuestDescription = (TextView) findViewById(R.id.txtV_QuestDescription);
        txtVQuestDescription.setText(o);

        Log.d("GameScreen", "crateQuest Exit");
    }

    private void extendQuest(){

        quest.getQuestList().get(1);


    }





}
