package com.sebastian.clausing.quester.questGen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
    private String description;

    private int actionPosition; // Position in the later quest
    private int actionID;       // ID of Action

    private boolean rewriteAction;  //Determines, if the action can be rewritten
    private boolean isActionRewritten = false;  //Determines if the action has been rewritten

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    private ArrayList<GameObject> arrayListGO = new ArrayList<>();
    private ArrayList<Item> arrItemList = new ArrayList<>();
    private ArrayList<Location> arrLocationList = new ArrayList<>();
    private ArrayList<NPC> arrNPCList = new ArrayList<>();
    private ArrayList<Player> arrPlayerList = new ArrayList<>();

    //Sepcial Constructor for Action Give item to NPC.
    public Action(int prmPosition, int prmID, NPC prmCaptured , NPC prmNPC){
        openDB();
        Cursor c;

        // GET Action NAME
        c = questerDB.rawQuery("SELECT * FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionID = prmID;
        this.actionName = c.getString(1);
        this.actionPosition = prmPosition;
        this.keywords = c.getString(2);
        this.itemuse = c.getInt(3);
        setRewriteAction(actionID);

        questerDB.close();

        //Special variables for give ITEM
        prmCaptured.setCaptured(true);
        arrNPCList.add(prmNPC);
        arrNPCList.add(prmCaptured);

        //
        arrayListGO.add(prmNPC);
        arrayListGO.add(prmCaptured);
    }

    //Sepcial Constructor for Action Give item to NPC OR <STEAL> item FRom NPC.
    public Action(int prmPosition, int prmID, Item prmItemForNPC, NPC prmNPC){
        openDB();
        Cursor c;

        // GET Action NAME
        c = questerDB.rawQuery("SELECT * FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionID = prmID;
        this.actionName = c.getString(1);
        this.actionPosition = prmPosition;
        this.keywords = c.getString(2);
        this.itemuse = c.getInt(3);
        setRewriteAction(actionID);

        questerDB.close();

        //Special variables for give ITEM
        arrNPCList.add(prmNPC);
        arrItemList.add(prmItemForNPC);

        if(actionID == 9 || actionID == 31 ){
            prmItemForNPC.setGiveToNPC(true);
        }

        //
        arrayListGO.add(prmNPC);
        arrayListGO.add(prmItemForNPC);
    }

    //Sepcial Constructor for exchange // npc to exchange with, item for NPC, item to get
    public Action(int prmPosition, int prmID, NPC prmNPC, Item prmItemForNPC, Item prmItemForPlayer){
        openDB();
        Cursor c;

        // GET Action NAME
        c = questerDB.rawQuery("SELECT * FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionID = prmID;
        this.actionName = c.getString(1);
        this.actionPosition = prmPosition;
        this.keywords = c.getString(2);
        this.itemuse = c.getInt(3);
        setRewriteAction(actionID);

        questerDB.close();

        // SET Objects
        prmItemForPlayer.setReceiveFromNPC(true);
        prmItemForNPC.setGiveToNPC(true);

        arrNPCList.add(prmNPC);
        arrItemList.add(prmItemForPlayer); //MUST BE ON POSITION 0
        arrItemList.add(prmItemForNPC); //MUST BE ON POSITION 1

        //
        arrayListGO.add(prmNPC);
        arrayListGO.add(prmItemForPlayer); //MUST BE ON POSITION 0
        arrayListGO.add(prmItemForNPC); //MUST BE ON POSITION 1

    }

    //Construvtor for Action creation with additional gameobject
    public Action(int prmPosition, int prmID, GameObject o){
        openDB();
        Cursor c;

        // GET ACTION NAME
        c = questerDB.rawQuery("SELECT * FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionID = prmID;
        this.actionPosition = prmPosition;
        this.actionName = c.getString(1);
        this.keywords = c.getString(2);
        this.itemuse = c.getInt(3);
        questerDB.close();

        setRewriteAction(actionID);
        addGameObject(o);
    }

    //Constructor for upcounting Action with automatic rewrite setting due to ID
    public Action(int prmPosition, int prmID){
        openDB();
        Cursor c;
        c = questerDB.rawQuery("SELECT * FROM Actions WHERE _id = " + prmID + ";", null);
        c.moveToFirst();

        this.actionPosition = prmPosition;
        this.actionID = prmID;
        this.actionName = c.getString(1);
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

        arrayListGO.add(o);
    }

    private void setRewriteAction(int prmID){
        if (prmID == 1 || prmID == 10 || prmID == 11 || prmID == 16 || prmID == 20 || prmID == 21
                || prmID == 23 || prmID == 24 || prmID == 31) { // 28 Quest + 22 Subquest
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

    private String getKeyword(){

        String entries[] = keywords.split(",");

        String kw = entries[(int) (Math.random()*entries.length)];
        return kw;
    }

    public void setDescription() {

        Log.d("SetDescription", "Action: " + getActionID());
        Log.d("SetDescription", "Game Object: " + getGameObject().getName());

        String str = getKeyword();
        Log.d("SetDescription", "keyword " + str);


        NPC capturedNPC = null;
        NPC normalNPC = null;

        for (NPC npc : arrNPCList) {
            if (npc.isCaptured() == true) {
                capturedNPC = npc;
            } else {
                normalNPC = npc;
            }
        }

        // Action capture
        if ((actionID == 9 || actionID == 31) & arrItemList.size() == 0) {
            String part1;

            String entries[] = str.split("ITEM");
            Log.d("SetDescription", "E0 " + entries[0]);
            Log.d("SetDescription", "Capured NPC " + capturedNPC.getName());
            Log.d("SetDescription", "Normal NPC " + normalNPC.getName());

            Log.d("SetDescription", "E1" + entries[1]);
            part1 = entries[0] + capturedNPC.getName() + entries[1];

            String entries2[] = part1.split("NPC");
            description = entries2[0] + normalNPC.getName();

            Log.d("SetDescription", "part1" + part1);
            Log.d("SetDescription", "description: " + description);
        }
        //Actions: exchange, give, <give>
        else if (actionID == 5 || actionID == 9 || actionID == 31) {

            String part1;

            String entries[] = str.split("ITEM");
            Log.d("SetDescription", "E0 " + entries[0]);
            Log.d("SetDescription", "NPC " + arrNPCList.get(0).getName());
            Log.d("SetDescription", "Item " + arrItemList.get(0).getName());

            Log.d("SetDescription", "E1" + entries[1]);
            part1 = entries[0] + arrItemList.get(0).getName() + entries[1];

            String entries2[] = part1.split("NPC");
            Log.d("SetDescription", "E0 " + entries2[0]);
            Log.d("SetDescription", "NPC " + arrNPCList.get(0).getName());
            description = entries2[0] + arrNPCList.get(0).getName();

            Log.d("SetDescription", "part1" + part1);
            Log.d("SetDescription", "description: " + description);

        }
        // Special Case, if Action "use" is used to capture criminal
        else if (actionID == 19) {
            if (arrItemList.size() > 0 && arrNPCList.size() > 0) {
                description = " capture " + arrNPCList.get(0).getName() + " with " + arrItemList.get(0).getName();
            }
            description = str + " " + getGameObject().getName();
            Log.d("SetDescription", description);
        }
        // All other cases with the Term ITEM in
        else if (str.contains("ITEM")) {
            String entries[] = str.split("ITEM");
            Log.d("SetDescription", "E0 " + entries[0]);
            Log.d("SetDescription", "GO " + getGameObject().getName());
            Log.d("SetDescription", "E1" + entries[1]);

            description = entries[0] + getGameObject().getName() + entries[1];
            Log.d("SetDescription", description);

        }
        // All other cases, without the Term ITEM
        else {
            description = str + " " + getGameObject().getName();

            Log.d("SetDescription", description);
        }

        //String s1 = description.substring(0, 1).toUpperCase();
        //String CaPdescription = s1 + description.substring(1);
        //description = CaPdescription;

    }

    public String getDescription(){
        return description;
    }

    public Item getItem(Integer c){
        return arrItemList.get(c);
    }

    public NPC getNPC(Integer c){
        return arrNPCList.get(c);
    }

    public ArrayList<GameObject> getAllGameObjects(){
        return arrayListGO;
    }
}
