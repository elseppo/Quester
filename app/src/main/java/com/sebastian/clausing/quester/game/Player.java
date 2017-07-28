package com.sebastian.clausing.quester.game;

import android.util.Log;

import com.sebastian.clausing.quester.petriNet.Transition;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class Player extends GameObject {

    private int hitPoints;
    private int armorClass;
    private int movement;
    private int hitDie;
    private int reputation;

    //Represent the players knowledge
    private ArrayList<Item> arrItemList = new ArrayList<>();
    private ArrayList<Location> arrLocationList = new ArrayList<>();
    private ArrayList<NPC> arrNPCList = new ArrayList<>();
    private ArrayList<GameObject> arrGOList = new ArrayList<>();

    public Player(int prmInt, String prmString){
        setStartLocation();
    }

    private void setStartLocation(){
        setX((int)((Math.random()) * 36 + 1));
        setY((int)((Math.random()) * 32 + 1));
    }

    //Add Objects to the Players KN
    public void addGameObject(GameObject o) {
        if (o instanceof Item) {
            arrItemList.add((Item) o);

        } else if (o instanceof Location) {
            arrLocationList.add((Location) o);

        } else if (o instanceof NPC) {
            arrNPCList.add((NPC) o);
        }
    }

    public void updateKnowledge(Transition prmSplit, Transition prmMerge){


        for(GameObject o : prmSplit.getAction().getAllGameObjects()){

            boolean found = false;

            for(GameObject o2 : arrGOList){
                if(o == o2){
                    found = true;
                }
            }

            if(found == false){
                arrGOList.add(o);
                Log.d("Simulate" , "KDB: Split " + prmSplit.getName()+ " Merge " + prmMerge.getName() + " " + o.getName());
            }

        }

    }

    public void updateKnowledge(Location o){

        boolean found = false;

        for(GameObject o2 : arrGOList){
            if(o == o2){
                found = true;
            }
        }

        if(found == false){
            arrGOList.add(o);
        }
    }

    public boolean checkItems(Item i){

        boolean found = false;

        for(Item i2 : arrItemList){
            if(i == i2){
                found = true;
            }
        }
        return found;
    }

    public ArrayList<GameObject> getKN(){
        return arrGOList;
    }

    public boolean checkKN(GameObject prmGO){

        boolean found = false;

        for(GameObject o : arrGOList){
            if(prmGO == o){
                found = true;
            }
        }

        return found;
    }

}
