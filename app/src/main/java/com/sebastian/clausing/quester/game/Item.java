package com.sebastian.clausing.quester.game;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class Item extends GameObject {

    private Itemtype type;

    private boolean receiveFromNPC = false;
    private boolean giveToNPC = false;


    public Item(int prmID, String prmName, int prmType, int prmX, int prmY, ArrayList<Itemtype> prmItemTypeArray){
        super(prmID, prmName, prmX, prmY);

        for (Itemtype i : prmItemTypeArray){
           // Log.d("GameLQ Item","For " + i.getName() + " ceck if i.id " +i.getId() + " equals " + prmType);
            if(i.getId() == prmType){
               // Log.d("GameLQ Item" , "   Set "+ prmName + " as type " + i.getName());
                this.type = i;
            }
        }

        Log.d("GameLQ Item","Created new ITEM: ---");
        Log.d("GameLQ Item","Name: " + this.getName() + " of Type " + type.getName());
        Log.d("GameLQ Item","Used for: ");

        for (Integer i :type.getItemusesList()) {
            Log.d("GameLQ Item" , "  "+ i);
        }

    }

    public Item(){

    }

    public boolean isReceiveFromNPC() {
        return receiveFromNPC;
    }

    public void setReceiveFromNPC(boolean receiveFromNPC) {
        this.receiveFromNPC = receiveFromNPC;
    }

    public Itemtype getType() {
        return type;
    }

    public void setGiveToNPC(boolean prmBoolean){
        giveToNPC = prmBoolean;
    }

    public boolean isGiveToNPC(){
        return giveToNPC;
    }

}
