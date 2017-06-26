package com.sebastian.clausing.quester.questGen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sebastian.clausing.quester.helper.DataBaseHelper;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Action {

    private Action left;		//Is used, if action gets rewritten
    private Action right;		//The normal Action path goes always "right"

    private String actionName;

    private int actionPosition; // Position in the later quest
    private int actionID;       // ID of Action

    private boolean rewriteAction;

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    //Precondition
    //Postcondition

    //Konstruktor for Standard Action
    public Action(int prmPosition, int prmID){
        openDB();
        Cursor c;
        c = questerDB.rawQuery("SELECT name FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionName = c.getString(0);
        this.actionPosition = prmPosition;
        this.actionID = prmID;
        setRewriteAction(actionID);

        questerDB.close();
    }

    public Action(int prmPosition){
        this.actionName = "End Action";
        this.actionPosition = prmPosition;
    }


    private void setRewriteAction(int prmID){
        if (prmID == 1 || prmID == 10 || prmID == 11 || prmID == 16 || prmID == 20 || prmID == 21 || prmID == 22
                || prmID == 23 || prmID == 24 || prmID == 28) {
            rewriteAction = true;
        }
        else{
            rewriteAction = false;
        }
    }

    public String getActionName() {
        return actionName;
    }

    public int getActionID() {
        return actionID;
    }

    public boolean getRewriteAction() {
        return rewriteAction;
    }

    public void setPosition(int prmKey) {
        this.actionPosition = prmKey;
    }

    public int getPosition() {
        return actionPosition;
    }


    public void openDB(){
        questerDB = dbHelper.getStaticDb();
    }

}
