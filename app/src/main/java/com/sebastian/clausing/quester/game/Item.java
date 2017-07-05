package com.sebastian.clausing.quester.game;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class Item extends GameObject {

    private Itemtype type;

    private boolean receive;



    public Item(int prmID, String prmName, int prmType, int prmX, int prmY, ArrayList<Itemtype> prmItemTypeArray){
        super(prmID, prmName, prmX, prmY);

        //Log.d("GameLQ Item","prmName " + prmName);
       // Log.d("GameLQ Item","prmType  " + prmType);


        for (Itemtype i : prmItemTypeArray){
           // Log.d("GameLQ Item","For " + i.getName() + " ceck if i.id " +i.getId() + " equals " + prmType);
            if(i.getId() == prmType){
               // Log.d("GameLQ Item" , "   Set "+ prmName + " as type " + i.getName());
                this.type = i;
            }
        }
    }

    public Item(){

    }

    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public Itemtype getType() {
        return type;
    }

}
