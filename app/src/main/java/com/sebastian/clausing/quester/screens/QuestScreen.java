package com.sebastian.clausing.quester.screens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.game.GameManager;
import com.sebastian.clausing.quester.helper.Evaluation;
import com.sebastian.clausing.quester.petriNet.Petrinet;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Action;
import com.sebastian.clausing.quester.questGen.Quest;

import java.io.IOException;
import java.io.PrintWriter;

import static com.sebastian.clausing.quester.R.id.toolbar;

public class QuestScreen extends FragmentActivity implements FragmentQuest.MessageFlow {

    //Fragments
    private FragmentManager fm = getFragmentManager();
    private FragmentQuest fragmentQuest;
    private FragmentAction fragmentAction;
    private static final String TAG_FRAGMENT_QUEST = "fragmentQuest";
    private static final String TAG_FRAGMENT_ACTION = "fragmentAction";

    //Objects
    private GameManager objGameL;
    private Quest objQuest;
    private Petrinet pnQuest;
    private String motivation;
    private boolean kill = false;
    private Evaluation objEvaluation = new Evaluation();

    //UI
    private Button showActions;
    Toolbar  toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_screen);

        //SET TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.toolbar_quest_screen);
        toolbar.setTitle("Quest Description");

        //Create new Quest
        Intent intent = getIntent();
        motivation = intent.getStringExtra("motivation");

        objGameL = new GameManager();
        objQuest = new Quest(objGameL, motivation);

        pnQuest = objQuest.getQuest();

        //evaluate();

        //find the retained fragments on activity start
        Log.d("QuestScreen onCreate" , "Set fm");
        fragmentQuest = (FragmentQuest) fm.findFragmentByTag(TAG_FRAGMENT_QUEST);


        if (fragmentQuest == null) {
            // addGameObject the fragment
            Log.d("QuestScreen onCreate" , "create fQuest for first time");
            fragmentQuest = (FragmentQuest) Fragment.instantiate(this, FragmentQuest.class.getName(), null);
            fm.beginTransaction().add(fragmentQuest, TAG_FRAGMENT_QUEST).commit();

            //Show Fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
            fragmentTransaction.commit();
        }

        fragmentQuest.setObjects(objGameL, objQuest);

        showActions = (Button) findViewById(R.id.btn_show_actions);
        showActions.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Log.d("QuestScreen " , "Button Start Pressed");
                swapFragments();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(kill == true){
            finish();
        }else if(fragmentAction != null && fragmentQuest != null){
                if(fragmentAction.isVisible()){
                 // Swap Fragment Back to Quest Overview
                    Log.d("QuestScreen onCreate" , "Swap From Action to Quest");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
                    fragmentTransaction.commit();
                    toolbar.setTitle("Quest Description");
                    showActions.setVisibility(View.VISIBLE);
                    kill = false;
                }
                else if(fragmentQuest.isVisible()){
                    //SWap Fragment to Action
                    Log.d("QuestScreen" , "Back to Start Screen");
                    Intent myIntent = new Intent(QuestScreen.this, StartScreen.class);
                 QuestScreen.this.startActivity(myIntent);
                 kill = true;
             }
            }else{
             //SWap Start Activity
             Log.d("QuestScreen" , "Back to Start Screen");
             Intent myIntent = new Intent(QuestScreen.this, StartScreen.class);
             QuestScreen.this.startActivity(myIntent);
             kill = true;
            }
        }

    private void startQuest(){

        Transition nextTransition;

        //Check if an entry is in the list
        if(pnQuest.getTransitionsAbleToFire().size() > 0){
            //One Transition is able to fire

            //We always just have one which is able to fire
            nextTransition = pnQuest.getTransitionsAbleToFire().get(0);
            fragmentAction = (FragmentAction) fm.findFragmentByTag(TAG_FRAGMENT_ACTION);

            // create the fragment and data the first time
            if (fragmentAction == null) {
                Log.d("QuestScreen onCreate" , "create FragmentAction for first time");
                // addGameObject the fragment
                fragmentAction = (FragmentAction) Fragment.instantiate(this, FragmentAction.class.getName(), null);

                fm.beginTransaction().add(fragmentAction, TAG_FRAGMENT_ACTION).commit();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flFragmentContainer, fragmentAction);
                fragmentTransaction.commit();

                showActions.setText("Next");
                fragmentAction.setObjects(objGameL,objQuest);
            }

            //Triggers the corresponding activity in fragmentAction, to set the TextView to a value which is stored in nextTransition
            Log.d("NextTransition", nextTransition.getName() + " Action: " + nextTransition.getAction().getActionName() + " Object: " + nextTransition.getAction().getGameObject().getName());
            fragmentAction.nextAction(nextTransition);
            nextTransition.fire();


        }
        else{
            // Create new Quest
            objQuest = new Quest(objGameL, motivation);
            pnQuest = objQuest.getQuest();

            // Send new Quest to the fragments
            fragmentQuest.setObjects(objGameL, objQuest);
            fragmentAction.setObjects(objGameL,objQuest);

            // Change Button back to start
            showActions.setText("Start Quest");

            // Replace Fragments
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
            fragmentTransaction.commit();

        }
    }


    private void swapFragments(){
        //Swaps between player actions and quest description
        // create the fragment and data the first time

        if (fragmentAction == null) {
            Log.d("QuestScreen onCreate" , "create FragmentAction for first time");
            //Swap Fragment to Action

            fragmentAction = (FragmentAction) Fragment.instantiate(this, FragmentAction.class.getName(), null);

            fm.beginTransaction().add(fragmentAction, TAG_FRAGMENT_ACTION).commit();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentAction);
            fragmentTransaction.commit();

            showActions.setVisibility(View.INVISIBLE);
            fragmentAction.setObjects(objGameL,objQuest);
            toolbar.setTitle("Player Actions");

        }
        //else if(fragmentAction.isVisible()){
          // Swap Fragment Back to Quest Overview
          //  Log.d("QuestScreen onCreate" , "Swap From Action to Quest");
          //  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
          //  fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
          //  fragmentTransaction.commit();

          //  showActions.setText("Show Actions");

        //}
        else if(fragmentQuest.isVisible()){
            //SWap Fragment to Action
            Log.d("QuestScreen onCreate" , "Swap From Quest to Action");
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentAction);
            fragmentTransaction.commit();
            showActions.setVisibility(View.INVISIBLE);
            toolbar.setTitle("Player Actions");
            //showActions.setText("Show Quest");
        }

    }

    public void message(String prmMessage) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        if(prmMessage!=null){
            Log.d("QuestScreen " , "Petrinet transmission of '"  +prmMessage+  "' complete.");
        }
        else{
            Log.d("QuestScreen " , "Petrinet transmission failed.");
        }
    }

    private void evaluate(){
        ///////////////////////////////////


        boolean ev = false;
        int loop = 100;

        Log.d("DEMOsc","--------------");


        if(ev==true){
            for(int i = 1; i <= loop; i++){

                objGameL = new GameManager();
                objQuest = new Quest(objGameL, motivation);

                pnQuest = objQuest.getQuest();
                objEvaluation.count(objQuest);

                Log.d("DEMOsc","Quest " + i);
                Log.d("DEMOsc","--------------");
                Log.d("DEMOsc","Motivation:  " + objQuest.getQuestGiver().getName() + " from " + objGameL.getLocation(objQuest.getQuestGiver()).getName() + " seeks for " + objQuest.getMotivationName() +  " and asks you to " + objQuest.getStrategyName() + ".");
                Log.d("DEMOsc","Description: " + objQuest.getAbstractDescription());
                Log.d("DEMOsc","Actions: ");

                for(int u = 0; u < objQuest.getActions().size() ; u++){
                    Log.d("DEMOsc","   " + objQuest.getActions().get(u));
                }

                Log.d("DEMOsc","--------------");


                // if(i == loop*0.1 || i == loop*0.2 || i == loop*0.3 || i == loop*0.4 || i == loop*0.5 || i == loop*0.6 || i == loop*0.7 || i == loop*0.8 || i == loop*0.9 || i == loop){
                //   Log.d("Evaluation" , "Fortschritt: " + i);
                // }

            }
        }else{
            objQuest = new Quest(objGameL, motivation);
            pnQuest = objQuest.getQuest();
            objEvaluation.count(objQuest);
        }



        objEvaluation.output();
        objEvaluation.calculateAvgDepth();

        ////////////////////////////

    }

}
