package com.sebastian.clausing.quester.game;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 05.07.2017.
 */

public class Itemtype {

    private int id;
    private String name;
    private String itemuses;
    private ArrayList<Integer> itemusesList = new ArrayList<>();

    public Itemtype(int prmID, String prmName, String prmUses){
        this.id = prmID;
        this.name = prmName;
        this.itemuses = prmUses;

        String entries[] = itemuses.split(",");
        for(String s : entries){
            itemusesList.add(Integer.valueOf(s));
            //Log.d("TEST",prmName + " "+s);
        }

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getItemuses() {
        return itemuses;
    }

    public ArrayList<Integer> getItemusesList(){
        return itemusesList;
    }
}
