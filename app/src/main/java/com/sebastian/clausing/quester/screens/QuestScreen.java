package com.sebastian.clausing.quester.screens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.game.GameLogic;
import com.sebastian.clausing.quester.petriNet.Petrinet;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Quest;

public class QuestScreen extends FragmentActivity implements FragmentQuest.MessageFlow {

    //Fragments
    private FragmentManager fm = getFragmentManager();
    private FragmentQuest fragmentQuest;
    private FragmentAction fragmentAction;
    private static final String TAG_FRAGMENT_QUEST = "fragmentQuest";
    private static final String TAG_FRAGMENT_ACTION = "fragmentAction";

    //Objects
    private GameLogic objGameL = new GameLogic();
    private Quest objQuest = new Quest(objGameL);
    private Petrinet pnQuest = objQuest.getQuest();

    //UI
    private Button btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_screen);

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

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Log.d("QuestScreen " , "Button Start Pressed");
                startQuest();
            }
        });
    }

    private void startQuest(){

        Transition nextTransition;

        //Check if an entry is in the list
        if(pnQuest.getTransitionsAbleToFire().size() > 0){
            //Transition is able to fire

            nextTransition = pnQuest.getTransitionsAbleToFire().get(0);
            fragmentAction = (FragmentAction) fm.findFragmentByTag(TAG_FRAGMENT_ACTION);

            //But we want only actions, that are NOT REWRITTEN
            //Rewritten actions are just indirect handled, via the sub-actions
            if(nextTransition.getAction().getIsActionRewritten() == false)
            {

                // create the fragment and data the first time
                if (fragmentAction == null) {
                    Log.d("QuestScreen onCreate" , "create FragmentAction for first time");
                    // addGameObject the fragment
                    fragmentAction = (FragmentAction) Fragment.instantiate(this, FragmentAction.class.getName(), null);

                    fm.beginTransaction().add(fragmentAction, TAG_FRAGMENT_ACTION).commit();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flFragmentContainer, fragmentAction);
                    fragmentTransaction.commit();

                    btnStart.setText("NEXT");
                    fragmentAction.setObjects(objGameL,objQuest);
                }

                //Triggers the corresponding activity in fragmentAction, to set the TextView to a value which is stored in nextTransition
                fragmentAction.nextAction(nextTransition);
                nextTransition.fire();
            }
            else{
                nextTransition.fire();
                //Recursive Call of the Method, TO "Step over the rewritten action"
                startQuest();
            }








        }
        else{
            // Create new Quest
            objQuest = new Quest(objGameL);
            pnQuest = objQuest.getQuest();

            // Send new Quest to the fragments
            fragmentQuest.setObjects(objGameL, objQuest);
            fragmentAction.setObjects(objGameL,objQuest);

            // Change Button back to start
            btnStart.setText("Start Quest");

            // Replace Fragments
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
            fragmentTransaction.commit();

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

}
