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
import java.util.Iterator;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Quest {

    // Objects for instance-counting
    private Increment countAction = new Increment();


    //Lists
    private ArrayList<Integer> abstractQuestList = new ArrayList<Integer>();
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
        Log.d("Quest", "----------> OpenDB");
        questerDB = dbHelper.getStaticDb();

        Log.d("Quest", "----------> Create Abstract Quest");
        createAbstractQuest();

        Log.d("Quest", "---------> Close DB");
        questerDB.close();

        Log.d("Quest", "---------> Abstract PetriNet");
        abstractPetriNet();

        Log.d("Quest", "---------> Find Split and Merge");
        findSplitMerge();

        Log.d("Quest", "---------> Print Lists");
        printLists();

        Log.d("Quest", "---------> Simulate Petri Net");
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

    }

    public void findSplitMerge(){

        Arc arcS;
        Place pConn;

        //Soulution for ConcurrentModificationException if " for(Arc a: quest.getArcs()){...} "
        int i = quest.getArcs().size();

        for (int c = 0; c < i; c++){

            arcS = quest.getArcs().get(c);

            pConn = arcS.getPlace();             // Place between the Split and Merge Transition
            Transition tSplit = arcS.getTransition();  // Transition, which splits the net
            Transition tMerge = null;                  // Transition, which merges the net
            boolean bln = tSplit.getAction().getRewriteAction();

            if(bln==true && arcS.getOrientation().equals("TRANSITION_TO_PLACE")) {
                // If entered: We have an Arc, thats pointing from an REWRITABLE TRANSITION towards a PLACE

                Log.d("Quest findSplitMerge", "xxxx pConn name: " + pConn.getName());
                // The Transition indicates an AND SPLIT in the petri net
                // NOW, find the Transition, that MERGES the AND


                for(Arc arcM: quest.getArcs()){
                    if(arcM.getOrientation().equals("PLACE_TO_TRANSITION") && arcM.getPlace().equals(pConn)){
                        tMerge = arcM.getTransition();
                        Log.d("Quest findSplitMerge", "xxxx tMerge name: " + tMerge.getName());
                    }
                }

                // Now all needed information for rewriting are together
                applyRules(tSplit, tMerge);
            }

        }

    }

    public ArrayList<Action> getQuestList(){
        //Log.d("Quest gSOA","Motivation = " + motivationName);
        //Log.d("Quest gSOA","Strategy = " + strategyName);
        return questList;
    }

    public void openDB(){
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

    private void applyRules(Transition prmTSplit, Transition prmTMerge) {

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

    private void changeQuestList(ArrayList<Action> prmList, int c){
        int i=0;
        questList.remove(c);

        for(Action a: prmList){
            questList.add(c+i, a);
            i++;
        }
    }

    public void rewritePetrinet(Transition prmTSplit, Transition prmTMerge, ArrayList<Action> prmActionList){

        Increment countPlace = new Increment();
        Increment countTransition = new Increment();
        Increment countArc = new Increment();

        Place place;
        Transition transition;
        Arc arc;

        // First Place after the Split
        place = new Place(prmTSplit.getName() + " / " + placeName(prmTSplit.getAction().getActionName(), countPlace.increment()),0);
        quest.add(place);

        // Arc from Split Transition to first Place
        Arc arcTP = new Arc(arcName(countArc.increment(), prmTSplit.getName(), place.getName()),prmTSplit, place);
        quest.add(arcTP);

        for(Action a: prmActionList){

            //Setup Transition
            transition = new Transition(prmTSplit.getName() + " / " + transitionName(a.getActionName(), countTransition.increment()));
            transition.setAction(a);
            quest.add(transition);

            // Setup Arc last P -> Current T
            arc = new Arc(arcName(countArc.increment(), place.getName(), transition.getName()),place, transition);
            quest.add(arc);

            //Setup New Place
            place = new Place(prmTSplit.getName() + " / " + placeName(a.getActionName(), countPlace.increment()),0);
            quest.add(place);

            //Setup Arc Current T -> New P
            arc = new Arc(arcName(countArc.increment(), transition.getName(), place.getName()), transition, place);
            quest.add(arc);
        }

        // Connection the last place with the merge Transition
        Arc arcPT = new Arc(arcName(countArc.increment(), place.getName(), prmTMerge.getName()),place, prmTMerge);
        quest.add(arcPT);

    }

    private void abstractPetriNet(){

        Increment countPlace = new Increment();
        Increment countTransition = new Increment();
        Increment countArc = new Increment();

        Transition transition;
        Arc arc;
        Place place;

        place = new Place(placeName("Start", countPlace.increment()),1);                                                // create Start place
        quest.add(place);

        for(Action a: questList){

            //Setup Transition
            transition = new Transition(transitionName(a.getActionName(), countTransition.increment()));
            transition.setAction(a);
            quest.add(transition);

            // Setup Arc last P -> Current T
            arc = new Arc(arcName(countArc.increment(), place.getName(), transition.getName()),place, transition);
            quest.add(arc);

            //Setup New Place
            place = new Place(placeName(a.getActionName(), countPlace.increment()),0);
            quest.add(place);

            //Setup Arc Current T -> New P
            arc = new Arc(arcName(countArc.increment(), transition.getName(), place.getName()), transition, place);
            quest.add(arc);

        }
    }

    private void printLists(){


        Log.d("Quest pL: " , "#Actions: " + questList.size());
        for(int r = 0; r < questList.size(); r++) {
            Log.d("Quest pL" , " Action ID: " + questList.get(r).getActionID()+ " Action Name: " + questList.get(r).getActionName());
        }

        Log.d("Quest pL: " , "#Used Rules: : " + appliedRulesList.size());
        for(int q = 0; q < appliedRulesList.size(); q++) {
            Log.d("Quest pL" , " Action ID: " + appliedRulesList.get(q));
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




        for(int c = 0; c < questList.size(); c++){

            //Setup Transition
            transition = new Transition(transitionName(questList.get(c).getActionName(), countT.increment()));
            transition.setAction(questList.get(c));
            quest.add(transition);

            // Setup Arc last P -> Current T
            arc = new Arc(arcName(countArc.increment(), place.getName(), transition.getName()),place, transition);
            quest.add(arc);

            //Setup New Place
            place = new Place(placeName(questList.get(c).getActionName(), countP.increment()),0);
            quest.add(place);

            //Setup Arc Current T -> New P
            arc = new Arc(arcName(countArc.increment(), transition.getName(), place.getName()), transition, place);
            quest.add(arc);
        }



        Log.d("Quest sQPN", "Set End Place & Transition");
        //Setup End Transition
        transition = new Transition(transitionName("End Action" , countT.increment()));
        transition.setAction(questList.get(questList.size()-1));
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

    public void printPetri() {

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

    public void simulatePetri(){

        while(quest.getTransitionsAbleToFire().size()>0){
            for(Transition t: quest.getTransitionsAbleToFire()){
                t.fire();
                Log.d("Quest Simulate", "Fire Action: " + t.getAction().getActionName());
            }
        }




    }

}
