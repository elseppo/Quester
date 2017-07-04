package com.sebastian.clausing.quester.game;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sebastian.clausing.quester.helper.DataBaseHelper;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class GameLogic {

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;
    Cursor c;

    private ArrayList<Item> arrItemList = new ArrayList<>();
    private ArrayList<Location> arrLocationList = new ArrayList<>();
    private ArrayList<NPC> arrNPCList = new ArrayList<>();
    private ArrayList<Player> arrPlayerList = new ArrayList<>();


    public GameLogic(){

        //Setup game World

        // Items
        questerDB = dbHelper.getStaticDb();
        c = questerDB.rawQuery("SELECT * FROM Items;", null);
        c.moveToFirst();

        while(c.moveToNext()){
            arrItemList.add(new Item(c.getInt(0),c.getString(1)));
        }

        //NPCs
        c = questerDB.rawQuery("SELECT * FROM NPCs;", null);
        c.moveToFirst();

        while(c.moveToNext()){
            arrNPCList.add(new NPC(c.getInt(0),c.getString(1)));
        }

        //Locations
        c = questerDB.rawQuery("SELECT * FROM Locations;", null);
        c.moveToFirst();
        while(c.moveToNext()){
            arrLocationList.add(new Location(c.getInt(0),c.getString(1)));
        }

    }


    public void add(GameObject o) {
        if (o instanceof Item) {
            arrItemList.add((Item) o);
        } else if (o instanceof Location) {
            arrLocationList.add((Location) o);
        } else if (o instanceof NPC) {
            arrNPCList.add((NPC) o);
        } else if (o instanceof Player){
            arrPlayerList.add((Player) o);
        }
    }

    public Item getItem(){
        //returns a random Item
        return arrItemList.get((int) (Math.random()*arrItemList.size()));
    }

    public Location getLocation(){
        //returns a random Item
        return arrLocationList.get((int) (Math.random()*arrLocationList.size()));
    }

    public NPC getNPC(){
        //returns a random Item
        return arrNPCList.get((int) (Math.random()*arrNPCList.size()));
    }

    public Item getPlaceholder(){

        Item placeholder = arrItemList.get((int) (Math.random()*arrItemList.size()));
        placeholder.setName("Placeholder");
        return placeholder;
    }

}
