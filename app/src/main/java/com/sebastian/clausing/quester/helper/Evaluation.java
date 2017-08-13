package com.sebastian.clausing.quester.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sebastian.clausing.quester.petriNet.Petrinet;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Action;
import com.sebastian.clausing.quester.questGen.Quest;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 09.08.2017.
 */

public class Evaluation {

    private Quest quest;
    private Petrinet pn;
    private DataBaseHelper dbHelper = new DataBaseHelper();
    private SQLiteDatabase questerDB;

    private ArrayList<Integer> actions = new ArrayList<>();
    private ArrayList<Integer> knowledge = new ArrayList<>();
    private ArrayList<Integer> comfort = new ArrayList<>();
    private ArrayList<Integer> reputation = new ArrayList<>();
    private ArrayList<Integer> serenity = new ArrayList<>();
    private ArrayList<Integer> protection = new ArrayList<>();
    private ArrayList<Integer> conquest = new ArrayList<>();
    private ArrayList<Integer> wealth = new ArrayList<>();
    private ArrayList<Integer> ability= new ArrayList<>();
    private ArrayList<Integer> equipment= new ArrayList<>();
    private ArrayList<Integer> rules = new ArrayList<>();
    private ArrayList<Integer> depth = new ArrayList<>();

    private int avgDepth;




    public Evaluation (){
        initializeLists();
    }

    private void countActions(){


        while(pn.getTransitionsAbleToFire().size()>0){

            for(Transition t: pn.getTransitionsAbleToFire()){

                int c = actions.get(t.getAction().getActionID()) + 1;

                actions.remove(t.getAction().getActionID());
                actions.add(t.getAction().getActionID(),c);

                //Log.d("Evaluation","Added: " + t.getAction().getActionName() + " to list pos: " + t.getAction().getActionID() + " = " + actions.get(t.getAction().getActionID()));
                t.fire();
            }

        }

        pn.getPlaces().get(0).setTokens(1);
    }

    private void countStrategy(){

        int m,s,id;

        switch(quest.getMotivationID()){

            case 0:
                m = knowledge.get(0) + 1;
                knowledge.remove(0);
                knowledge.add(0,m);

                id = quest.getStrategyID()+1;
                s = knowledge.get(id)+1;
                knowledge.remove(id);
                knowledge.add(id,s);
                break;
            case 1:
                m = comfort.get(0) + 1;
                comfort.remove(0);
                comfort.add(0,m);

                id = quest.getStrategyID()+1;
                s = comfort.get(id)+1;
                comfort.remove(id);
                comfort.add(id,s);
                break;
            case 2:
                m = reputation.get(0) + 1;
                reputation.remove(0);
                reputation.add(0,m);

                id = quest.getStrategyID()+1;
                s = reputation.get(id)+1;
                reputation.remove(id);
                reputation.add(id,s);
                break;
            case 3:
                m = serenity.get(0) + 1;
                serenity.remove(0);
                serenity.add(0,m);

                id = quest.getStrategyID()+1;
                s = serenity.get(id)+1;
                serenity.remove(id);
                serenity.add(id,s);
                break;
            case 4:
                m = protection.get(0) + 1;
                protection.remove(0);
                protection.add(0,m);

                id = quest.getStrategyID()+1;
                s = protection.get(id)+1;
                protection.remove(id);
                protection.add(id,s);
                break;
            case 5:
                m = conquest.get(0) + 1;
                conquest.remove(0);
                conquest.add(0,m);

                id = quest.getStrategyID()+1;
                s = conquest.get(id)+1;
                conquest.remove(id);
                conquest.add(id,s);
                break;
            case 6:
                m = wealth.get(0) + 1;
                wealth.remove(0);
                wealth.add(0,m);

                id = quest.getStrategyID()+1;
                s = wealth.get(id)+1;
                wealth.remove(id);
                wealth.add(id,s);
                break;
            case 7:
                m = ability.get(0) + 1;
                ability.remove(0);
                ability.add(0,m);

                id = quest.getStrategyID()+1;
                s = ability.get(id)+1;
                ability.remove(id);
                ability.add(id,s);
                break;
            case 8:
                m = reputation.get(0) + 1;
                equipment.remove(0);
                equipment.add(0,m);

                id = quest.getStrategyID()+1;
                s = equipment.get(id)+1;
                equipment.remove(id);
                equipment.add(id,s);
                break;
        }

    }

