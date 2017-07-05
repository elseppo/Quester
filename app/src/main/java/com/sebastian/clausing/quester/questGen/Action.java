package com.sebastian.clausing.quester.questGen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sebastian.clausing.quester.game.GameObject;
import com.sebastian.clausing.quester.game.Item;
import com.sebastian.clausing.quester.game.Location;
import com.sebastian.clausing.quester.game.NPC;
import com.sebastian.clausing.quester.game.Player;
import com.sebastian.clausing.quester.helper.DataBaseHelper;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Action {

    private String actionName;
    private String keywords;
    private int itemuse;

    private int actionPosition; // Position in the later quest
    private int actionID;       // ID of Action

    private boolean rewriteAction;  //Determines, if the action can be rewritten
    private boolean isActionRewritten = false;  //Determines if the action has been rewritten

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    private ArrayList<Item> arrItemList = new ArrayList<>();
    private ArrayList<Location> arrLocationList = new ArrayList<>();
    private ArrayList<NPC> arrNPCList = new ArrayList<>();
    private ArrayList<Player> arrPlayerList = new ArrayList<>();


    //Sepcial Constructor for rewrite rule 13.
    public Action(int prmPosition, int prmID, NPC prmNPC, Item prmGiveItem, GameObject prmGetItem){
        openDB();
        Cursor c;

        // GET Action NAME
        c = questerDB.rawQuery("SELECT name FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionName = c.getString(0);
        this.actionPosition = prmPosition;
        this.actionID = prmID;
        setRewriteAction(actionID);

        questerDB.close();

        // SET Objects
        Item get = (Item) prmGetItem;
        Item give = prmGiveItem;
        get.setReceive(true);
        give.setReceive(false);

        arrNPCList.add(prmNPC);
        arrItemList.add(get);
        arrItemList.add(give);

    }

    //Construvtor for Action creation with additional gameobject
    public Action(int prmPosition, int prmID, GameObject o){
        openDB();
        Cursor c;

        // GET ACTION NAME
        c = questerDB.rawQuery("SELECT name FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionName = c.getString(0);
        this.actionPosition = prmPosition;
        this.actionID = prmID;
        setRewriteAction(actionID);
        questerDB.close();

        //GET GAME OBJECT
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

    //Constructor for upcounting Action with automatic rewrite setting due to ID
    public Action(int prmPosition, int prmID){
        openDB();
        Cursor c;
        c = questerDB.rawQuery("SELECT * FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionName = c.getString(1);
        this.actionPosition = prmPosition;
        this.actionID = prmID;
        this.keywords = c.getString(2);
        this.itemuse = c.getInt(3);

        setRewriteAction(actionID);

        questerDB.close();
    }

    //Konstruktor for Standard Action
    public Action(int prmPosition){
        this.actionName = "End Action";
        this.actionPosition = prmPosition;
    }

    //Objects an Action can contain
    public void addGameObject(GameObject o) {
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

    private void setRewriteAction(int prmID){
        if (prmID == 1 || prmID == 10 || prmID == 11 || prmID == 16 || prmID == 20 || prmID == 21
                || prmID == 23 || prmID == 24) { // 28 Quest + 22 Subquest
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

    public void setRewriteAction(boolean prmBln){
        this.rewriteAction = prmBln;
    }

    public boolean getIsActionRewritten(){
        return isActionRewritten;
    }

    public void setIsActionRewritten(boolean prmBln){
        this.isActionRewritten = prmBln;
    }

    public GameObject getGameObject(){

        if(arrItemList.size() > 0)
        {
            return arrItemList.get(0);

        }else if(arrLocationList.size() > 0)
        {
            return arrLocationList.get(0);

        }else if(arrNPCList.size()>0)
         {
             return arrNPCList.get(0);
         }

         return null;
    }

    public int getItemuse() {
        return itemuse;
    }

}
