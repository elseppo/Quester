package com.sebastian.clausing.quester.questGen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.sebastian.clausing.quester.game.GameLogic;
import com.sebastian.clausing.quester.game.GameObject;
import com.sebastian.clausing.quester.game.Item;
import com.sebastian.clausing.quester.game.NPC;
import com.sebastian.clausing.quester.game.Player;
import com.sebastian.clausing.quester.helper.DataBaseHelper;
import com.sebastian.clausing.quester.helper.Increment;
import com.sebastian.clausing.quester.petriNet.Arc;
import com.sebastian.clausing.quester.petriNet.Petrinet;
import com.sebastian.clausing.quester.petriNet.Place;
import com.sebastian.clausing.quester.petriNet.Transition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebs-Desktop on 21.06.2017.
 */

public class Quest {

    // Objects for instance-counting
    private Increment countAction = new Increment();
    private Increment countPlace = new Increment();
    private Increment countTransition = new Increment();
    private Increment countArc = new Increment();

    private boolean doSplitMerge = true;
    private boolean foundRewritableAction;

    //Lists
    private ArrayList<Integer> abstractQuestList = new ArrayList<Integer>();
    private ArrayList<Integer> appliedRulesList = new ArrayList<>();
    private ArrayList<Action> questListAbstract = new ArrayList<Action>();
    private ArrayList<Transition> pendingTasks = new ArrayList<>();

    private int strategyID;
    private int motivationID;
    private int applyRulesCounter = 0;
    private int questDepth = 0;

    private String strategyName;
    private String motivationName;
    private String strategySequence;
    private String abstractDesctription = "";

    private NPC questGiver;
    private Player player;

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    //Petrinet
    private Petrinet quest = new Petrinet("Quest");

    //GameWorld
    GameLogic game;

    public Quest(GameLogic prmGame){

        this.game = prmGame;
        this.questGiver = game.getNPC();
        this.player = game.getPlayer();

        // Tell Player some random Locations
        player.updateKnowledge(game.getLocation(player));
        player.updateKnowledge(game.getLocation());
        player.updateKnowledge(game.getLocation());
        player.updateKnowledge(game.getLocation());
        player.updateKnowledge(game.getLocation());
        player.updateKnowledge(game.getLocation());
        player.updateKnowledge(game.getLocation());


        Log.d("Quest", "--- NEW QUEST -------------------------------------------------");

        createAbstractQuest();

        generateAbstractQuestDescription();

        createAbstractPetriNet();

        Log.d("Quest Strategy:", getStrategyName());


        //findSplitMerge Method is used, to
        //  1. Find a Transition which can be rewritten
        //  2. Apply the rule to that transition
        //  3. Add that rewritten Transitions to the Petrinet
        //  As long as there are rewritable actions, do
        //Do the Method as long until there are no more rewritable transitions

        while(doSplitMerge == true){

            foundRewritableAction = false;

            for (Transition t: quest.getTransitions()){
                if(t.getAction().getRewriteAction() == true){
                    foundRewritableAction = true;
                }
            }

            if(foundRewritableAction == true)
            {
                findSplitMerge();
            }
            else
            {
                doSplitMerge = false;
            }

        }

        //After this Method the quest is fully generated and rules are applied.

        //printRewrittenActions();
        printPetri();
        simulatePetri();


        Log.d("Quest", "---------> EXIT QUEST");
    }

    private void determineMotivation(){
        //Determine Motivation
        int totalMotivations = 9;
        motivationID=(int) (Math.random()* totalMotivations); // Possible Outcomes from 0 to (totalMotivations -1)
    }

    private void determineStrategyOld(String prmMotivation) {

        // Variables
        motivationName = prmMotivation;
        Cursor c;
        int totalStrategies;

        //Open DB
        Log.d("Quest", "----------> OpenDB");
        questerDB = dbHelper.getStaticDb();

        // Determine how many Strategies the Motivation has
        c = questerDB.rawQuery("SELECT Count(*) FROM " + motivationName + ";", null);
        c.moveToFirst();
        totalStrategies = c.getInt(0);

        //Determine a random Strategy
        strategyID = (int) (Math.random() * totalStrategies); // Possible Outcomes from 0 to (totalStrategies -1)

        //Set Strategy Name and strategy sequence
        c = questerDB.rawQuery("SELECT * FROM " + "'" + motivationName + "'" + "WHERE _id =  " + "'" + strategyID + "';", null);
        c.moveToFirst();
        strategyName = c.getString(1);
        strategySequence = c.getString(2);

        Log.d("Quest", "---------> Name " + strategyName);
        Log.d("Quest", "---------> Sequence " + strategySequence);

        // Determine the sequence of actions and put them into a integer list
        String sequence[] = strategySequence.split(",");

        for (String s : sequence) {
            abstractQuestList.add(Integer.parseInt(s));
        }

        //Get a Strategy
        String strategy[] = strategyName.split(";");
        strategyName = strategy[(int) (Math.random() * (strategy.length - 1))];

        Log.d("Quest", "---------> Close DB");
        questerDB.close();


        // Fill questListAbstract with Actions
        for (int c1 = 0; c1 < abstractQuestList.size(); c1++) {
            questListAbstract.add(new Action(countAction.increment(), abstractQuestList.get(c1)));
        }

        //For all abstract quests, where the Quest giver is needed in a later Action
        //Set the quest giver at this point to the last action.
        if (     motivationID == 0 ||
                 motivationID == 1 ||
                 motivationID == 2 ||
                (motivationID == 3 && strategyID != 0) ||
                (motivationID == 4 && strategyID == 0) ||
                (motivationID == 5 && strategyID == 1) ||
                (motivationID == 8 && strategyID == 1)) {
            questListAbstract.get(questListAbstract.size() - 1).addGameObject(questGiver);
        }

    }

