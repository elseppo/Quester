package com.sebastian.clausing.quester.screens;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Action;
import com.sebastian.clausing.quester.questGen.Quest;

import java.lang.reflect.Field;
import java.util.UUID;

public class FragmentQuest extends Fragment {

    private MessageFlow mCallback;
    private Quest questOBJ;
    private TextView txtVQuestDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("FragQuest onCreate", "Create Quest");
        createQuest();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // CREATE VIEWS HERE
        Log.d("FragQuest onCreateView", "Start");

        View layout = inflater.inflate(R.layout.content_quest_fragment, null);
        txtVQuestDescription = (TextView) layout.findViewById(R.id.txtV_QuestDescription);
        txtVQuestDescription.setText(questOBJ.getAbstractDescription());
        Log.d("FragQuest onCreateView", "mCallback");
        mCallback.setQuest(questOBJ);
        Log.d("FragQuest onCreateView", "End");
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // Container Activity must implement this interface
    public interface MessageFlow {
        public void setQuest(Quest QuestOBJ);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (MessageFlow) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    private void createQuest(){
        Log.d("FragQuest createQuest", "---------> Start");

        questOBJ = new Quest();
        Log.d("FragQuest crateQuest", "---------> Exit");
    }



}
