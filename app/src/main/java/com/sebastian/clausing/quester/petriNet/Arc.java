package com.sebastian.clausing.quester.petriNet;

/**
 * Eine Kante geht von einer Stelle zu einer Transition oder umgekehrt.
 * Das wird �ber die Konstruktoren abgebildet.
 * 
 * @author rmetzler
 */
public class Arc
extends PetrinetObject {

    Place place;
    Transition transition;
    Direction direction;
    int weight = 1;
    
    enum Direction {
        
        /**
         * Die 2 Richtungen, die so eine Kante haben darf
         */
        
        PLACE_TO_TRANSITION {

            @Override
            public boolean canFire(Place p, int weight) {
                return p.hasAtLeastTokens(weight);
            }

            @Override
            public void fire(Place p, int weight) {
                p.removeTokens(weight);
            }

            public String getOrientation() {
                return "PLACE_TO_TRANSITION";
            }



        },
        
        TRANSITION_TO_PLACE {
            @Override
            public boolean canFire(Place p, int weight) {
                return ! p.maxTokensReached(weight);
            }

            @Override
            public void fire(Place p, int weight) {
                p.addTokens(weight);
            }

            public String getOrientation() {
                return "TRANSITION_TO_PLACE";
            }


        };

        public abstract boolean canFire(Place p, int weight);

        public abstract void fire(Place p, int weight);

        public abstract String getOrientation();

    }
    
    public Arc(String name, Direction d, Place p, Transition t) {
        super(name);
        this.direction = d;
        this.place = p;
        this.transition = t;
    }

    public Arc(String name, Place p, Transition t) {
        this(name, Direction.PLACE_TO_TRANSITION, p, t);
        t.addIncoming(this);
    }

    public Arc(String name, Transition t, Place p) {
        this(name, Direction.TRANSITION_TO_PLACE, p, t);
        t.addOutgoing(this);
    }

    public boolean canFire() {
        return direction.canFire(place, weight);
    }
    
    public void fire() {
        this.direction.fire(place, this.weight);
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return weight;
    }

    public Direction getDirection(){
        return direction;
    }

    public String getOrientation(){
        return direction.getOrientation();
    }

    public Transition getTransition(){
        return transition;
    }

    public Place getPlace(){
        return place;
    }

}
