package com.sebastian.clausing.quester.petriNet;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/rmetzler/simple-java-petrinet
 * @author rmetzler
 */

public class Petrinet
extends PetrinetObject {

    private static final String nl = "\n";
    List<Place> places              = new ArrayList<Place>();
    List<Transition> transitions    = new ArrayList<Transition>();
    List<Arc> arcs                  = new ArrayList<Arc>();

    public Petrinet(String name) {
        super(name);
    }

    public void add(PetrinetObject o) {
        if (o instanceof Arc) {
            arcs.add((Arc) o);
        } else if (o instanceof Place) {
            places.add((Place) o);
        } else if (o instanceof Transition) {
            transitions.add((Transition) o);
        }
    }
    
    public List<Transition> getTransitionsAbleToFire() {
        ArrayList<Transition> list = new ArrayList<Transition>();
        for (Transition t : transitions) {
            if (t.canFire()) {
                list.add(t);
            }
        }
        return list;
    }
    
    public Transition transition(String name) {
        Transition t = new Transition(name);
        transitions.add(t);
        return t;
    }
    
    public Place place(String name) {
        Place p = new Place(name);
        places.add(p);
        return p;
    }
    
    public Place place(String name, int initial) {
        Place p = new Place(name, initial);
        places.add(p);
        return p;
    }
    
    public Arc arc(String name, Place p, Transition t) {
        Arc arc = new Arc(name, p, t);
        arcs.add(arc);
        return arc;
    }
    
    public Arc arc(String name, Transition t, Place p) {
        Arc arc = new Arc(name, t, p);
        arcs.add(arc);
        return arc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Petrinet ");
        sb.append(super.toString()).append(nl);
        sb.append("---Transitions---").append(nl);
        for (Transition t : transitions) {
            sb.append(t).append(nl);
        }
        sb.append("---Places---").append(nl);
        for (Place p : places) {
            sb.append(p).append(nl);
        }
        return sb.toString();
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Arc> getArcs() {
        return arcs;
    }
}
