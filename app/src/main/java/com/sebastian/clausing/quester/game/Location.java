package com.sebastian.clausing.quester.game;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class Location extends GameObject {

    private String type;
    private int size;

    public Location(int prmID, String prmName, String prmType, int prmX, int prmY, int prmSize){
        super(prmID, prmName, prmX, prmY);

        this.type = prmType;
        this.size = prmSize;

    }

    public Location(){

    }



}
