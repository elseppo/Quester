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
    private ArrayList<Itemtype> arrItemtypeList = new ArrayList<>();
    private ArrayList<Location> arrLocationList = new ArrayList<>();
    private ArrayList<NPC> arrNPCList = new ArrayList<>();
    private ArrayList<Player> arrPlayerList = new ArrayList<>();


    public GameLogic(){
        //Setup game World

        questerDB = dbHelper.getStaticDb();


        //ItemType
        c = questerDB.rawQuery("SELECT * FROM Itemtypes;", null);
        c.moveToFirst();
        c.moveToPrevious();

        while(c.moveToNext()){
            Log.d("GameL","while Itemtype id " + c.getInt(0) );
            arrItemtypeList.add(new Itemtype(c.getInt(0),c.getString(1),c.getString(2)));
        }

        for (Itemtype t:arrItemtypeList){
            Log.d("GameLQ"," type: " + t.getName());
        }


        c = questerDB.rawQuery("SELECT * FROM Items;", null);
        c.moveToFirst();
        c.moveToPrevious();
        //Items
        while(c.moveToNext()){
            arrItemList.add(new Item(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3),c.getInt(4), arrItemtypeList));
        }

        ////////////////

        //NPCs
        c = questerDB.rawQuery("SELECT * FROM NPCs;", null);
        c.moveToFirst();
        c.moveToPrevious();

        while(c.moveToNext()){
            arrNPCList.add(new NPC(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getInt(6)));
        }

        ////////////////

        //Locations
        c = questerDB.rawQuery("SELECT * FROM Locations;", null);
        c.moveToFirst();
        c.moveToPrevious();

        while(c.moveToNext()){
            arrLocationList.add(new Location(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getInt(4),c.getInt(5)));
        }

        questerDB.close();

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

    public Item getItem(int prmItemuseID){
        //returns a random Item of specific itemuse type

        //Log.d("GameLQ" , "prmItemuseID " + prmItemuseID);

        ArrayList<Item> specificItems = new ArrayList<>();
        boolean item;

        for(Item i : arrItemList){
            item = false;
            //Log.d("GameLQ" , "ItemName " + i.getName() + " TypeObject @ " + i.getType());

            for(Integer in: i.getType().getItemusesList()){
                item = true;
            }
            if(item==true){
                specificItems.add(i);
            }

        }
        return specificItems.get((int) (Math.random()*specificItems.size()));
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