    private void initializeLists(){

        for(int i = 0; i <= 17; i++){
            rules.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 31; i++){
            actions.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 4; i++){
            knowledge.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 2; i++){
            comfort.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 7; i++){
            serenity.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 7; i++){
            protection.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 2; i++){
            conquest.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 3; i++){
            wealth.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 7; i++){
            ability.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 8; i++){
            equipment.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

        for(int i = 0; i <= 3; i++){
            reputation.add(0);
            //Log.d("Evaluation","Nuller Liste size:" + actions.size());
        }

    }

    public void output(){
        Log.d("Evaluation","Count Strategies and Motivations -----------");

        Log.d("Evaluation","knowledge count: " + knowledge.get(0));
        for(int c = 0; c < knowledge.size(); c++){

            if(knowledge.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + knowledge.get(c));
            }

        }

        Log.d("Evaluation","comfort count: " + comfort.get(0));
        for(int c = 0; c < comfort.size(); c++){

            if(comfort.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + comfort.get(c));
            }

        }

        Log.d("Evaluation","reputation count: " + reputation.get(0));
        for(int c = 0; c < reputation.size(); c++){

            if(reputation.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + reputation.get(c));
            }

        }

        Log.d("Evaluation","serenity count: " + serenity.get(0));
        for(int c = 0; c < serenity.size(); c++){

            if(serenity.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + serenity.get(c));
            }

        }

        Log.d("Evaluation","protection count: " + protection.get(0));
        for(int c = 0; c < protection.size(); c++){

            if(protection.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + protection.get(c));
            }

        }

        Log.d("Evaluation","conquest count: " + conquest.get(0));
        for(int c = 0; c < conquest.size(); c++){

            if(conquest.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + conquest.get(c));
            }

        }

        Log.d("Evaluation","wealth count: " + wealth.get(0));
        for(int c = 0; c < wealth.size(); c++){

            if(wealth.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + wealth.get(c));
            }

        }

        Log.d("Evaluation","ability count: " + ability.get(0));
        for(int c = 0; c < ability.size(); c++){

            if(ability.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + ability.get(c));
            }

        }

        Log.d("Evaluation","equipment count: " + equipment.get(0));
        for(int c = 0; c < equipment.size(); c++){

            if(equipment.get(c)>0 && c != 0){
                Log.d("Evaluation"," - Strategy ID: " + c + " count: " + equipment.get(c));
            }

        }

        Log.d("Evaluation","Count Actions -----------");


        for(int k = 0; k < actions.size(); k++){

            //questerDB = dbHelper.getStaticDb();
            //Cursor c;

            // GET Action NAME
            //c = questerDB.rawQuery("SELECT name FROM Actions WHERE _id = " + k + ";", null);
            //c.moveToFirst();
            //questerDB.close();

            if(actions.get(k)>0){
                Log.d("Evaluation","Action ID: " + k + " count: " + actions.get(k));
            }


        }


        //Log.d("Evaluation","Quest Rules -----------");

        //Log.d("Evaluation" , "Used Rules: " + quest.getAppliedRulesList().size());
        //for(int q = 0; q < quest.getAppliedRulesList().size(); q++) {
        //    Log.d("Evaluation" , " Rule ID: " + quest.getAppliedRulesList().get(q));
        //}

        Log.d("Evaluation","Count Rules -----------");


        for(int k = 0; k < rules.size(); k++){

            if(rules.get(k)>0){
                Log.d("Evaluation","Rule ID: " + k + " count: " + rules.get(k));
            }


        }


    }

    private void countRules(){
        int id;
        for(int i =0; i < quest.getAppliedRulesList().size();i++){

            id = quest.getAppliedRulesList().get(i);

           // Log.d("Evaluation","Rule TEST " + id);



            int c = rules.get(id);
            //Log.d("Evaluation","  Rule TEST c =   " + c);
            c = c + 1;
            //Log.d("Evaluation","  Rule TEST c+1 = " + c);

            rules.remove(id);
            rules.add(id,c);
        }

    }

    public void count(Quest prmQuest){
        this.quest =prmQuest;
        this.pn = quest.getQuest();
        countActions();
        countStrategy();
        countRules();
        countDepth();
    }

    private void countDepth(){

        //Log.d("Evaluation","Depth: "  + quest.getQuestDepth());
        depth.add(quest.getQuestDepth());
    }

    public void calculateAvgDepth(){
        int sum = 0;

        for(int i = 0; i < depth.size(); i++){
            sum = sum + depth.get(i);
        }

        avgDepth = sum / depth.size();
        Log.d("Evaluation","Quest Depth -----------");
        Log.d("Evaluation ","Average Depth = " + avgDepth);

    }


}
