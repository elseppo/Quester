package com.sebastian.clausing.quester.screens;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.petriNet.Petrinet;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Action;
import com.sebastian.clausing.quester.questGen.Quest;

public class QuestScreen extends FragmentActivity implements FragmentQuest.MessageFlow {
    private FragmentManager fm = getFragmentManager();
    private FragmentQuest fragmentQuest;
    private FragmentAction fragmentAction;
    private Quest questOBJ;
    private Petrinet quest;
    private static final String TAG_FRAGMENT_QUEST = "fragmentQuest";
    private static final String TAG_FRAGMENT_ACTION = "fragmentAction";

    private Button btnNext;
    private Button btnBack;
    private Button btnStart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_screen);

        //find the retained fragments on activity start
        Log.d("QuestScreen onCreate" , "Set fm");
        fragmentQuest = (FragmentQuest) fm.findFragmentByTag(TAG_FRAGMENT_QUEST);

        if (fragmentQuest == null) {
            // add the fragment
            Log.d("QuestScreen onCreate" , "create fQuest for first time");
            fragmentQuest = (FragmentQuest) Fragment.instantiate(this, FragmentQuest.class.getName(), null);
            fm.beginTransaction().add(fragmentQuest, TAG_FRAGMENT_QUEST).commit();

            //Show Fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
            fragmentTransaction.commit();
        }

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Log.d("QuestScreen " , "Button Start Pressed");
                next();
            }
        });

    }

    private void next(){

        Transition nextTransition;
        //Check if an entry is in the list
        if(quest.getTransitionsAbleToFire().size() > 0){
            nextTransition = quest.getTransitionsAbleToFire().get(0);

            fragmentAction = (FragmentAction) fm.findFragmentByTag(TAG_FRAGMENT_ACTION);
            // create the fragment and data the first time
            if (fragmentAction == null) {
                Log.d("QuestScreen onCreate" , "create fAction for first time");
                // add the fragment
                fragmentAction = (FragmentAction) Fragment.instantiate(this, FragmentAction.class.getName(), null);

                fm.beginTransaction().add(fragmentAction, TAG_FRAGMENT_ACTION).commit();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flFragmentContainer, fragmentAction);
                fragmentTransaction.commit();

                btnStart.setText("NEXT");
            }

            //Triggers the corresponding activity in fragmentAction, to set the TextView to a value which is stored in nextTransition
            fragmentAction.nextAction(nextTransition);
            nextTransition.fire();
        }
        else{

            btnStart.setText("Start Quest");

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragmentQuest);
            fragmentTransaction.commit();

        }
    }


    public void setQuest(Quest prmQuestOBJ) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        questOBJ=prmQuestOBJ;
        quest = questOBJ.getQuest();

        if(questOBJ.getQuest() !=null){
            Log.d("QuestScreen " , "Petrinet transmission of '"  + questOBJ.getQuest().getName() +  "' complete.");
        }
        else{
            Log.d("QuestScreen " , "Petrinet transmission failed.");
        }
    }

}
