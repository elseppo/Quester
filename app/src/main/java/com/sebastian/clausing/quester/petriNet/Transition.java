package com.sebastian.clausing.quester.petriNet;

import android.util.Log;

import com.sebastian.clausing.quester.questGen.Action;

import java.util.ArrayList;
import java.util.List;

public class Transition
extends PetrinetObject{

    private List<Arc> incoming = new ArrayList<Arc>();
    private List<Arc> outgoing = new ArrayList<Arc>();
    private Action action;
    private boolean split = false;
    private boolean merge = false;
    private int depth = 0;

    public Transition(String name) {
        super(name);
    }

    public Transition(String name, int prmDepth) {
        super(name);
        this.depth = prmDepth;
    }

    public void setAction(Action prmAction){
        this.action = prmAction;
    }

    public Action getAction(){
        return this.action;
    }


    /**
     * @return darf die Transition feuern?
     */
    public boolean canFire() {
        boolean canFire = true;
        
        // ich denke, dass auch eine Transition, 
        // die nur auf einer Seite Kanten hat, feuern darf
        canFire = ! this.isNotConnected();
        
        for (Arc arc : incoming) {
            canFire = canFire & arc.canFire();
        }
        
        for (Arc arc : outgoing) {
            canFire = canFire & arc.canFire();
        }
        return canFire;
    }
    
    /**
     * Transition soll feuern
     */
    public void fire() {

        Log.d("Transition","Fire: " + getName());


        for (Arc arc : incoming) {
            arc.fire();
        }
        
        for (Arc arc : outgoing) {
            arc.fire();
        }

    }
    
    /**
     * @param arc Eingehende Kante hinzuf�gen
     */
    public void addIncoming(Arc arc) {
        this.incoming.add(arc);
    }
    
    /**
     * @param arc ausgehende Kante hinzuf�gen
     */
    public void addOutgoing(Arc arc) {
        this.outgoing.add(arc);
    }

    /**
     * @return ist die Transition mit keiner Kante verbunden?
     */
    public boolean isNotConnected() {
        return incoming.isEmpty() && outgoing.isEmpty();
    }
    
    @Override
    public String toString() {
        return super.toString() + 
               (isNotConnected() ? " IS NOT CONNECTED" : "" ) +
               (canFire()? " READY TO FIRE" : "");
    }

    public void setSplit(boolean prmType){
        this.split = prmType;
    }

    public void setMerge(boolean prmType){
        this.merge = prmType;
    }

    public boolean isSplit(){
        return this.split;
    }

    public boolean isMerge(){
        return this.merge;
    }

    public int getDepth(){
        return this.depth;
    }

    public void setDepth(Integer prmDepth){
        this.depth = prmDepth;
    }

}
