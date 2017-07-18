package com.sebastian.clausing.quester.game;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sebastian.clausing.quester.helper.DataBaseHelper;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class NPC extends GameObject {

    private boolean adult;
    private boolean male;
    private int home;
    private boolean captured = false;


    public NPC(int prmID, String prmName, int prmAdult, int prmMale, int prmX, int prmY, int prmHome) {
        super(prmID, prmName, prmX, prmY);

        if (prmAdult == 1) {
            adult = true;
        } else {
            adult = false;
        }

        if (prmMale == 1) {
            male = true;
        } else {
            male = false;
        }

        this.home = prmHome;
    }

    public NPC() {

    }

    public int getHomeINT() {
        return home;
    }

    public String getHomeSTRING() {

        DataBaseHelper myDBHelper = new DataBaseHelper();
        SQLiteDatabase questerDB = myDBHelper.getStaticDb();
        Cursor c = questerDB.rawQuery("SELECT name FROM Locations WHERE _id = " + "'" + home + "'" + ";", null);
        c.moveToFirst();
        String home = c.getString(0);
        questerDB.close();

        return home;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean prmCaptured) {
        this.captured = prmCaptured;
    }

}