    private void determineStrategy(String prmMotivation) {

        // Variables
        motivationName = prmMotivation;
        Cursor c;
        int totalStrategies;

        //Open DB
        //Log.d("Quest", "----------> OpenDB");
        questerDB = dbHelper.getStaticDb();

        // Determine how many Strategies the Motivation has
        c = questerDB.rawQuery("SELECT Count(*) FROM " + motivationName + ";", null);
        c.moveToFirst();
        totalStrategies = c.getInt(0);

        //Determine a random Strategy
        strategyID = (int) (Math.random() * totalStrategies); // Possible Outcomes from 0 to (totalStrategies -1)


        //Set Strategy Name and strategy sequence
        c = questerDB.rawQuery("SELECT * FROM " + "'" + motivationName + "'" + "WHERE _id =  " + "'" + strategyID + "';", null);
        c.moveToFirst();
        strategyName = c.getString(1);
        strategySequence = c.getString(2);

        //Log.d("Quest", "---------> Name " + strategyName);
        //Log.d("Quest", "---------> Sequence " + strategySequence);

        // Determine the sequence of actions and put them into a integer list
        String sequence[] = strategySequence.split(",");

        for (String s : sequence) {
            abstractQuestList.add(Integer.parseInt(s));
        }

        //Get a Strategy
        String strategy[] = strategyName.split(";");
        strategyName = strategy[(int) (Math.random() * (strategy.length - 1))];

        //Log.d("Quest", "---------> Close DB");
        questerDB.close();

        Log.d("Quest","Motivation: " + prmMotivation+", Strategy: " + strategyID + " " + strategyName);


        // Fill questListAbstract with Actions
        //for (int c1 = 0; c1 < abstractQuestList.size(); c1++) {
        //    questListAbstract.add(new Action(countAction.increment(), abstractQuestList.get(c1)));
        //}

        //For all abstract quests, where the Quest giver is needed in a later Action
        //Set the quest giver at this point to the last action.
        //if (     motivationID == 0 ||
        //        motivationID == 1 ||
        //        motivationID == 2 ||
        //        (motivationID == 3 && strategyID != 0) ||
        //        (motivationID == 4 && strategyID == 0) ||
        //        (motivationID == 5 && strategyID == 1) ||
        //        (motivationID == 8 && strategyID == 1)) {
        //    questListAbstract.get(questListAbstract.size() - 1).addGameObject(questGiver);
        //}

    }

