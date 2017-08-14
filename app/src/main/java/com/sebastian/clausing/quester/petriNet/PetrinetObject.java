package com.sebastian.clausing.quester.petriNet;

/**
 * https://github.com/rmetzler/simple-java-petrinet
 * @author rmetzler
 */

public class PetrinetObject {

    private String name;
    
    public PetrinetObject(String name) {
        super();
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return /*getClass().getSimpleName() + " " + */ name;
    }
}
