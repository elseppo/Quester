package com.sebastian.clausing.quester.questGen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;


import com.sebastian.clausing.quester.game.GameLogic;
import com.sebastian.clausing.quester.game.GameObject;
import com.sebastian.clausing.quester.game.Item;
import com.sebastian.clausing.quester.game.Location;
import com.sebastian.clausing.quester.game.NPC;
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
    boolean foundRewritableAction;


    //Lists
    private ArrayList<Integer> abstractQuestList = new ArrayList<Integer>();
    private ArrayList<Integer> appliedRulesList = new ArrayList<>();
    private ArrayList<Action> questListAbstract = new ArrayList<Action>();

    private int strategyID;
    private int motivationID;

    private String strategyName;
    private String motivationName;
    private String strategySequence;

    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    //Petrinet
    private Petrinet quest = new Petrinet("Quest");

    //GameWorld
    GameLogic game;

    public Quest(GameLogic prmGame){

        this.game = prmGame;

        Log.d("Quest", "--- NEW QUEST -------------------------------------------------");

        createAbstractQuest();

        abstractPetriNet();


        //findSplitMerge Method is used, to
        //  1. Find a Transition which can be rewritten
        //  2. Apply the rule to that transition
        //  3. Add that rewritten Transitions to the Petrinet
        //  As long as there are rewritable actions, do

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
        //printPetri();
        //simulatePetri();

        Log.d("Quest", "---------> EXIT QUEST");
    }

    private void determineMotivation(){
        //Determine Motivation
        int totalMotivations = 9;
        motivationID=(int) (Math.random()* totalMotivations); // Possible Outcomes from 0 to (totalMotivations -1)
    }

    private void determineStrategy(String prmMotivation){

        // Variables
        motivationName = prmMotivation;
        Cursor c;
        int totalStrategies;

        //Open DB
        Log.d("Quest", "----------> OpenDB");
        questerDB = dbHelper.getStaticDb();

        // Determine how many Strategies the Motivation has
        c = questerDB.rawQuery("SELECT Count(*) FROM " + motivationName+";", null);
        c.moveToFirst();
        totalStrategies = c.getInt(0);

        //Determine a random Strategy
        strategyID =  (int) (Math.random() * totalStrategies); // Possible Outcomes from 0 to (totalStrategies -1)

        //Set Strategy Name and strategy sequence
        c = questerDB.rawQuery("SELECT * FROM " + "'"+motivationName +"'"+  "WHERE _id =  " +"'"+ strategyID +"';" , null);
        c.moveToFirst();
        strategyName = c.getString(1);
        strategySequence = c.getString(2);

        Log.d("Quest", "---------> Name " + strategyName);
        Log.d("Quest", "---------> Sequence " + strategySequence);

        // Determine the sequence of actions and put them into a integer list
        String sequence[] = strategySequence.split(",");

        for(String s:sequence){
            abstractQuestList.add(Integer.parseInt(s));
        }

        //Get a Strategy
        String strategy[] = strategyName.split(";");
        strategyName = strategy[(int) (Math.random() * (strategy.length-1))];

        Log.d("Quest", "---------> Close DB");
        questerDB.close();

    }

    private void createAbstractQuest(){

        // Determine the Quests Motivation
        determineMotivation();

        // Determine Strategy According to Motivation ID
        switch (motivationID){
            case 0:
                //Knowledge
                determineStrategy("Knowledge");
                break;
            case 1:
                //Comfort
                determineStrategy("Comfort");
                break;
            case 2:
                //Reputation
                determineStrategy("Reputation");
                break;

            case 3:
                //Serenity
                determineStrategy("Serenity");
                break;

            case 4:
                //Protection
                determineStrategy("Protection");
                break;

            case 5:
                //Conquest
                determineStrategy("Conquest");
                break;


            case 6:
                //Wealth
                determineStrategy("Wealth");
                break;

            case 7:
                //Ability
                determineStrategy("Ability");
                break;

            case 8:
                //Equipment
                determineStrategy("Equipment");
                break;
        }

        // Fill questListAbstract with Actions
        for(int c = 0; c < abstractQuestList.size(); c++){
            questListAbstract.add(new Action(countAction.increment(), abstractQuestList.get(c)));
        }

        //Set game objects
        for(Action a: questListAbstract){
            setGameObjects(a);
            Log.d("Set Game Object", a.getActionName() + " " + a.getObject());
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

        ArrayList<Action> newActionsList = new ArrayList<>();
        newActionsList.clear();

        Log.d("Quest applyRules: " , "-----> Check " + prmTSplit.getName() + " Action: " + prmTSplit.getAction().getActionName());

        int c = prmTSplit.getAction().getActionID();
        GameObject gOBJ = prmTSplit.getAction().getObject();
        // For each action ID, there are several Rules how to rewrite them

        switch (c) {
            case 1: // <capture>
                Log.d("Quest applyRules: " ,"Rule 17: [<capture> --> <get>, <goto>, capture]");
                appliedRulesList.add(17);

                newActionsList.add(new Action(countAction.increment() ,20, game.getItem())); //<get>
                newActionsList.add(new Action(countAction.increment() ,10, game.getLocation())); //<goto>
                newActionsList.add(new Action(countAction.increment() ,24, gOBJ)); //capture
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 10: // <goto>
                switch ((int) (Math.random() * 3)) {
                    case 0:
                        Log.d("Quest applyRules: " ,"Rule 3: [<goto> --> goto]");
                        appliedRulesList.add(3);

                        newActionsList.add(new Action(countAction.increment() ,25, gOBJ));   // goto
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        Log.d("Quest applyRules: " ,"Rule 4: [<goto> --> explore]");
                        appliedRulesList.add(4);

                        newActionsList.add(new Action(countAction.increment() ,7, gOBJ));    //explore

                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(5);
                        Log.d("Quest applyRules: " ,"Rule 5: [<goto> --> <learn>, goto]");
                        newActionsList.add(new Action(countAction.increment() ,23, gOBJ));   //<learn>
                        newActionsList.add(new Action(countAction.increment() ,25, gOBJ)); //goto
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 11: // <kill>
                appliedRulesList.add(18);
                Log.d("Quest applyRules: " ,"Rule 18: [<kill> --> <goto>, kill]");
                newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                newActionsList.add(new Action(countAction.increment() ,25, gOBJ)); //kill
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 16: // <spy>
                appliedRulesList.add(16);
                Log.d("Quest applyRules: " ,"Rule 16: [<spy> --> <goto>, spy, <goto>, report]");
                newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                newActionsList.add(new Action(countAction.increment() ,27, gOBJ)); //spy
                newActionsList.add(new Action(countAction.increment() ,10, game.getItem())); //<goto>           <-- ÄNDERN
                newActionsList.add(new Action(countAction.increment() ,15, game.getNPC())); //report            <-- ÄNDERN
                rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                break;

            case 20: // <get>
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        appliedRulesList.add(10);
                        Log.d("Quest applyRules: " ,"Rule 10: [<get> --> get]");
                        newActionsList.add(new Action(countAction.increment() ,29, gOBJ));   //get
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(11);
                        Log.d("Quest applyRules: " ,"Rule 11: [<get> --> <steal>]");
                        newActionsList.add(new Action(countAction.increment() ,21, gOBJ));   //<steal>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(12);
                        Log.d("Quest applyRules: " ,"Rule 12: [<get> --> <goto>, <gather>]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,8, gOBJ));   //<gather>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 3:
                        appliedRulesList.add(13);
                        Log.d("Quest applyRules: " ,"Rule 13: [<get> --> <goto>, <get>, <goto>, <subquest>, exchange]");

                        Item giveItem = game.getItem();
                        NPC exchangeNPC = game.getNPC();

                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,20, giveItem)); //<get>
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation())); //<goto>
                        newActionsList.add(new Action(countAction.increment() ,22, game.getPlaceholder())); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,5, exchangeNPC, giveItem, gOBJ)); //exchange
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 21: // <steal>
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        appliedRulesList.add(14);
                        Log.d("Quest applyRules: " ,"Rule 14: [<steal> --> <goto>, stealth, take]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,17, game.getNPC())); //stealth
                        newActionsList.add(new Action(countAction.increment() ,18, gOBJ)); //take
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(15);
                        Log.d("Quest applyRules: " ,"Rule 15: [<steal> --> <goto>, <kill>, take]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,11, game.getLocation())); //<kill>
                        newActionsList.add(new Action(countAction.increment() ,18, gOBJ)); //take
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 22: // <subquest>
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        appliedRulesList.add(1);
                        Log.d("Quest applyRules: " ,"Rule 1: [<subquest> --> <goto>]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(2);
                        Log.d("Quest applyRules: " ,"Rule 2: [<subquest> --> <goto>, <Quest>, <goto>]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,28, game.getPlaceholder())); //<Quest>
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation())); //<goto>
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            case 23: // <learn>
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        appliedRulesList.add(6);
                        Log.d("Quest applyRules: " ,"Rule 6: [<learn> --> learn]");
                        newActionsList.add(new Action(countAction.increment() ,30, game.getLocation()));   //learn
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 1:
                        appliedRulesList.add(7);
                        Log.d("Quest applyRules: " ,"Rule 7: [<learn> --> <goto>, <subquest>, listen]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,22, game.getPlaceholder())); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,12, game.getNPC())); //listen
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 2:
                        appliedRulesList.add(8);
                        Log.d("Quest applyRules: " ,"Rule 8: [<learn> --> <goto>, <get>, read]");
                        newActionsList.add(new Action(countAction.increment() ,10, game.getLocation()));   //<goto>
                        newActionsList.add(new Action(countAction.increment() ,20, gOBJ)); //<get>
                        newActionsList.add(new Action(countAction.increment() ,13, gOBJ)); //read
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                    case 3:
                        appliedRulesList.add(9);
                        Log.d("Quest applyRules: " ,"Rule 9: [<learn> --> <get> <subquest>, give, listen]");

                        Item getItem = new Item();

                        newActionsList.add(new Action(countAction.increment() ,20, getItem));   //<get>
                        newActionsList.add(new Action(countAction.increment() ,22, game.getPlaceholder())); //<subquest>
                        newActionsList.add(new Action(countAction.increment() ,9, getItem)); //give
                        newActionsList.add(new Action(countAction.increment() ,12,game.getItem())); //listen
                        rewritePetrinet(prmTSplit,prmTMerge,newActionsList);

                        break;
                }
                break;

            default:
                Log.d("Quest applyRules: " ,"-----> No change to:" + prmTSplit.getName()) ;
                break;
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

        //Log.d("Quest rewritePetrinet", "Print new Petrinet");
    }

    private void abstractPetriNet(){

        Transition transition;
        Arc arc;
        Place place;

        place = new Place(setName("S"),1);                                                // create Start place
        quest.add(place);

        for(Action a: questListAbstract){

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

    private void setGameObjects(Action prmAction){

        int id = prmAction.getActionID();

        if(id == 6 || id == 8 || id == 13 || id == 14 || id == 19 || id == 20 || id == 29){
            // experiment, gather, read, repair, use, get, <get>
            prmAction.add(game.getItem());
        }else if (id == 7 || id == 10 || id == 25 || id == 23 || id == 30 || id == 21){
            // explore, goto, learn, <goto> <learn> <steal>
            prmAction.add(game.getLocation());
        }else if (id == 4 || id == 1 || id == 11 || id == 12 || id == 15 || id == 17 || id == 26 || id == 24 ){
            // CAPTURE ESCORT KILL  LISTEN  REPORT STEALTH <kill> <capture>
            prmAction.add(game.getNPC());
        }else if(id == 16 || id == 27){
            //SPY
            switch((int)(Math.random()*2)){
                case 0:
                    prmAction.add(game.getLocation());
                    break;
                case 1:
                    prmAction.add(game.getNPC());
                    break;
            }
        }else if(id == 5 || id == 9 || id == 18){
            // exchange, give, take
            prmAction.add(game.getNPC());
            prmAction.add(game.getItem());
        }else if(id == 2 || id == 3){
            // Damage, defend
            switch((int)(Math.random()*3)){
                case 0:
                    prmAction.add(game.getLocation());
                    break;
                case 1:
                    prmAction.add(game.getNPC());
                    break;
                case 2:
                    prmAction.add(game.getItem());
                    break;
            }
        }
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
                Log.d("Quest Simulate", "Fire Transition: " + t.getName() + " - " + t.getAction().getActionName());
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
        Log.d("Quest", " -----> Create Description of Abstract Quest");

        String description = "";

        for(Action a: questListAbstract)
        {
            description = description + a.getActionName() + " ";
        }

        return description;
    }

    public String getStrategyName(){
        return strategyName;
    }

}