    private void createAbstractQuest(){

        // Determine the Quests Motivation
        determineMotivation();
        GameObject mainGO;
        Item mainItem;

        //
        //motivationID = 0;
        //


        // Determine Strategy According to Motivation ID
        switch (motivationID){
            case 0:
                //Knowledge
                determineStrategy("Knowledge");


                switch(strategyID){
                    case 0: //Deliver Item for Study

                        Item getItem = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,20, getItem));   //<get> takeable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,9, getItem, questGiver)); //Give Item to NPC
                        break;

                    case 1: // SSpy
                        questListAbstract.add(new Action(countAction.increment() ,16, game.getNPC()));   //<spy> NPC
                        break;

                    case 2: //Interview NPC

                        NPC listen = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(listen)));         //<goto> Loc
                        questListAbstract.add(new Action(countAction.increment() ,12, listen));                          //listen NPC
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));   //<goto> questgiver
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));                     //report quetGIver
                        break;

                    case 3: //Use an item in the field

                        Item getItem2 = game.getItem(6);

                        questListAbstract.add(new Action(countAction.increment() ,20, getItem2));                    //<get> Item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation()));                            //<goto> loc
                        questListAbstract.add(new Action(countAction.increment() ,19, getItem2));                    //use item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> quetGIver
                        questListAbstract.add(new Action(countAction.increment() ,9,  getItem2, questGiver));                     //<give> item to quetGIver
                        break;
                }
                break;

            case 1:
                //Comfort
                determineStrategy("Comfort");

                switch(strategyID){
                    case 0: // Obtain Luxuries
                        Item getItem = game.getItem(3);
                        questListAbstract.add(new Action(countAction.increment() ,20, getItem));    //<get> Item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(getItem)));    //<goto> Item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> quetGIver
                        questListAbstract.add(new Action(countAction.increment() ,9, getItem, questGiver));    //<give> Item
                        break;

                    case 1: //Kill pests
                        NPC pests = game.getNPC();
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(pests)));                    //<goto> pests
                        questListAbstract.add(new Action(countAction.increment() ,11, pests));          //<kill> pests
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));  //report to quetGIver
                        break;
                }
                break;

            case 2:
                //Reputation
                determineStrategy("Reputation");

                switch (strategyID){
                    case 0: //Optain rare Item
                        Item getItem = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,20, getItem));   //<get> takeable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,9, getItem, questGiver)); //Give Item to NPC
                        break;

                    case 1: // kill enemy
                        NPC npc = game.getNPC();
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npc)));                    //<goto> pests
                        questListAbstract.add(new Action(countAction.increment() ,11, npc));          //<kill> pests
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));  //report to quetGIver
                        break;

                    case 2: //visit a location and report
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation()));                    //<goto> location
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> QG
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));  //report to quetGIver
                        break;
                }
                break;

            case 3:
                //Serenity
                determineStrategy("Serenity");

                switch (strategyID){
                    case 0: //revenge justice
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation()));                    //<goto> location
                        questListAbstract.add(new Action(countAction.increment() ,11, game.getNPC()));          //<kill> npc
                        break;

                    case 1: //capture criminal
                        Item itemCapture = game.getItem(7);
                        NPC npcCapture = game.getNPC();
                        questListAbstract.add(new Action(countAction.increment() ,1, itemCapture));   //<get> capturable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npcCapture))); //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,19, itemCapture, npcCapture)); //use capturable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));       //<goto> QG
                        questListAbstract.add(new Action(countAction.increment() ,9, npcCapture, questGiver)); //Give captured npc to NPC
                        Log.d("Quest", "Item " + itemCapture.getName());
                        break;

                    case 2: //capture criminal 2
                        Item itemCapture2 = game.getItem(7);
                        NPC npcCapture2 = game.getNPC();
                        questListAbstract.add(new Action(countAction.increment() ,1, itemCapture2));   //<get> capturable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npcCapture2))); //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,19, itemCapture2, npcCapture2)); //use capturable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));       //<goto> QG
                        questListAbstract.add(new Action(countAction.increment() ,9, npcCapture2, questGiver)); //Give captured npc to NPC
                        Log.d("Quest", "Item " + itemCapture2.getName());
                        break;

                    case 3: //check on npc
                        NPC npc = game.getNPC();
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npc)));                    //<goto> pests
                        questListAbstract.add(new Action(countAction.increment() ,12, npc));          //listen to npc
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));  //report to quetGIver
                        break;

                    case 4: //check on npc 2
                        NPC npc2 = game.getNPC();
                        Item item2 = game.getItem(3);
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(item2)));                    //<goto> item
                        questListAbstract.add(new Action(countAction.increment() ,18, item2));          //take item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npc2)));                    //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,9, item2, questGiver));  //give tiem to quetGIver
                        break;

                    case 5: //recover lost / stolen item
                        Item item3 = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,20, item3));          //<get> item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,9, item3, questGiver));  //give tiem to quetGIver
                        break;

                    case 6: //rescue captured npc
                        NPC rescue = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(rescue)));      //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,2, game.getLocation(rescue)));                         // damage
                        questListAbstract.add(new Action(countAction.increment() ,4, rescue));                         // escort
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));      //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));  //report to quetGIver
                        break;
                }

                break;

            case 4:
                //Protection
                determineStrategy("Protection");

                switch(strategyID){

                    case 0: // attack threatening entities
                        NPC threat = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(threat)));      //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,2, threat));                         // damage
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));      //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,15, questGiver));  //report to quetGIver
                        break;

                    case 1: // treat or repair 1
                        Item tool = game.getItem(3);
                        Item repair = game.getItem(5);

                        questListAbstract.add(new Action(countAction.increment() ,20, tool));   //<get> takeable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(repair))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,19, tool)); //use Item to NPC
                        break;

                    case 2: //treat or repair 2
                        Item repair2 = game.getItem(5);

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(repair2)));   //<goto> repair
                        questListAbstract.add(new Action(countAction.increment() ,14, repair2)); //repair Item to NPC
                        break;

                    case 3: // create diversion
                        Item toolDiv = game.getItem(3);
                        NPC Diverse = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,20, toolDiv));   //<get> item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(Diverse))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,19, toolDiv)); //Use Item to NPC
                        break;

                    case 4: //create diversion2
                        NPC Diverse2 = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(Diverse2))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,2, game.getLocation(Diverse2))); //damage
                        break;

                    case 5: //assenble fortifaction
                        Item fortify = game.getItem(5);

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(fortify))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,14, fortify)); //repair Item to NPC
                        break;

                    case 6: //guard entity
                        NPC defend = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(defend))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,3, defend)); //defend Item to NPC
                        break;
                }

                break;

            case 5:
                //Conquest
                determineStrategy("Conquest");

                switch(strategyID){

                    case 0: //attack enemy
                        NPC attack = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(attack))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,2, game.getLocation(attack))); //damage
                        break;

                    case 1: //steal stuff
                        NPC npc2 = game.getNPC();
                        Item item2 = game.getItem(3);
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npc2)));                    //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,21, item2));          //steal from npc 2
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver)));                    //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,9, item2, questGiver));  //give to quetGIver
                        break;
                }
                break;


            case 6:
                //Wealth
                determineStrategy("Wealth");

                switch (strategyID){

                    case 0: //gather raw material
                        Item gather = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(gather))); //<goto> item
                        questListAbstract.add(new Action(countAction.increment() ,20, gather)); //gather Item
                        break;

                    case 1: //steal valuables for resale
                        Item value = game.getItem(3);
                        NPC npc = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(npc))); //<goto> item
                        questListAbstract.add(new Action(countAction.increment() ,21, value, npc)); //steal Item
                        break;

                    case 2: // make vauluables for resale
                        Item repair = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(repair))); //<goto> item
                        questListAbstract.add(new Action(countAction.increment() ,14, repair)); //repair Item
                        break;
                }
                break;

            case 7:
                //Ability
                determineStrategy("Ability");

                switch(strategyID){

                    case 0: //assemble toool for new skill
                        Item repair = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(repair))); //<goto> item
                        questListAbstract.add(new Action(countAction.increment() ,19, repair)); //use Item
                        break;

                    case 1: // obtain training materials
                        Item item = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,20, item));   //<get> item
                        questListAbstract.add(new Action(countAction.increment() ,19, item)); //Use Item
                        break;

                    case 2: //use existing tool
                        Item tool = game.getItem(6);
                        questListAbstract.add(new Action(countAction.increment() ,19, tool)); //Use Item
                        break;

                    case 3: //Practice combat
                        Item item1 = game.getItem(6);
                        questListAbstract.add(new Action(countAction.increment() ,2, item1)); //Use Item
                        break;

                    case 4: //Practice skill
                        Item item2 = game.getItem(6);
                        questListAbstract.add(new Action(countAction.increment() ,19, item2)); //Use Item
                        break;

                    case 5: // research skill
                        Item item3 = game.getItem(6);
                        questListAbstract.add(new Action(countAction.increment() ,20, item3));   //<get>
                        questListAbstract.add(new Action(countAction.increment() ,19, item3)); //Use
                        break;

                    case 6: // research skill 2
                        Item item4 = game.getItem(6);
                        questListAbstract.add(new Action(countAction.increment() ,20, item4));   //<get>
                        questListAbstract.add(new Action(countAction.increment() ,6, item4)); //experiment skill
                        break;
                }

                break;

            case 8:
                //Equipment
                determineStrategy("Equipment");

                switch (strategyID){

                    case 0: // assemble
                        Item rep = game.getItem(5);

                        questListAbstract.add(new Action(countAction.increment() ,14, rep));   //repair
                        break;

                    case 1: //deliver supplies
                        Item getItem = game.getItem(3);

                        questListAbstract.add(new Action(countAction.increment() ,20, getItem));   //<get> takeable item
                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(questGiver))); //<goto> quest giver
                        questListAbstract.add(new Action(countAction.increment() ,9, getItem, questGiver)); //Give Item to NPC
                        break;

                    case 2: //Steal supplies
                        Item steal = game.getItem(3);
                        NPC npc = game.getNPC();


                        questListAbstract.add(new Action(countAction.increment() ,21, steal, npc)); //steal Item
                        break;

                    case 3: // trade for supplies
                        Item getItem2 =  game.getItem(3);
                        Item giveITem =  game.getItem(3);
                        NPC trade = game.getNPC();

                        questListAbstract.add(new Action(countAction.increment() ,10, game.getLocation(trade))); //<goto> npc
                        questListAbstract.add(new Action(countAction.increment() ,5, trade, giveITem, getItem2)); //exchange
                        break;

                }
                break;
        }
    }

    private void findSplitMerge(){
        //Soulution for ConcurrentModificationException if " for(Arc a: quest.getArcs()){...} "
        List<Arc> arcListFS = new ArrayList<>();

        //Copy List of Arcs in PetriNet
        for(Arc a: quest.getArcs()){
            arcListFS.add(a);
        }
        int listSize = arcListFS.size();
        Arc arcS;

        //Log.d("Quest findSplitMerge ", "Search For Possible Split");
        //For each Arc of arcListFS
        for (int c = 0; c < listSize; c++){
            arcS = arcListFS.get(c);

            Transition tSplit = arcS.getTransition();  // Transition, which splits the petrinet
            boolean isActionRewritable = tSplit.getAction().getRewriteAction();

            //Log.d("Quest findSplitMerge ", "Check #" + c + ": Arc " + arcS.getName() + " Orientation: " + arcS.getOrientation());

            if(isActionRewritable==true && arcS.getOrientation().equals("TRANSITION_TO_PLACE")) {
                // If entered: We have an Arc, thats pointing from an REWRITABLE TRANSITION towards a PLACE
                // The Transition indicates an AND SPLIT in the petrinet
                // NOW, find the Transition, that MERGES the AND
                Place pConn = arcS.getPlace();               // Place between the Split and Merge Transition

                //Log.d("Quest findSplitMerge", "Possible Split at: " + tSplit.getName());
                //Log.d("Quest findSplitMerge", "Connection Place: " + pConn.getName());

                // Check again all Arcs, to find the Merge Transition
                // E.g. The transition which pConn points at

                boolean mergeFound = false;
                int c1 = 0;

                while(c1 < listSize && mergeFound == false){
                    c1++;

                    Arc arcM = arcListFS.get(c1);
                   // Log.d("Quest findSplitMerge ", "Search Merge for: " + tSplit.getName() + " | AT: " + arcM.getName());

                    if(arcM.getOrientation().equals("PLACE_TO_TRANSITION") && arcM.getPlace().equals(pConn)){
                        Transition tMerge = arcM.getTransition();  // Transition, which merges the net
                        //Log.d("Quest findSplitMerge", "Possible Merge at: " + tMerge.getName());

                        mergeFound = true;

                        // Now all needed information for rewriting are together
                        applyRules(tSplit, tMerge);

                        //Copy new updated List of Arcs (To find rewritable transitions that have just been created in applyRules)
                        //  Also set counter back to 0
                        //Copy List of Arcs in PetriNet

                    }
                    else{
                        //Log.d("Quest findSplitMerge ", "- Cannot Merge at: " + arcM.getName() + " at Orientation " + arcM.getOrientation());
                    }
                }
            }
        }
    }

    private void applyRules(Transition prmTSplit, Transition prmTMerge) {

        applyRulesCounter++;

        ArrayList<Action> newActionsList = new ArrayList<>();
        newActionsList.clear();


        int c = prmTSplit.getAction().getActionID();
        GameObject gOBJ = prmTSplit.getAction().getGameObject();

        Log.d("Quest applyRules: " , "-----> " + applyRulesCounter + ". Iteration: " + prmTSplit.getName() + " Action: " + prmTSplit.getAction().getActionName() + " " + gOBJ.getName());


        // For each action ID, there are several Rules how to rewrite them
        switch (c) {
            case 1: // <capture>
                Log.d("Quest applyRules: " ,"Rule 17: [<capture> --> <get>, <goto>, capture]");
                appliedRulesList.add(17);

                newActionsList.add(new Action(countAction.increment() ,20, game.getItem(7))); //<get>
                newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(gOBJ))); //<goto>
                newActionsList.add(new Action(countAction.increment() ,24, gOBJ)); //capture

                break;

            case 10: // <goto>

                // If location is unknown, learn it
                if(player.checkKN(gOBJ) == false){
                    appliedRulesList.add(5);
                    Log.d("Quest applyRules: " ,"Rule 5: [<goto> --> <learn>, goto]");

                    newActionsList.add(new Action(countAction.increment() ,23, game.getLocation(gOBJ)));   //<learn>
                    newActionsList.add(new Action(countAction.increment() ,25, gOBJ)); //goto
                    //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);
                }
                else{ // else go to it
                    switch ((int) (Math.random() * 2)) {
                        case 0:
                            Log.d("Quest applyRules: " ,"Rule 3: [<goto> --> goto]");
                            appliedRulesList.add(3);

                            newActionsList.add(new Action(countAction.increment() ,25, gOBJ));   // goto
                            //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                            break;
                        case 1:
                            Log.d("Quest applyRules: " ,"Rule 4: [<goto> --> explore]");
                            appliedRulesList.add(4);

                            newActionsList.add(new Action(countAction.increment() ,7, gOBJ));    //explore
                            //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                            break;
                    }
                }
                break;


            case 11: // <kill>
                appliedRulesList.add(18);
                Log.d("Quest applyRules: " ,"Rule 18: [<kill> --> <goto>, kill]");
                newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(gOBJ)));   //<goto>
                newActionsList.add(new Action(countAction.increment() ,26, gOBJ)); //kill
                //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 16: // <spy>
                appliedRulesList.add(16);
                Log.d("Quest applyRules: " ,"Rule 16: [<spy> --> <goto>, spy, goto, report]");
                newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(gOBJ)));   //<goto>
                newActionsList.add(new Action(countAction.increment() ,27, gOBJ)); //spy
                newActionsList.add(new Action(countAction.increment() ,25, game.getLocation(questGiver))); //goto (back to gquest giver)
                newActionsList.add(new Action(countAction.increment() ,15, questGiver)); //report   to quest giver
                //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 20: // <get>
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        appliedRulesList.add(10);
                        Log.d("Quest applyRules: " ,"Rule 10: [<get> --> get]");
                        newActionsList.add(new Action(countAction.increment() ,29, gOBJ));   //get
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(11);
                        NPC npc = game.getNPC();
                        Item item = (Item) gOBJ;

                        Log.d("Quest applyRules: " ,"Rule 11: [<get> --> <steal>]");
                        newActionsList.add(new Action(countAction.increment() ,21 , item, npc));   //<steal>
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(12);
                        Log.d("Quest applyRules: " ,"Rule 12: [<get> --> <goto>, <gather>]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(gOBJ)));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,8, gOBJ));   //<gather>
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 3:
                        appliedRulesList.add(13);
                        Log.d("Quest applyRules: " ,"Rule 13: [<get> --> <goto>, <get>, <goto>, <subquest>, exchange]");

                        Item itemForPlayer = prmTSplit.getAction().getItem(0);
                        Item itemForNPC = game.getItem(3);
                        NPC exchangeNPC = game.getNPC();

                        //newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(itemForNPC)));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,20, itemForNPC)); //<get>
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(exchangeNPC))); //<goto> exchange NPC
                        //newActionsList.add(new Action(countAction.increment() ,22, game.getPlaceholder())); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,5, exchangeNPC, itemForNPC, itemForPlayer)); //exchange
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);
                        break;
                }
                break;

            case 21: // <steal>
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        appliedRulesList.add(14);
                        Log.d("Quest applyRules: " ,"Rule 14: [<steal> --> <goto>, stealth, take]");

                        //FEHLER
                        NPC steal = prmTSplit.getAction().getNPC(0);
                        Item take = prmTSplit.getAction().getItem(0);

                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(steal)));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,17, steal)); //stealth
                        newActionsList.add(new Action(countAction.increment() ,18, take)); //take
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(15);
                        Log.d("Quest applyRules: " ,"Rule 15: [<steal> --> <goto>, <kill>, take]");

                        NPC steal2 = prmTSplit.getAction().getNPC(0);
                        Item take2 = prmTSplit.getAction().getItem(0);

                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(steal2)));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,11, steal2)); //<kill>
                        newActionsList.add(new Action(countAction.increment() ,18, take2)); //take
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 22: // <subquest>
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        appliedRulesList.add(1);
                        Log.d("Quest applyRules: " ,"Rule 1: [<subquest> --> <goto>]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(2);
                        Log.d("Quest applyRules: " ,"Rule 2: [<subquest> --> <goto>, <Quest>, <goto>]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        //newActionsList.add(new Action(countAction.increment() ,28, game.getPlaceholder())); //<Quest>
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation())); //<goto>
                        //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 23: // <learn>

                // if true, you know where it is.
                //if (player.checkKN(gOBJ) == true) {
                //    appliedRulesList.add(6);
                //    Log.d("Quest applyRules: " ,"Rule 6: [<learn> --> learn]");
                //    newActionsList.add(new Action(countAction.increment() ,30, game.getLocation(gOBJ)));   //learn
                    //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);
                //}
                // else learn it
                //else{
                    switch ((int) (Math.random() * 3)) {

                        case 0:
                            appliedRulesList.add(7);
                            Log.d("Quest applyRules: " ,"Rule 7: [<learn> --> <goto>, <subquest>, listen]");

                            NPC listen = game.getNPC();

                            // Check special case: cannot go somewhere u dont know
                            while(game.getLocation(listen) == game.getLocation(gOBJ))
                            {
                                Log.d("Quest Special Case: " ,"Rule 7");
                                listen = game.getNPC();
                            }

                            newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(listen)));   //<goto>
                            //newActionsList.add(new Action(countAction.increment() ,22, game.getPlaceholder())); //<subquest>
                            newActionsList.add(new Action(countAction.increment() ,12, listen)); //listen
                            //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                            break;
                        case 1:
                            appliedRulesList.add(8);
                            Log.d("Quest applyRules: " ,"Rule 8: [<learn> --> <goto>, <get>, read]");

                            Item read = game.getItem(4);

                            while(game.getLocation(read) == game.getLocation(gOBJ)){
                                read = game.getItem(4);
                            }

                            newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(read)));   //<goto>
                            newActionsList.add(new Action(countAction.increment() ,20, read)); //<get>
                            newActionsList.add(new Action(countAction.increment() ,13, read)); //read
                            //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                            break;
                        case 2:
                            appliedRulesList.add(9);
                            Log.d("Quest applyRules: " ,"Rule 9: [<learn> --> <get> <subquest>, give, listen]");

                            Item give = game.getItem();
                            NPC listen2 = game.getNPC();

                            newActionsList.add(new Action(countAction.increment() ,20, give));   //<get>
                            //newActionsList.add(new Action(countAction.increment() ,22, game.getPlaceholder())); //<subquest>
                            newActionsList.add(new Action(countAction.increment() ,10, game.getLocation(listen2)));   //<goto>
                            newActionsList.add(new Action(countAction.increment() ,9, give, listen2)); //give
                            newActionsList.add(new Action(countAction.increment() ,12,listen2)); //listen
                            //rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                            break;
                  //  }

                }
                break;

            default:
                Log.d("Quest applyRules: " ,"-----> No change to:" + prmTSplit.getName()) ;
                break;
        }
        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);
    }

    private void rewritePetrinet(Transition prmTSplit, Transition prmTMerge, ArrayList<Action> prmActionList){

        Log.d("Quest rewritePetrinet", "Split at: " + prmTSplit.getName() + " | Merge at: " + prmTMerge.getName());

        Place place;
        Transition transition;
        Arc arc;

        int newDepth = prmTSplit.getDepth() + 1;

        //Update the overall quest Depth parameter
        if(newDepth > questDepth){
            questDepth = newDepth;
        }

        prmTSplit.getAction().setRewriteAction(false);
        prmTSplit.getAction().setIsActionRewritten(true);
        prmTSplit.setSplit(true);
        prmTMerge.setMerge(true);

        //Add Split to pending Tasks
        pendingTasks.add(prmTSplit);

        // First Place after the Split
        place = new Place(setName("P"),0);
        quest.add(place);

        // Arc from Split Transition to first Place
        Arc arcTP = new Arc(setName("A"),prmTSplit, place);
        quest.add(arcTP);

        for(Action a: prmActionList){

            a.setDescription();

            //Setup Transition
            transition = new Transition(setName("T"), newDepth);
            transition.setAction(a);
            Log.d("RewritePN applyRules", transition.getName() + " holds " + transition.getAction().getGameObject().getName());
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

        //Update Player KNB
        if (prmTSplit.getAction().getActionID() == 23 || prmTSplit.getAction().getActionID() == 30) {
            player.updateKnowledge(prmTSplit, prmTMerge);
        }

        //Log.d("Quest rewritePetrinet", "Print new Petrinet");
    }

    private void createAbstractPetriNet(){

        Transition transition;
        Arc arc;
        Place place;

        int initialDepth = 0;

        place = new Place(setName("S"),1);                                                // create Start place
        quest.add(place);

        for(Action a: questListAbstract){

            //Setup Transition
            transition = new Transition(setName("T"), initialDepth);
            transition.setAction(a);
            //transition.updatePlayerKN();
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
        transition = new Transition(setName("F"), initialDepth);
        transition.setAction(new Action (countAction.increment(),0, game.getPlaceholder()));
       // transition.updatePlayerKN();
        quest.add(transition);

        // Setup Arc last P -> Current T
        arc = new Arc(setName("A"), place, transition);
        quest.add(arc);

        // Setup END PLACE
        place = new Place(setName("E"),0);                                                // create Start place
        quest.add(place);

        //Setup Arc Current T -> New P
        arc = new Arc(setName("A"), transition, place);
        quest.add(arc);
    }

    private void printLists(){

        Log.d("Quest", "---------> Print Lists");

        Log.d("Quest printLists: " , "#Actions: " + questListAbstract.size());
        for(int r = 0; r < questListAbstract.size(); r++) {
            Log.d("Quest printLists" , " Action ID: " + questListAbstract.get(r).getActionID()+ " Action Name: " + questListAbstract.get(r).getActionName());
        }

        Log.d("Quest pL: " , "#Used Rules: : " + appliedRulesList.size());
        for(int q = 0; q < appliedRulesList.size(); q++) {
            Log.d("Quest pL" , " Action ID: " + appliedRulesList.get(q));
        }

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

    private void setGameObject(Integer id){

        Action action = new Action(12);

        //Set a suiting game Object, accoring to the action
        //(cannot set game object for <get> as a place, this is not possible)

        if(id == 6 || id == 8 || id == 13 || id == 14 || id == 19 || id == 20 || id == 29 || id == 1 || id == 24 || id == 21){
            // experiment, gather, read, repair, use, get, <get>, <capture> capture

            //Itemuse describes a specific type an item must have, in order to be used with the action
            //This value is stored in the itemtype --> itemuses as a String value separated by ","
            //Log.d("GameLQ Quest",  "Action: " + action.getActionName() + " Itemuse " + action.getItemuse());


        }else if (id == 7 || id == 10 || id == 25 || id == 23 || id == 30 ){
            // explore, goto, learn, <goto> <learn> <steal>
            action.addGameObject(game.getLocation());

        }else if (id == 4 || id == 1 || id == 11 || id == 12 || id == 15 || id == 17 || id == 26 || id == 24 ){
            // CAPTURE ESCORT KILL  LISTEN  REPORT STEALTH <kill> <capture>
            action.addGameObject(game.getNPC());

        }else if(id == 16 || id == 27){
            //SPY
            switch((int)(Math.random()*2)){
                case 0:
                    action.addGameObject(game.getLocation());
                    break;
                case 1:
                    action.addGameObject(game.getNPC());
                    break;
            }
        }else if(id == 5 || id == 9 || id == 18){
            // exchange, give, take
            action.addGameObject(game.getNPC());
            action.addGameObject(game.getItem());
        }else if(id == 2 || id == 3){
            // Damage, defend
            switch((int)(Math.random()*3)){
                case 0:
                    action.addGameObject(game.getLocation());
                    break;
                case 1:
                    action.addGameObject(game.getNPC());
                    break;
                case 2:
                    action.addGameObject(game.getItem());
                    break;
            }
        }
    }

    public void generateAbstractQuestDescription(){

        printLists();
        boolean returnToQuestGiver = false;
        int s = questListAbstract.size();

        //Adress Player
        abstractDesctription = "Hey, my name is " + questGiver.getName() + ". I'd like you to do me a favour. Could you please ";

        if(questListAbstract.size()>=2)
        {
            if(questListAbstract.get(questListAbstract.size() - 2).getActionID()== 10) // <GOTO>
            {
                returnToQuestGiver = true;
            }
        }


        for(Action action: questListAbstract){

            action.setDescription();

            // List has only 1 entry
            if(s < 3){
                // add new action to description
                abstractDesctription = abstractDesctription + action.getDescription();

                if(s == 2 && action != questListAbstract.get(s-1)){
                    // if list has 2 entrys and action is not the last action, add an "and"
                    abstractDesctription = abstractDesctription + " and ";
                }
            }else if(returnToQuestGiver == false){
                // Player has not to return to quest giver
                abstractDesctription = abstractDesctription + action.getDescription();

                if(action != questListAbstract.get(s-1) || action != questListAbstract.get(s-2) || action != questListAbstract.get(s)){
                    abstractDesctription = abstractDesctription + " and ";
                }

                if(action == questListAbstract.get(s-2)){
                    abstractDesctription = abstractDesctription + ". At last ";
                }

            }else if(action != questListAbstract.get(s-1) && action != questListAbstract.get(s-2) ){
                abstractDesctription = abstractDesctription + action.getDescription();

                if(action != questListAbstract.get(s-3)){
                    abstractDesctription = abstractDesctription + " and ";
                }

                } else if (action == questListAbstract.get(s-1)) {
                    abstractDesctription = abstractDesctription + ". If you are done, come back to " + game.getLocation(questGiver).getName() + " and " + action.getDescription();
                }
            }
        abstractDesctription = abstractDesctription + ".";
        Log.d("SetDescription", "Final: " + abstractDesctription);
    }

    public void printPetri() {

        //Log.d("Quest printPetri", "---> Places: " + quest.getPlaces().size());
        //for (int c = 0; c < quest.getPlaces().size(); c++){
         //   Log.d("Quest printPetri", quest.getPlaces().get(c).getName() + " Tokens: " + quest.getPlaces().get(c).getTokens());
        //}

        Log.d("Quest printPetri", "---> Transitions: " + quest.getTransitions().size());
        for (Transition t : quest.getTransitions()){
            Log.d("Quest printPetri", t.getName() + " - " + t.getAction().getActionName());
            Log.d("Quest printPetri",  "    @ " + t.getAction().getGameObject().getName());
        }

        Log.d("Quest printPetri", "---> Transitions able to fire: " + quest.getTransitionsAbleToFire().size());
        for (Transition t : quest.getTransitionsAbleToFire()){
            Log.d("Quest printPetri", t.getName() +  " - "  + t.getAction().getActionName());
        }

        //Log.d("Quest printPetri", "---> Arcs: " + quest.getArcs().size());
        //for (Arc a : quest.getArcs()){

          //  if(a.getOrientation().equals("TRANSITION_TO_PLACE")){
            //    Log.d("Quest printPetri", a.getName() + " From  " + a.getTransition().getName() + " To " + a.getPlace().getName());
            //}else{
             //   Log.d("Quest printPetri", a.getName() + " From  " + a.getPlace().getName() + " To " + a.getTransition().getName());
            //}
        //}


    }

    private void simulatePetri(){

        Log.d("Quest Simulate", "---------> Simulate Petri Net");
        Log.d("Quest Simulate", "Player Start Pos: " + game.getLocation(player).getName());
        Log.d("Quest Simulate", "Quest Giver: " + questGiver.getName());
        Log.d("Quest Simulate", "Quest Motivation: " + motivationName);
        Log.d("Quest Simulate", "Quest Strategy: " + strategyName);
        Log.d("Quest Simulate", "Quest Depth: " + questDepth);
        Log.d("Quest Simulate", "0   1   2   3   4   5   6");
        Log.d("Quest Simulate", "|   |   |   |   |   |   |");

        while(quest.getTransitionsAbleToFire().size()>0){
            for(Transition t: quest.getTransitionsAbleToFire()){
                t.fire();
                String type ="";

                if(t.isMerge() == true){
                    type = type + " MERGE";
                }

                if(t.isSplit() == true){
                    type = type + " SPLIT";
                }

                if(t.getDepth() == 0)
                {
                    Log.d("Quest Simulate", t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }
                else if(t.getDepth() == 1)
                {
                    Log.d("Quest Simulate", "|   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }
                else if(t.getDepth() == 2)
                {
                    Log.d("Quest Simulate", "|   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }
                else if(t.getDepth() == 3)
                {
                    Log.d("Quest Simulate", "|   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }
                else if(t.getDepth() == 4)
                {
                    Log.d("Quest Simulate", "|   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 5)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 6)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 7)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 8)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 9)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 10)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else if(t.getDepth() == 11)
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }else
                {
                    Log.d("Quest Simulate", "|   |   |   |   |   |   |   |   |   |   |   |   " + t.getName() + " - " + t.getAction().getActionName() + " " + t.getAction().getGameObject().getName());
                }


            }
        }
    }

    public Petrinet getQuest(){
        return quest;
    }

    public void printRewrittenActions(){

        Log.d("Quest", " -----> Print Rewritten Actions ");

        for(Transition tRa:  quest.getTransitions()){
            if( tRa.getAction().getIsActionRewritten() ==true ){
                Log.d("Quest printRewrittenAct", "Transition " + tRa.getName() + " - "  + tRa.getAction().getActionName());
            }
        }

    }

    public String getAbstractDescription(){
        return abstractDesctription;
    }

    public String getStrategyName(){
        return strategyName;
    }

    public NPC getQuestGiver(){
        return questGiver;
    }

    public String getDialogue(){

        String qgName = questGiver.getName();
        String qgHome = questGiver.getHomeSTRING();



        return qgName;
    }

    public String getMotivationName(){
        return motivationName;
    }

}
