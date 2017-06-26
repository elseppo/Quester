package com.sebastian.clausing.quester.helper;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Increment {

    private int count = -1;

    public int increment(){
        count ++;
        return count;
    }

    public int returnValue(){
        return count;
    }

}
