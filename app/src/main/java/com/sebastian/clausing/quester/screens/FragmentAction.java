package com.sebastian.clausing.quester.screens;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Quest;

import java.util.ArrayList;

public class FragmentAction extends Fragment {

    private TextView txtYourQuest;
    private Activity mActivity;
    private String test;
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("FragmentAction " , "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // CREATE VIEWS HERE
        Log.d("FragmentAction " , "onCreateView");
        View layout = inflater.inflate(R.layout.content_action_fragment, container, false);
        txtYourQuest = (TextView) layout.findViewById(R.id.txtV_Your_Quest);
        txtYourQuest.setText(test);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("FragmentAction " , "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        Log.d("FragmentAction " , "onAttach");
       }

    public void nextAction(Transition prmTransition){
        Log.d("FragmentAction " , "nextAction");
        position++;

        if(prmTransition.getAction().getActionName()!=null){
            test = ("Your " + position + ". action " + " is to:\n "
                    + prmTransition.getAction().getActionName() + "\n Something");
        }
        else{
            test = "Congratulations! \nYou finished your quest.";
        }

        if(mActivity!=null){
            FrameLayout container = (FrameLayout) mActivity.findViewById(R.id.flFragmentContainer);
            LayoutInflater.from(getActivity())
                    .inflate(R.layout.content_action_fragment, container, false);
            txtYourQuest.setText(test);
        }

    }
}
