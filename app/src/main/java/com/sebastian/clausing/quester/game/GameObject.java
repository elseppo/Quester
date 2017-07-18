package com.sebastian.clausing.quester.game;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class GameObject {

    private String name;
    private int id;
    private int x;
    private int y;

    public GameObject(){

    }



    public GameObject(int prmID, String prmName, int prmX, int prmY){

        this.id = prmID;
        this.name = prmName;

        this.x = prmX;
        this.y =prmY;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX( int prmX){
        this.x = prmX;
    }

    public void setY( int prmY){
        this.y = prmY;
    }
}
