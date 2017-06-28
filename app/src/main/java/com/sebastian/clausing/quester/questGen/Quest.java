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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Quest {

    // Objects for instance-counting
    private Increment countAction = new Increment();
    Increment countPlace = new Increment();
    Increment countTransition = new Increment();
    Increment countArc = new Increment();


    //Lists
    private ArrayList<Integer> abstractQuestList = new ArrayList<Integer>();
    private ArrayList<Action> abstractActionList = new ArrayList<>();
    private ArrayList<Integer> appliedRulesList = new ArrayList<>();
    private ArrayList<Action> questList = new ArrayList<Action>();

    private int strategyID;
    private int motivationID;

    private String strategyName;
    private String motivationName;

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    //Petrinet
    private Petrinet quest = new Petrinet("Quest");


    public Quest(){

        Log.d("Quest", "--- NEW QUEST CONSTRUCTOR START -------------------------------------------------");

        Log.d("Quest", "----------> OpenDB");
        questerDB = dbHelper.getStaticDb();

        Log.d("Quest", "----------> Create Abstract Quest");
        createAbstractQuest();

        Log.d("Quest", "---------> Close DB");
        questerDB.close();

        Log.d("Quest", "---------> Create abstract PetriNet");
        abstractPetriNet();
        printPetri();

        Log.d("Quest", "---------> Find Split and Merge");
        findSplitMerge();

        printRewrittenActions();
        simulatePetri();

        Log.d("Quest", "---------> Exit Constructor Quest");

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
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 1:
                        abstractQuestList.add(16);
                        break;
                    case 2:
                        abstractQuestList.add(10);
                        abstractQuestList.add(12);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                    case 3:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(19);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                }
                break;
            case 1: //Comfort

                determineStrategy("Comfort");


                switch (strategyID){
                    case 0:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 1:
                        abstractQuestList.add(10);
                        abstractQuestList.add(2);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                }
                break;
            case 2: //Reputation

                determineStrategy("Reputation");


                switch (strategyID){
                    case 0:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 1:
                        abstractQuestList.add(10);
                        abstractQuestList.add(11);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                    case 2:
                        abstractQuestList.add(10);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                }
                break;
            case 3: //Serenity

                determineStrategy("Serenity");


                switch (strategyID){
                    case 0:
                        abstractQuestList.add(10);
                        abstractQuestList.add(2);
                        break;
                    case 1:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(19);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 2:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(19);
                        abstractQuestList.add(1);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 3:
                        abstractQuestList.add(10);
                        abstractQuestList.add(12);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                    case 4:
                        abstractQuestList.add(10);
                        abstractQuestList.add(18);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 5:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 6:
                        abstractQuestList.add(10);
                        abstractQuestList.add(2);
                        abstractQuestList.add(4);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                }
                break;
            case 4: //Protection

                determineStrategy("Protection");


                switch(strategyID){
                    case 0:
                        abstractQuestList.add(10);
                        abstractQuestList.add(2);
                        abstractQuestList.add(10);
                        abstractQuestList.add(15);
                        break;
                    case 1:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(19);
                        break;
                    case 2:
                        abstractQuestList.add(10);
                        abstractQuestList.add(14);
                        break;
                    case 3:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(19);
                        break;
                    case 4:
                        abstractQuestList.add(10);
                        abstractQuestList.add(2);
                        break;
                    case 5:
                        abstractQuestList.add(10);
                        abstractQuestList.add(14);
                        break;
                    case 6:
                        abstractQuestList.add(10);
                        abstractQuestList.add(3);
                        break;
                }
                break;
            case 5: //Conquest

                determineStrategy("Conquest");


                switch (strategyID){
                    case 0:
                        abstractQuestList.add(10);
                        abstractQuestList.add(2);
                        break;
                    case 1:
                        abstractQuestList.add(10);
                        abstractQuestList.add(21);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                }
                break;
            case 6: //Wealth

                determineStrategy("Wealth");


                switch(strategyID){
                    case 0:
                        abstractQuestList.add(10);
                        abstractQuestList.add(20);
                        break;
                    case 1:
                        abstractQuestList.add(10);
                        abstractQuestList.add(21);
                        break;
                    case 2:
                        abstractQuestList.add(14);
                        break;
                }
                break;
            case 7: //Ability

                determineStrategy("Ability");


                switch(strategyID){
                    case 0:
                        abstractQuestList.add(14);
                        abstractQuestList.add(19);
                        break;
                    case 1:
                        abstractQuestList.add(20);
                        abstractQuestList.add(19);
                        break;
                    case 2:
                        abstractQuestList.add(19);
                        break;
                    case 3:
                        abstractQuestList.add(2);
                        break;
                    case 4:
                        abstractQuestList.add(19);
                        break;
                    case 5:
                        abstractQuestList.add(20);
                        abstractQuestList.add(19);
                        break;
                    case 6:
                        abstractQuestList.add(20);
                        abstractQuestList.add(6);
                        break;
                }
                break;
            case 8: //Equipment

                determineStrategy("Equipment");


                switch(strategyID){
                    case 0:
                        abstractQuestList.add(14);
                        break;
                    case 1:
                        abstractQuestList.add(20);
                        abstractQuestList.add(10);
                        abstractQuestList.add(9);
                        break;
                    case 2:
                        abstractQuestList.add(21);
                        break;
                    case 3:
                        abstractQuestList.add(10);
                        abstractQuestList.add(5);
                        break;
                }
                break;
        }

        // Fill questList with Actions
        for(int c = 0; c < abstractQuestList.size(); c++){
            questList.add(new Action(countAction.increment(), abstractQuestList.get(c)));
        }

        //Fill abstractActionList with Actions
        for(Action a: questList){
            abstractActionList.add(a);
        }

    }

    private void findSplitMerge(){

        //Soulution for ConcurrentModificationException if " for(Arc a: quest.getArcs()){...} "
        List<Arc> arcListFS = new ArrayList<Arc>();

        //Copy List of Arcs in PetriNet
        for(Arc a: quest.getArcs()){
            arcListFS.add(a);
        }

        int listSize = arcListFS.size();


        Arc arcS;

        Log.d("Quest findSplitMerge ", "Search For Possible Split");
        for (int c = 0; c < listSize; c++){

            arcS = arcListFS.get(c);

            Transition tSplit = arcS.getTransition();  // Transition, which splits the net
            boolean isActionRewritable = tSplit.getAction().getRewriteAction();

            Log.d("Quest findSplitMerge ", "Check #" + c + ": Arc " + arcS.getName() + " Orientation: " + arcS.getOrientation());

            if(isActionRewritable==true && arcS.getOrientation().equals("TRANSITION_TO_PLACE")) {
                // If entered: We have an Arc, thats pointing from an REWRITABLE TRANSITION towards a PLACE
                // The Transition indicates an AND SPLIT in the petrinet
                // NOW, find the Transition, that MERGES the AND
                Place pConn = arcS.getPlace();               // Place between the Split and Merge Transition

                Log.d("Quest findSplitMerge", "Possible Split at: " + tSplit.getName());
                Log.d("Quest findSplitMerge", "Connection Place: " + pConn.getName());

                // Check again all Arcs, to find the Merge Transition
                // E.g. The transition which pConn points at

                boolean mergeFound = false;
                int c1 = 0;

                while(c1 < listSize && mergeFound == false){
                    c1++;

                    Arc arcM = arcListFS.get(c1);
                    Log.d("Quest findSplitMerge ", "Search Merge for: " + tSplit.getName() + " | AT: " + arcM.getName());

                    if(arcM.getOrientation().equals("PLACE_TO_TRANSITION") && arcM.getPlace().equals(pConn)){
                        Transition tMerge = arcM.getTransition();  // Transition, which merges the net
                        Log.d("Quest findSplitMerge", "Possible Merge at: " + tMerge.getName());

                        mergeFound = true;

                        // Now all needed information for rewriting are together
                        applyRules(tSplit, tMerge);
                    }
                    else{
                        Log.d("Quest findSplitMerge ", "- Cannot Merge at: " + arcM.getName() + " at Orientation " + arcM.getOrientation());
                    }
                }

                if(mergeFound = true){
                    Log.d("Quest findSplitMerge ", "Rewrite Succesfull");
                }else{
                    Log.d("Quest findSplitMerge ", "Rewrite Failed");
                }


            }
        }
    }

    private void openDB(){
        questerDB = dbHelper.getStaticDb();
    }

    private void applyRules_old(Transition prmTSplit, Transition prmTMerge) {

        for(int c = 0; c < questList.size(); c++){

            Log.d("Quest_rewrite Actions: " , "-----> Apply Rules on Entry " + questList.get(c).getActionName());

            for(int z = 0; z < questList.size(); z++) {
                Log.d("Quest_rewrite Actions:" , " Action ID: " + questList.get(z).getActionID()+ " Action Name: " + questList.get(z).getActionName());
            }

            if(questList.get(c).getRewriteAction() == true){

                switch (questList.get(c).getActionID()) {
                    case 1: // <capture>
                        questList.remove(c);
                        questList.add(c, new Action(countAction.increment() ,20));   //<get>
                        questList.add(c+1, new Action(countAction.increment() ,10)); //<goto>
                        questList.add(c+2, new Action(countAction.increment() ,24)); //capture
                        appliedRulesList.add(17);
                        Log.d("Quest_rewrite Actions: " ,"Rule 17: [<capture> --> <get>, <goto>, capture]");
                        break;

                    case 10: // <goto>
                        switch ((int) (Math.random() * 3)) {
                            case 0:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,25));   // goto
                                appliedRulesList.add(3);
                                Log.d("Quest_rewrite Actions: " ,"Rule 3: [<goto> --> goto]");
                                break;
                            case 1:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,7));    //explore
                                appliedRulesList.add(4);
                                Log.d("Quest_rewrite Actions: " ,"Rule 4: [<goto> --> explore]");
                                break;
                            case 2:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,23));   //<learn>
                                questList.add(c+1, new Action(countAction.increment() ,25)); //goto
                                appliedRulesList.add(5);
                                Log.d("Quest_rewrite Actions: " ,"Rule 5: [<goto> --> <learn>, goto]");
                                break;
                        }
                        break;

                    case 11: // <kill>
                        questList.remove(c);
                        questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                        questList.add(c++, new Action(countAction.increment() ,25)); //kill
                        appliedRulesList.add(18);
                        Log.d("Quest_rewrite Actions: " ,"Rule 18: [<kill> --> <goto>, kill]");
                        break;

                    case 16: // <spy>
                        questList.remove(c);
                        questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                        questList.add(c+1, new Action(countAction.increment() ,27)); //spy
                        questList.add(c+2, new Action(countAction.increment() ,10)); //<goto>
                        questList.add(c+3, new Action(countAction.increment() ,15)); //report
                        appliedRulesList.add(16);
                        Log.d("Quest_rewrite Actions: " ,"Rule 16: [<spy> --> <goto>, spy, <goto>, report]");
                        break;

                    case 20: // <get>
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,29));   //get
                                Log.d("Quest_rewrite Actions: " ,"Rule 10: [<get> --> get]");
                                break;
                            case 1:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,21));   //<steal>
                                appliedRulesList.add(11);
                                Log.d("Quest_rewrite Actions: " ,"Rule 11: [<get> --> <steal>]");
                                break;
                            case 2:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,8));   //<gather>
                                appliedRulesList.add(12);
                                Log.d("Quest_rewrite Actions: " ,"Rule 12: [<get> --> <goto>, <gather>]");
                                break;
                            case 3:

                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,20)); //<get>
                                questList.add(c+2, new Action(countAction.increment() ,10)); //<goto>
                                questList.add(c+3, new Action(countAction.increment() ,22)); //<subquest>
                                questList.add(c+4, new Action(countAction.increment() ,5)); //exchange
                                appliedRulesList.add(13);
                                Log.d("Quest_rewrite Actions: " ,"Rule 13: [<get> --> <goto>, <get>, <goto>, <subquest>, exchange]");
                                break;
                        }
                        break;

                    case 21: // <steal>
                        switch ((int) (Math.random() * 2)) {
                            case 0:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,17)); //stealth
                                questList.add(c+2, new Action(countAction.increment() ,18)); //take
                                appliedRulesList.add(14);
                                Log.d("Quest_rewrite Actions: " ,"Rule 14: [<steal> --> <goto>, stealth, take]");
                                break;
                            case 1:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,11)); //<kill>
                                questList.add(c+2, new Action(countAction.increment() ,18)); //take
                                appliedRulesList.add(15);
                                Log.d("Quest_rewrite Actions: " ,"Rule 15: [<steal> --> <goto>, <kill>, take]");
                                break;
                        }
                        break;

                    case 22: // <subquest>
                        switch ((int) (Math.random() * 2)) {
                            case 0:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                appliedRulesList.add(1);
                                Log.d("Quest_rewrite Actions: " ,"Rule 1: [<subquest> --> <goto>]");
                                break;
                            case 1:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,28)); //<Quest>
                                questList.add(c+2, new Action(countAction.increment() ,10)); //<goto>
                                appliedRulesList.add(2);
                                Log.d("Quest_rewrite Actions: " ,"Rule 2: [<subquest> --> <goto>, <Quest>, <goto>]");
                                break;
                        }
                        break;

                    case 23: // <learn>
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,30));   //learn
                                Log.d("Quest_rewrite Actions: " ,"Rule 6: [<learn> --> learn]");
                                break;
                            case 1:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,22)); //<subquest>
                                questList.add(c+2, new Action(countAction.increment() ,12)); //listen
                                appliedRulesList.add(7);
                                Log.d("Quest_rewrite Actions: " ,"Rule 7: [<learn> --> <goto>, <subquest>, listen]");
                                break;
                            case 2:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,10));   //<goto>
                                questList.add(c+1, new Action(countAction.increment() ,20)); //<get>
                                questList.add(c+2, new Action(countAction.increment() ,13)); //read
                                appliedRulesList.add(8);
                                Log.d("Quest_rewrite Actions: " ,"Rule 8: [<get> --> <goto>, <get>, read]");
                                break;
                            case 3:
                                questList.remove(c);
                                questList.add(c, new Action(countAction.increment() ,20));   //<get>
                                questList.add(c+1, new Action(countAction.increment() ,22)); //<subquest>
                                questList.add(c+2, new Action(countAction.increment() ,9)); //give
                                questList.add(c+3, new Action(countAction.increment() ,12)); //listen
                                appliedRulesList.add(9);
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

    private void applyRules_old2(Transition prmTSplit, Transition prmTMerge) {

        ArrayList<Action> newActionsList = new ArrayList<>();

        for(int c = 0; c < questList.size(); c++){

            newActionsList.clear();

            Log.d("Quest_rewrite Actions: " , "-----> Apply Rules on Entry " + questList.get(c).getActionName());

            for(int z = 0; z < questList.size(); z++) {
                Log.d("Quest_rewrite Actions:" , " Action ID: " + questList.get(z).getActionID()+ " Action Name: " + questList.get(z).getActionName());
            }

            if(questList.get(c).getRewriteAction() == true){

                switch (questList.get(c).getActionID()) {
                    case 1: // <capture>

                        newActionsList.add(new Action(countAction.increment() ,20)); //<get>
                        newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                        newActionsList.add(new Action(countAction.increment() ,24)); //capture

                        changeQuestList(newActionsList,c);

                        appliedRulesList.add(17);
                        Log.d("Quest_rewrite Actions: " ,"Rule 17: [<capture> --> <get>, <goto>, capture]");
                        break;

                    case 10: // <goto>
                        switch ((int) (Math.random() * 3)) {
                            case 0:
                                newActionsList.add(new Action(countAction.increment() ,25));   // goto

                                changeQuestList(newActionsList,c);

                                appliedRulesList.add(3);
                                Log.d("Quest_rewrite Actions: " ,"Rule 3: [<goto> --> goto]");
                                break;
                            case 1:
                                Log.d("Quest_rewrite Actions: " ,"c: " + c);

                                newActionsList.add(new Action(countAction.increment() ,7));    //explore

                                changeQuestList(newActionsList,c);

                                appliedRulesList.add(4);
                                Log.d("Quest_rewrite Actions: " ,"Rule 4: [<goto> --> explore]");
                                break;
                            case 2:
                                newActionsList.add(new Action(countAction.increment() ,23));   //<learn>
                                newActionsList.add(new Action(countAction.increment() ,25)); //goto
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(5);
                                Log.d("Quest_rewrite Actions: " ,"Rule 5: [<goto> --> <learn>, goto]");
                                break;
                        }
                        break;

                    case 11: // <kill>
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,25)); //kill
                        changeQuestList(newActionsList,c);
                        appliedRulesList.add(18);
                        Log.d("Quest_rewrite Actions: " ,"Rule 18: [<kill> --> <goto>, kill]");
                        break;

                    case 16: // <spy>
                        Log.d("Quest_rewrite Actions: " ,"c: " + c);
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,27)); //spy
                        newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                        newActionsList.add(new Action(countAction.increment() ,15)); //report
                        changeQuestList(newActionsList,c);
                        appliedRulesList.add(16);
                        Log.d("Quest_rewrite Actions: " ,"Rule 16: [<spy> --> <goto>, spy, <goto>, report]");
                        break;

                    case 20: // <get>
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                newActionsList.add(new Action(countAction.increment() ,29));   //get
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(10);
                                Log.d("Quest_rewrite Actions: " ,"Rule 10: [<get> --> get]");
                                break;
                            case 1:
                                newActionsList.add(new Action(countAction.increment() ,21));   //<steal>
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(11);
                                Log.d("Quest_rewrite Actions: " ,"Rule 11: [<get> --> <steal>]");
                                break;
                            case 2:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,8));   //<gather>
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(12);
                                Log.d("Quest_rewrite Actions: " ,"Rule 12: [<get> --> <goto>, <gather>]");
                                break;
                            case 3:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,20)); //<get>
                                newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                                newActionsList.add(new Action(countAction.increment() ,22)); //<subquest>
                                newActionsList.add(new Action(countAction.increment() ,5)); //exchange
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(13);
                                Log.d("Quest_rewrite Actions: " ,"Rule 13: [<get> --> <goto>, <get>, <goto>, <subquest>, exchange]");
                                break;
                        }
                        break;

                    case 21: // <steal>
                        switch ((int) (Math.random() * 2)) {
                            case 0:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,17)); //stealth
                                newActionsList.add(new Action(countAction.increment() ,18)); //take
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(14);
                                Log.d("Quest_rewrite Actions: " ,"Rule 14: [<steal> --> <goto>, stealth, take]");
                                break;
                            case 1:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,11)); //<kill>
                                newActionsList.add(new Action(countAction.increment() ,18)); //take
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(15);
                                Log.d("Quest_rewrite Actions: " ,"Rule 15: [<steal> --> <goto>, <kill>, take]");
                                break;
                        }
                        break;

                    case 22: // <subquest>
                        switch ((int) (Math.random() * 2)) {
                            case 0:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(1);
                                Log.d("Quest_rewrite Actions: " ,"Rule 1: [<subquest> --> <goto>]");
                                break;
                            case 1:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,28)); //<Quest>
                                newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(2);
                                Log.d("Quest_rewrite Actions: " ,"Rule 2: [<subquest> --> <goto>, <Quest>, <goto>]");
                                break;
                        }
                        break;

                    case 23: // <learn>
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                newActionsList.add(new Action(countAction.increment() ,30));   //learn
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(6);
                                Log.d("Quest_rewrite Actions: " ,"Rule 6: [<learn> --> learn]");
                                break;
                            case 1:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,22)); //<subquest>
                                newActionsList.add(new Action(countAction.increment() ,12)); //listen
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(7);
                                Log.d("Quest_rewrite Actions: " ,"Rule 7: [<learn> --> <goto>, <subquest>, listen]");
                                break;
                            case 2:
                                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                                newActionsList.add(new Action(countAction.increment() ,20)); //<get>
                                newActionsList.add(new Action(countAction.increment() ,13)); //read
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(8);
                                Log.d("Quest_rewrite Actions: " ,"Rule 8: [<get> --> <goto>, <get>, read]");
                                break;
                            case 3:
                                newActionsList.add(new Action(countAction.increment() ,20));   //<get>
                                newActionsList.add(new Action(countAction.increment() ,22)); //<subquest>
                                newActionsList.add(new Action(countAction.increment() ,9)); //give
                                newActionsList.add(new Action(countAction.increment() ,12)); //listen
                                changeQuestList(newActionsList,c);
                                appliedRulesList.add(9);
                                Log.d("Quest_rewrite Actions: " ,"Rule 9: [<get> --> <subquest>, give, listen]");
                                break;
                        }
                        break;

                    default:
                        Log.d("Quest_rewrite Actions: " ,"-----> No Change were Made");
                        break;
                }
            }
            // For each rewritten quest list rewrite the petrinet
            if(newActionsList.size()>=1)
            {
                Log.d("Quest_rewrite Actions: " ,"newActionsList 1 "+ newActionsList.get(0).getActionName());
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);
            }else{
                Log.d("Quest_rewrite Actions: " ,"-----> No Change for "+ questList.get(c).getActionName());
            }
        }
    }


    private void applyRules(Transition prmTSplit, Transition prmTMerge) {

        ArrayList<Action> newActionsList = new ArrayList<>();
        newActionsList.clear();

        Log.d("Quest applyRules: " , "-----> Check " + prmTSplit.getName() + " Action: " + prmTSplit.getAction().getActionName());

        int c = prmTSplit.getAction().getActionID();
        // For each action ID, there are several Rules how to rewrite them

        switch (c) {
            case 1: // <capture>
                Log.d("Quest applyRules: " ,"Rule 17: [<capture> --> <get>, <goto>, capture]");
                appliedRulesList.add(17);

                newActionsList.add(new Action(countAction.increment() ,20)); //<get>
                newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                newActionsList.add(new Action(countAction.increment() ,24)); //capture
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 10: // <goto>
                switch ((int) (Math.random() * 3)) {
                    case 0:
                        Log.d("Quest applyRules: " ,"Rule 3: [<goto> --> goto]");
                        appliedRulesList.add(3);

                        newActionsList.add(new Action(countAction.increment() ,25));   // goto

                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        Log.d("Quest applyRules: " ,"Rule 4: [<goto> --> explore]");
                        appliedRulesList.add(4);

                        newActionsList.add(new Action(countAction.increment() ,7));    //explore

                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(5);
                        Log.d("Quest applyRules: " ,"Rule 5: [<goto> --> <learn>, goto]");
                        newActionsList.add(new Action(countAction.increment() ,23));   //<learn>
                        newActionsList.add(new Action(countAction.increment() ,25)); //goto
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 11: // <kill>
                appliedRulesList.add(18);
                Log.d("Quest applyRules: " ,"Rule 18: [<kill> --> <goto>, kill]");
                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                newActionsList.add(new Action(countAction.increment() ,25)); //kill
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 16: // <spy>
                appliedRulesList.add(16);
                Log.d("Quest applyRules: " ,"Rule 16: [<spy> --> <goto>, spy, <goto>, report]");                newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                newActionsList.add(new Action(countAction.increment() ,27)); //spy
                newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                newActionsList.add(new Action(countAction.increment() ,15)); //report
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 20: // <get>
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        appliedRulesList.add(10);
                        Log.d("Quest applyRules: " ,"Rule 10: [<get> --> get]");
                        newActionsList.add(new Action(countAction.increment() ,29));   //get
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(11);
                        Log.d("Quest applyRules: " ,"Rule 11: [<get> --> <steal>]");
                        newActionsList.add(new Action(countAction.increment() ,21));   //<steal>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(12);
                        Log.d("Quest applyRules: " ,"Rule 12: [<get> --> <goto>, <gather>]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,8));   //<gather>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 3:
                        appliedRulesList.add(13);
                        Log.d("Quest applyRules: " ,"Rule 13: [<get> --> <goto>, <get>, <goto>, <subquest>, exchange]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,20)); //<get>
                        newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                        newActionsList.add(new Action(countAction.increment() ,22)); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,5)); //exchange
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 21: // <steal>
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        appliedRulesList.add(14);
                        Log.d("Quest applyRules: " ,"Rule 14: [<steal> --> <goto>, stealth, take]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,17)); //stealth
                        newActionsList.add(new Action(countAction.increment() ,18)); //take
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(15);
                        Log.d("Quest applyRules: " ,"Rule 15: [<steal> --> <goto>, <kill>, take]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,11)); //<kill>
                        newActionsList.add(new Action(countAction.increment() ,18)); //take
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 22: // <subquest>
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        appliedRulesList.add(1);
                        Log.d("Quest applyRules: " ,"Rule 1: [<subquest> --> <goto>]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(2);
                        Log.d("Quest applyRules: " ,"Rule 2: [<subquest> --> <goto>, <Quest>, <goto>]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,28)); //<Quest>
                        newActionsList.add(new Action(countAction.increment() ,10)); //<goto>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 23: // <learn>
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        appliedRulesList.add(6);
                        Log.d("Quest applyRules: " ,"Rule 6: [<learn> --> learn]");
                        newActionsList.add(new Action(countAction.increment() ,30));   //learn
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(7);
                        Log.d("Quest applyRules: " ,"Rule 7: [<learn> --> <goto>, <subquest>, listen]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,22)); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,12)); //listen
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(8);
                        Log.d("Quest applyRules: " ,"Rule 8: [<get> --> <goto>, <get>, read]");
                        newActionsList.add(new Action(countAction.increment() ,10));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,20)); //<get>
                        newActionsList.add(new Action(countAction.increment() ,13)); //read
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 3:
                        appliedRulesList.add(9);
                        Log.d("Quest applyRules: " ,"Rule 9: [<get> --> <subquest>, give, listen]");
                        newActionsList.add(new Action(countAction.increment() ,20));   //<get>
                        newActionsList.add(new Action(countAction.increment() ,22)); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,9)); //give
                        newActionsList.add(new Action(countAction.increment() ,12)); //listen
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            default:
                Log.d("Quest applyRules: " ,"-----> No change to:" + prmTSplit.getName()) ;
                break;
        }
    }

    private void changeQuestList(ArrayList<Action> prmList, int prmC){
        Log.d("Quest changeQuestList: " , "Remove: " + questList.get(prmC).getActionName() + " From " + prmC);
        questList.remove(prmC);

        for(Action a: prmList){
            questList.add(prmC, a);
            Log.d("Quest changeQuestList: " , "Add: " + a.getActionName() + " To " + prmC);
            prmC = prmC+1;
        }
    }

    private void rewritePetrinet(Transition prmTSplit, Transition prmTMerge, ArrayList<Action> prmActionList){

        Log.d("Quest rewritePetrinet", "Split at: " + prmTSplit.getName() + " | Merge at: " + prmTMerge.getName());

        Increment countPlace = new Increment();
        Increment countTransition = new Increment();
        Increment countArc = new Increment();

        Place place;
        Transition transition;
        Arc arc;

        prmTSplit.getAction().setRewriteAction(false);
        prmTSplit.getAction().setIsActionRewritten(true);

        // First Place after the Split
        place = new Place(setName("P"),0);
        quest.add(place);

        // Arc from Split Transition to first Place
        Arc arcTP = new Arc(setName("A"),prmTSplit, place);
        quest.add(arcTP);

        for(Action a: prmActionList){

            //Setup Transition
            transition = new Transition(setName("T"));
            transition.setAction(a);
            quest.add(transition);

            // Setup Arc last P -> Current T
            arc = new Arc(setName("A"),place, transition);
            quest.add(arc);

            //Setup New Place
            place = new Place(setName("P"),0);
            quest.add(place);

            //Setup Arc Current T -> New P
            arc = new Arc(setName("A"), transition, place);
            quest.add(arc);
        }

        // Connection the last place with the merge Transition
        Arc arcPT = new Arc(setName("A"),place, prmTMerge);
        quest.add(arcPT);

        Log.d("Quest rewritePetrinet", "Print new Petrinet");
    }

    private void abstractPetriNet(){

        Transition transition;
        Arc arc;
        Place place;

        place = new Place(setName("S"),1);                                                // create Start place
        quest.add(place);

        for(Action a: questList){

            //Setup Transition
            transition = new Transition(setName("T"));
            transition.setAction(a);
            quest.add(transition);

            // Setup Arc last P -> Current T
            arc = new Arc(setName("A"),place, transition);
            quest.add(arc);

            //Setup New Place
            place = new Place(setName("P"),0);
            quest.add(place);

            //Setup Arc Current T -> New P
            arc = new Arc(setName("A"), transition, place);
            quest.add(arc);

        }

        // Setup TRANSITION FINISH QUEST
        //Setup Transition
        transition = new Transition(setName("F"));
        transition.setAction(new Action (countAction.increment(),0));
        quest.add(transition);

        // Setup Arc last P -> Current T
        arc = new Arc(setName("A"), place, transition);
        quest.add(arc);

        // Setup END PACE
        place = new Place(setName("E"),0);                                                // create Start place
        quest.add(place);

        //Setup Arc Current T -> New P
        arc = new Arc(setName("A"), transition, place);
        quest.add(arc);
    }

    private void printLists(){

        Log.d("Quest", "---------> Print Lists");

        Log.d("Quest printLists: " , "#Actions: " + questList.size());
        for(int r = 0; r < questList.size(); r++) {
            Log.d("Quest printLists" , " Action ID: " + questList.get(r).getActionID()+ " Action Name: " + questList.get(r).getActionName());
        }

        Log.d("Quest pL: " , "#Used Rules: : " + appliedRulesList.size());
        for(int q = 0; q < appliedRulesList.size(); q++) {
            Log.d("Quest pL" , " Action ID: " + appliedRulesList.get(q));
        }

    }

    private String placeNameO(String prmName, int prmIncrement){
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

    private String transitionNameO(String prmName,int prmIncrement){
        String name = "T" + prmIncrement + " ("+ prmName+")";
        return name;
    }

    private String arcNameO(int prmIncrement, String prmFrom, String prmTo){
        String name = "A" + prmIncrement + " [" + prmFrom + "] --> [" + prmTo + "]";
        return name;
    }

    private String setName(String prmS){
        String name = "";

        if (prmS.equals("A")) {
            // SET NAME FOR ARC
            name = "A" + countArc.increment();

        } else if (prmS.equals("T")) {
            // SET NAME FOR TRANSITION
            name = "T" + countTransition.increment();

        }else if (prmS.equals("P")){
            // SET NAME FOR PLACE
            name = "P" + countPlace.increment();
        }else if (prmS.equals("S")){
            // SET NAME FOR START
            name = "P" + countPlace.increment() + " - Start";
        }else if(prmS.equals("E")){
            //SEt Name For END
            name = "P" + countPlace.increment() + " - End";
        }else if(prmS.equals("F")){
            name = "T" + countTransition.increment() + " - finish quest";
        }
        return name;
    }

    public void printPetri() {

        Log.d("Quest printPetri", "---> Places: " + quest.getPlaces().size());
        for (int c = 0; c < quest.getPlaces().size(); c++){
            Log.d("Quest printPetri", quest.getPlaces().get(c).getName() + " Tokens: " + quest.getPlaces().get(c).getTokens());
        }

        Log.d("Quest printPetri", "---> Transitions: " + quest.getTransitions().size());
        for (Transition t : quest.getTransitions()){
            Log.d("Quest printPetri", t.getName() + " - " + t.getAction().getActionName());
        }

        Log.d("Quest printPetri", "---> Transitions able to fire: " + quest.getTransitionsAbleToFire().size());
        for (Transition t : quest.getTransitionsAbleToFire()){
            Log.d("Quest printPetri", t.getName() +  " - "  + t.getAction().getActionName());
        }

        Log.d("Quest printPetri", "---> Arcs: " + quest.getArcs().size());
        for (Arc a : quest.getArcs()){

            if(a.getOrientation().equals("TRANSITION_TO_PLACE")){
                Log.d("Quest printPetri", a.getName() + " From  " + a.getTransition().getName() + " To " + a.getPlace().getName());
            }else{
                Log.d("Quest printPetri", a.getName() + " From  " + a.getPlace().getName() + " To " + a.getTransition().getName());
            }


        }


    }

    public void simulatePetri(){

        Log.d("Quest", "---------> Simulate Petri Net");
        while(quest.getTransitionsAbleToFire().size()>0){
            for(Transition t: quest.getTransitionsAbleToFire()){
                t.fire();
                Log.d("Quest Simulate", "Fire Action: " + t.getAction().getActionName());
            }
        }




    }

    public Petrinet getQuest(){
        return quest;
    }

    public ArrayList<Action> getQuestList(){
        //Log.d("Quest gSOA","Motivation = " + motivationName);
        //Log.d("Quest gSOA","Strategy = " + strategyName);
        return questList;
    }

    public ArrayList<Action> getAbstractList(){
        return abstractActionList;
    }

    public void printRewrittenActions(){

        Log.d("Quest", " -----> Print Rewritten Actions ");
        
        for(Transition tRa:  quest.getTransitions()){
            if( tRa.getAction().getIsActionRewritten() ==true ){
                Log.d("Quest printRewrittenAct", "Transition " + tRa.getName() + " - "  + tRa.getAction().getActionName());
            }
        }

    }

}
