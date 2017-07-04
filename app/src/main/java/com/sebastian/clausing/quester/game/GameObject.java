package com.sebastian.clausing.quester.game;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class GameObject {

    private String name;
    private Location location;
    private int id;

    public GameObject(){

    }

    public GameObject(int prmID, String prmName){

        this.id = prmID;
        this.name = prmName;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }
}
