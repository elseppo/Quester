package com.sebastian.clausing.quester.screens;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.game.GameLogic;
import com.sebastian.clausing.quester.helper.DataBaseHelper;
import com.sebastian.clausing.quester.questGen.Quest;

public class FragmentQuest extends Fragment {

    //Message Handling
    private MessageFlow mCallback;

    //Objects
    private GameLogic objGameL;
    private Quest objQuest;

    //UI
    private TextView txtVQuestDescription;
    private TextView txtVQuestStrategy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // CREATE VIEWS HERE

        View layout = inflater.inflate(R.layout.content_quest_fragment, null);
        txtVQuestDescription = (TextView) layout.findViewById(R.id.txtV_QuestDescription);
        txtVQuestStrategy = (TextView) layout.findViewById((R.id.txtV_Strategy));

        txtVQuestDescription.setText(objQuest.getAbstractDescription());
        txtVQuestStrategy.setText(randomDescription());

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


    public void setObjects(GameLogic prmGameL, Quest prmQuest){

        this.objQuest = prmQuest;
        this.objGameL = prmGameL;
    }

    private String randomDescription(){
        String text = "";

        String giverName = objQuest.getQuestGiver().getName();
        String home = objQuest.getQuestGiver().getHomeSTRING();


        switch((int)  (Math.random() * 2)){

            case 0:
                text = giverName + " asks you, to " + objQuest.getStrategyName() +  "\n After you finished the quest, return to me at " + home;
                break;

            case 1:
                text = "Hey, my name is " + giverName + ". I'd like you to do me a favour. Could you please " + objQuest.getAbstractDescription() + " and than meet me at "+ home;
                break;



        }



        return text;
    }



}
