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
    private Player player = new Player(0, "Player1");

    public GameLogic(){
        //Open DB
        questerDB = dbHelper.getStaticDb();

        //ItemTypes
        c = questerDB.rawQuery("SELECT * FROM Itemtypes;", null);
        c.move(-1);

        while(c.moveToNext()){
            Log.d("GameL","while Itemtype id " + c.getInt(0) );
            arrItemtypeList.add(new Itemtype(c.getInt(0),c.getString(1),c.getString(2)));
        }

        //Items
        c = questerDB.rawQuery("SELECT * FROM Items;", null);
        c.move(-1);

        //Items
        while(c.moveToNext()){
            arrItemList.add(new Item(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3),c.getInt(4), arrItemtypeList));
        }

        //NPCs
        c = questerDB.rawQuery("SELECT * FROM NPCs;", null);
        c.move(-1);

        while(c.moveToNext()){
            arrNPCList.add(new NPC(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getInt(6)));
        }

        //Locations
        c = questerDB.rawQuery("SELECT * FROM Locations;", null);
        c.move(-1);

        while(c.moveToNext()){
            arrLocationList.add(new Location(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getInt(4),c.getInt(5)));
        }

        //Close DB
        questerDB.close();

        //Set Players Start Location
        player.addGameObject(getLocation(player));
    }


    public void add(GameObject o) {
        if (o instanceof Item) {
            arrItemList.add((Item) o);
        } else if (o instanceof Location) {
            arrLocationList.add((Location) o);
        } else if (o instanceof NPC) {
            arrNPCList.add((NPC) o);
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

        boolean itemUseMatch;

        // Get Every Item and Check its Itemuses (stored in the Itemtype)
        for(Item i : arrItemList){
            itemUseMatch = false;

            // Get uses of the current item
            ArrayList<Integer> itemuses = i.getType().getItemusesList();

            //Check each entry in the uses list
            for(Integer in: itemuses){
                //If we have a match with the prmType, set a marker, that we want this item in the specififc itemlist
                if(in == prmItemuseID)
                itemUseMatch = true;
            }

            // If the marker is true, add the item to the specific list
            if(itemUseMatch==true){
                specificItems.add(i);
            }
        }


        Item rItem = specificItems.get((int) (Math.random()*specificItems.size()));
        Log.d("GameL", "Returned Item: " + rItem.getName() + "ITEMUSE: " + rItem.getType().getItemuses());

        return rItem;
    }

    public Location getLocation(){
        //returns a random Location
        return arrLocationList.get((int) (Math.random()*arrLocationList.size()));
    }

    public Location getLocation(GameObject prmGO){

        Log.d("Dist Test","On Object: " + prmGO.getName());

        int x1 = prmGO.getX();
        int y1 = prmGO.getY();
        int x2;
        int y2;
        double currentDistance;

        Log.d("Dist Test","x1: " + x1);
        Log.d("Dist Test","y1: " + y1);

        double nearestDistance = -1;
        Location nearestLocation = null;

        for (Location currentLocation: arrLocationList){

            x2 = currentLocation.getX();
            y2 = currentLocation.getY();

            //Log.d("Dist Test","On Location: " + currentLocation.getName());
            //Log.d("Dist Test","  x2: " + x2);
            //Log.d("Dist Test","  y2: " + y2);

            currentDistance = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));

            //Log.d("Dist Test","  currentDistance: " + currentDistance);

            if(nearestDistance == -1){
                //Log.d("Dist Test","First Iteration (nearestDistance = -1)");
                nearestDistance = currentDistance;
                nearestLocation = currentLocation;
            }

            else if (currentDistance < nearestDistance){
                //Log.d("Dist Test","Other Iteration (currentDist < nearestDist)");
                nearestDistance = currentDistance;
                nearestLocation = currentLocation;
            }

            //Log.d("Dist Test","  nearestDistance: " + nearestDistance);
            //Log.d("Dist Test","  nearestLocation: " + nearestLocation.getName());

        }

        //Log.d("Dist Test","  ABSOLUT nearestLocation: " + nearestLocation.getName());
        return nearestLocation;
    }

    public NPC getNPC(){
        //returns a random NPC
        return arrNPCList.get((int) (Math.random()*arrNPCList.size()));
    }

    public Item getPlaceholder(){

        Item placeholder = arrItemList.get((int) (Math.random()*arrItemList.size()));
        placeholder.setName("Placeholder");
        return placeholder;
    }

    public Player getPlayer(){
        return this.player;
    }

}
