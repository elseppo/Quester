package com.sebastian.clausing.quester.screens;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.game.GameManager;
import com.sebastian.clausing.quester.questGen.Quest;

public class FragmentQuest extends Fragment {

    //Message Handling
    private MessageFlow mCallback;

    //Objects
    private GameManager objGameL;
    private Quest objQuest;
    private String motivation;

    //UI
    private TextView txtVQuestDescription;
    private TextView txtVQuestHL1;
    private TextView txtVQuestMotivation;
    private TextView txtVQuestGiver;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // CREATE VIEWS HERE

        View layout = inflater.inflate(R.layout.content_quest_fragment, null);
        //txtVQuestStrategy = (TextView) layout.findViewById(R.id.txtV_Strategy);
        txtVQuestDescription = (TextView) layout.findViewById(R.id.txtV_QuestDescription);
        txtVQuestHL1 = (TextView) layout.findViewById(R.id.txtV_HL1);
        txtVQuestGiver = (TextView) layout.findViewById(R.id.txtV_QuestGiver);

        txtVQuestGiver.setText(objQuest.getQuestGiver().getName() + " from " + objGameL.getLocation(objQuest.getQuestGiver()).getName() + " seeks for " + objQuest.getMotivationName() +  " and asks you to " + objQuest.getStrategyName() + ".");
        //txtVQuestMotivation.setText("The Quest is : " + objQuest.getMotivationName());
        //txtVQuestStrategy.setText("Strategy: "+ objQuest.getStrategyName());
        txtVQuestDescription.setText(objQuest.getAbstractDescription());

        //Log.d("FragQuest onCreateView", "mCallback");
        //mCallback.setQuest(objQuest);
        //Log.d("FragQuest onCreateView", "End");
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // Container Activity must implement this interface
    public interface MessageFlow {
        public void message(String prmMessage);
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        // this.mActivity= (Activity) context;
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (MessageFlow) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    public void setObjects(GameManager prmGameL, Quest prmQuest){
        this.objQuest = prmQuest;
        this.objGameL = prmGameL;

    }





}
