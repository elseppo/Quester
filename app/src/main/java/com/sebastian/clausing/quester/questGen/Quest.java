package com.sebastian.clausing.quester.questGen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.sebastian.clausing.quester.helper.DataBaseHelper;
import com.sebastian.clausing.quester.helper.Increment;
import com.sebastian.clausing.quester.petriNet.Arc;
import com.sebastian.clausing.quester.petriNet.Petrinet;
import com.sebastian.clausing.quester.petriNet.Place;
import com.sebastian.clausing.quester.petriNet.Transition;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Quest {

    // Static up-counting Action ID
    private Increment countA = new Increment();
    private Increment countP = new Increment();

    private ArrayList<Integer> sequenceOfActionsID = new ArrayList<Integer>();
    private ArrayList<Action> sequenceOfActions = new ArrayList<Action>();
    private ArrayList<Integer> appliedRulesID = new ArrayList<>();


    private int strategyID;
    private int motivationID;

    private String strategyName;
    private String motivationName;

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    private Petrinet quest = new Petrinet("Quest");

    public Quest(){
        Log.d("Quest", "----------> OpenDB");
        questerDB = dbHelper.getStaticDb();

        Log.d("Quest", "----------> Create Abstract Quest");
        createAbstractQuest();

        Log.d("Quest", "---------> Create Sequence of Actions");
        createSequenceofActions();

        Log.d("Quest", "---------> Close DB");
        questerDB.close();

        Log.d("Quest", "---------> Rewrite Actions");
        rewriteActions();

        Log.d("Quest", "---------> Print Lists");
        printLists();

        Log.d("Quest", "---------> Set Petri Net");
        setQuestPetriNet();

        Log.d("Quest", "---------> Test Petri Net");
        testPetri();

        Log.d("Quest", "---------> Exit Constructor");
    }

    private void createSequenceofActions(){

        for(int c = 0; c < sequenceOfActionsID.size(); c++){
            sequenceOfActions.add(new Action(countA.increment(),sequenceOfActionsID.get(c)));
        }

    }

    private void determineMotivation(){
        //Determine Motivation
        int totalMotivations = 9;
        motivationID=(int) (Math.random()* totalMotivations); // Possible Outcomes from 0 to (totalMotivations -1)
    }

    private void determineStrategy(String prmMotivation){
        Cursor c;
        int totalStrategies;

        motivationName = prmMotivation;

        // Determine how many Strategies the Motivation has
        c = questerDB.rawQuery("SELECT Count(*) FROM " + motivationName+";", null);
        c.moveToFirst();
        totalStrategies = c.getInt(0);

        //Determine random Strategy
        strategyID =  (int) (Math.random() * totalStrategies); // Possible Outcomes from 0 to (totalStrategies -1)

        //Set Strategy Name
        c = questerDB.rawQuery("SELECT strategy FROM " + "'"+motivationName +"'"+  "WHERE _id =  " +"'"+ strategyID +"';" , null);
        c.moveToFirst();
        strategyName = c.getString(0);
    }

    private void createAbstractQuest(){

        // Determine the Quests Motivation
        determineMotivation();

        // Determine Strategy According to Motivation ID
        switch (motivationID){
            case 0: //Knowledge

                determineStrategy("Knowledge");

                switch (strategyID){
                    case 0:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 1:
                        sequenceOfActionsID.add(16);
                        break;
                    case 2:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(12);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                    case 3:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(19);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                }
                break;
            case 1: //Comfort

                determineStrategy("Comfort");


                switch (strategyID){
                    case 0:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 1:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(2);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                }
                break;
            case 2: //Reputation

                determineStrategy("Reputation");


                switch (strategyID){
                    case 0:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 1:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(11);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                    case 2:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                }
                break;
            case 3: //Serenity

                determineStrategy("Serenity");


                switch (strategyID){
                    case 0:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(2);
                        break;
                    case 1:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(19);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 2:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(19);
                        sequenceOfActionsID.add(1);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 3:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(12);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                    case 4:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(18);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 5:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 6:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(2);
                        sequenceOfActionsID.add(4);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                }
                break;
            case 4: //Protection

                determineStrategy("Protection");


                switch(strategyID){
                    case 0:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(2);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(15);
                        break;
                    case 1:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(19);
                        break;
                    case 2:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(14);
                        break;
                    case 3:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(19);
                        break;
                    case 4:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(2);
                        break;
                    case 5:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(14);
                        break;
                    case 6:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(3);
                        break;
                }
                break;
            case 5: //Conquest

                determineStrategy("Conquest");


                switch (strategyID){
                    case 0:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(2);
                        break;
                    case 1:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(21);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                }
                break;
            case 6: //Wealth

                determineStrategy("Wealth");


                switch(strategyID){
                    case 0:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(20);
                        break;
                    case 1:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(21);
                        break;
                    case 2:
                        sequenceOfActionsID.add(14);
                        break;
                }
                break;
            case 7: //Ability

                determineStrategy("Ability");


                switch(strategyID){
                    case 0:
                        sequenceOfActionsID.add(14);
                        sequenceOfActionsID.add(19);
                        break;
                    case 1:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(19);
                        break;
                    case 2:
                        sequenceOfActionsID.add(19);
                        break;
                    case 3:
                        sequenceOfActionsID.add(2);
                        break;
                    case 4:
                        sequenceOfActionsID.add(19);
                        break;
                    case 5:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(19);
                        break;
                    case 6:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(6);
                        break;
                }
                break;
            case 8: //Equipment

                determineStrategy("Equipment");


                switch(strategyID){
                    case 0:
                        sequenceOfActionsID.add(14);
                        break;
                    case 1:
                        sequenceOfActionsID.add(20);
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(9);
                        break;
                    case 2:
                        sequenceOfActionsID.add(21);
                        break;
                    case 3:
                        sequenceOfActionsID.add(10);
                        sequenceOfActionsID.add(5);
                        break;
                }
                break;
        }

    }

    public ArrayList<Action> getSequenceOfActions(){
        //Log.d("Quest gSOA","Motivation = " + motivationName);
        //Log.d("Quest gSOA","Strategy = " + strategyName);
        return sequenceOfActions;
    }

    public ArrayList<Integer> getActionSequenceID(){

        return sequenceOfActionsID;
    }


    public void openDB(){
        questerDB = dbHelper.getStaticDb();
    }


    private void rewriteActions() {

        for(int c = 0; c < sequenceOfActions.size(); c++){

            Log.d("Quest_rewrite Actions: " , "-----> Apply Rules on Entry " + sequenceOfActions.get(c).getActionName());

            for(int z = 0; z < sequenceOfActions.size(); z++) {
                Log.d("Quest_rewrite Actions:" , " Action ID: " +sequenceOfActions.get(z).getActionID()+ " Action Name: " + sequenceOfActions.get(z).getActionName());

            }


            if(sequenceOfActions.get(c).getRewriteAction() == true){

                switch (sequenceOfActions.get(c).getActionID()) {
                    case 1: // <capture>
                        sequenceOfActions.remove(c);
                        sequenceOfActions.add(c, new Action(countA.increment() ,20));   //<get>
                        sequenceOfActions.add(c+1, new Action(countA.increment() ,10)); //<goto>
                        sequenceOfActions.add(c+2, new Action(countA.increment() ,24)); //capture
                        appliedRulesID.add(17);
                        Log.d("Quest_rewrite Actions: " ,"Rule 17: [<capture> --> <get>, <goto>, capture]");
                        break;

                    case 10: // <goto>
                        switch ((int) (Math.random() * 3)) {
                            case 0:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,25));   // goto
                                appliedRulesID.add(3);
                                Log.d("Quest_rewrite Actions: " ,"Rule 3: [<goto> --> goto]");
                                break;
                            case 1:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,7));    //explore
                                appliedRulesID.add(4);
                                Log.d("Quest_rewrite Actions: " ,"Rule 4: [<goto> --> explore]");
                                break;
                            case 2:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,23));   //<learn>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,25)); //goto
                                appliedRulesID.add(5);
                                Log.d("Quest_rewrite Actions: " ,"Rule 5: [<goto> --> <learn>, goto]");
                                break;
                        }
                        break;

                    case 11: // <kill>
                        sequenceOfActions.remove(c);
                        sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                        sequenceOfActions.add(c++, new Action(countA.increment() ,25)); //kill
                        appliedRulesID.add(18);
                        Log.d("Quest_rewrite Actions: " ,"Rule 18: [<kill> --> <goto>, kill]");
                        break;

                    case 16: // <spy>
                        sequenceOfActions.remove(c);
                        sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                        sequenceOfActions.add(c+1, new Action(countA.increment() ,27)); //spy
                        sequenceOfActions.add(c+2, new Action(countA.increment() ,10)); //<goto>
                        sequenceOfActions.add(c+3, new Action(countA.increment() ,15)); //report
                        appliedRulesID.add(16);
                        Log.d("Quest_rewrite Actions: " ,"Rule 16: [<spy> --> <goto>, spy, <goto>, report]");
                        break;

                    case 20: // <get>
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,29));   //get
                                Log.d("Quest_rewrite Actions: " ,"Rule 10: [<get> --> get]");
                                break;
                            case 1:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,21));   //<steal>
                                appliedRulesID.add(11);
                                Log.d("Quest_rewrite Actions: " ,"Rule 11: [<get> --> <steal>]");
                                break;
                            case 2:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,8));   //<gather>
                                appliedRulesID.add(12);
                                Log.d("Quest_rewrite Actions: " ,"Rule 12: [<get> --> <goto>, <gather>]");
                                break;
                            case 3:

                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,20)); //<get>
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,10)); //<goto>
                                sequenceOfActions.add(c+3, new Action(countA.increment() ,22)); //<subquest>
                                sequenceOfActions.add(c+4, new Action(countA.increment() ,5)); //exchange
                                appliedRulesID.add(13);
                                Log.d("Quest_rewrite Actions: " ,"Rule 13: [<get> --> <goto>, <get>, <goto>, <subquest>, exchange]");
                                break;
                        }
                        break;

                    case 21: // <steal>
                        switch ((int) (Math.random() * 2)) {
                            case 0:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,17)); //stealth
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,18)); //take
                                appliedRulesID.add(14);
                                Log.d("Quest_rewrite Actions: " ,"Rule 14: [<steal> --> <goto>, stealth, take]");
                                break;
                            case 1:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,11)); //<kill>
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,18)); //take
                                appliedRulesID.add(15);
                                Log.d("Quest_rewrite Actions: " ,"Rule 15: [<steal> --> <goto>, <kill>, take]");
                                break;
                        }
                        break;

                    case 22: // <subquest>
                        switch ((int) (Math.random() * 2)) {
                            case 0:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                appliedRulesID.add(1);
                                Log.d("Quest_rewrite Actions: " ,"Rule 1: [<subquest> --> <goto>]");
                                break;
                            case 1:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,28)); //<Quest>
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,10)); //<goto>
                                appliedRulesID.add(2);
                                Log.d("Quest_rewrite Actions: " ,"Rule 2: [<subquest> --> <goto>, <Quest>, <goto>]");
                                break;
                        }
                        break;

                    case 23: // <learn>
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,30));   //learn
                                Log.d("Quest_rewrite Actions: " ,"Rule 6: [<learn> --> learn]");
                                break;
                            case 1:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,22)); //<subquest>
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,12)); //listen
                                appliedRulesID.add(7);
                                Log.d("Quest_rewrite Actions: " ,"Rule 7: [<learn> --> <goto>, <subquest>, listen]");
                                break;
                            case 2:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,10));   //<goto>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,20)); //<get>
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,13)); //read
                                appliedRulesID.add(8);
                                Log.d("Quest_rewrite Actions: " ,"Rule 8: [<get> --> <goto>, <get>, read]");
                                break;
                            case 3:
                                sequenceOfActions.remove(c);
                                sequenceOfActions.add(c, new Action(countA.increment() ,20));   //<get>
                                sequenceOfActions.add(c+1, new Action(countA.increment() ,22)); //<subquest>
                                sequenceOfActions.add(c+2, new Action(countA.increment() ,9)); //give
                                sequenceOfActions.add(c+3, new Action(countA.increment() ,12)); //listen
                                appliedRulesID.add(9);
                                Log.d("Quest_rewrite Actions: " ,"Rule 9: [<get> --> <subquest>, give, listen]");
                                break;
                        }
                        break;

                    default:
                        Log.d("Quest_rewrite Actions: " ,"-----> No Change were Made");
                        break;
                }
            }
        }
    }

    private void printLists(){


        Log.d("Quest pL: " , "#Actions: " + sequenceOfActions.size());
        for(int r = 0; r < sequenceOfActions.size(); r++) {
            Log.d("Quest pL" , " Action ID: " +sequenceOfActions.get(r).getActionID()+ " Action Name: " + sequenceOfActions.get(r).getActionName());
        }

        Log.d("Quest pL: " , "#Used Rules: : " + appliedRulesID.size());
        for(int q = 0; q < appliedRulesID.size(); q++) {
            Log.d("Quest pL" , " Action ID: " +appliedRulesID.get(q));
        }

    }

    private String placeName(String prmName, int prmIncrement){
        String name;
        if (prmName.equals("Start") || prmName.equals("End"))
        {
            name = "P" + prmIncrement + " ("+ prmName +")";
        }
        else{
            name = "P" + prmIncrement + " ("+ prmName + " done)";
        }
        return name;
    }

    private String transitionName(String prmName,int prmIncrement){
        String name = "T" + prmIncrement + " ("+ prmName+")";
        return name;
    }

    private String arcName(int prmIncrement, String prmFrom, String prmTo){
        String name = "A" + prmIncrement + " [" + prmFrom + "] --> [" + prmTo + "]";
        return name;
    }

    private void setQuestPetriNet(){

        Increment countP = new Increment();
        Increment countT = new Increment();
        Increment countArc = new Increment();

        Transition transition;
        Arc arc;
        Place place;

        // Setup Initial Place
        place = new Place(placeName("Start", countP.increment()),1);                                                // create Start place
        quest.add(place);




        for(int c = 0; c < sequenceOfActions.size();c++){

            //Setup Transition
            transition = new Transition(transitionName(sequenceOfActions.get(c).getActionName(), countT.increment()));
            transition.setAction(sequenceOfActions.get(c));
            quest.add(transition);

            // Setup Arc last P -> Current T
            arc = new Arc(arcName(countArc.increment(), place.getName(), transition.getName()),place, transition);
            quest.add(arc);

            //Setup New Place
            place = new Place(placeName(sequenceOfActions.get(c).getActionName(), countP.increment()),0);
            quest.add(place);

            //Setup Arc Current T -> New P
            arc = new Arc(arcName(countArc.increment(), transition.getName(), place.getName()), transition, place);
            quest.add(arc);

        }



        Log.d("Quest sQPN", "Set End Place & Transition");
        //Setup End Transition
        transition = new Transition(transitionName("End Action" , countT.increment()));
        transition.setAction(new Action(countA.increment()));
        quest.add(transition);

        // Setup Arc from last Place to End Transition
        arc = new Arc(arcName(countArc.increment(), place.getName(), transition.getName()),place, transition);
        quest.add(arc);

        // Setup End Place
        place = new Place(placeName("End", countP.increment()),0);                                                // create initial place
        quest.add(place);

        // Setup Arc End Transition to End Place
        arc = new Arc(arcName(countArc.increment(), transition.getName(), place.getName()),transition, place);
        quest.add(arc);

    }


    public void testPetri() {

        Log.d("Quest tP", "---> Places: " + quest.getPlaces().size());
        for (int c = 0; c < quest.getPlaces().size(); c++){
            Log.d("Quest TP", quest.getPlaces().get(c).getName() + "Tokens: " + quest.getPlaces().get(c).getTokens());
        }

        Log.d("Quest tP", "---> Transitions: " + quest.getTransitions().size());
        for (Transition t : quest.getTransitions()){
            Log.d("Quest tP", t.getName());
        }

        Log.d("Quest tP", "---> Transitions able to fire: " + quest.getTransitionsAbleToFire().size());
        for (Transition t : quest.getTransitionsAbleToFire()){
            Log.d("Quest tP", t.getName());
        }

        Log.d("Quest tP", "---> Arcs: " + quest.getArcs().size());
        for (Arc a : quest.getArcs()){
            Log.d("Quest tP", a.getName());
        }


    }


}
