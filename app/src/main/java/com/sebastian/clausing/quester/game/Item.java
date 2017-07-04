package com.sebastian.clausing.quester.game;

/**
 * Created by Sebs-Desktop on 03.07.2017.
 */

public class Item extends GameObject {

    private boolean receive;


    public Item(int prmInt, String prmString){
        super(prmInt, prmString);
    }

    public Item(){

    }

    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

}
