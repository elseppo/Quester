package com.sebastian.clausing.quester.screens;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sebastian.clausing.quester.R;
import com.sebastian.clausing.quester.game.GameManager;
import com.sebastian.clausing.quester.helper.MyListAdapter;
import com.sebastian.clausing.quester.petriNet.Transition;
import com.sebastian.clausing.quester.questGen.Quest;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.sebastian.clausing.quester.R.id.toolbar;

public class FragmentAction extends Fragment {

    //Objects
    private GameManager objGameL;
    private Quest objQuest;

    // UI
    private TextView txtActionStep;
    private TextView txtActionDescription;
    private Activity mActivity;
    private Context mContext;
    private String actionStep;
    private String actionDescription;
    private int position = 0;
    private ListView lvActions;
    private ArrayList<String> action = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.d("FragmentAction " , "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // CREATE VIEWS HERE
        //Log.d("FragmentAction " , "onCreateView");
        Log.d("Atomic Actions: ", "test");

        View layout = inflater.inflate(R.layout.content_action_fragment, container, false);
        Log.d("Atomic Actions: ", "test2");

        lvActions = (ListView) layout.findViewById(R.id.lv_Actions);
        //txtActionStep = (TextView) layout.findViewById(R.id.txtV_Your_Quest);
        //txtActionDescription = (TextView) layout.findViewById(R.id.txtV_action_description);

        //Log.d("FragmentAction " , objQuest.getAbstractDescription().);

        Log.d("Atomic Actions: ", "test3");


        MyListAdapter arrayAdapter = new MyListAdapter(getActivity(), R.layout.list_item_action, action );
        lvActions.setAdapter(arrayAdapter);

        Log.d("Atomic Actions: ", "test4");

        //((QuestScreen) getActivity()).setBack();

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Log.d("FragmentAction " , "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mContext = context;
        //Log.d("FragmentAction " , "onAttach");


       }

    public void nextAction(Transition prmTransition){
        // Log.d("FragmentAction " , "nextAction");
        position++;

        Log.d("NextTransition", prmTransition.getName() + " Action: " + prmTransition.getAction().getActionName() + " Object: " + prmTransition.getAction().getGameObject().getName());


        if(prmTransition.getAction().getActionName()!=null){
            Log.d("FragAction - nextAction", "Current " + prmTransition.getName() + ", Action " + prmTransition.getAction().getDescription());
            actionStep = (position + ". " + prmTransition.getAction().getDescription());

            //IF Transitions has Subtasksk, i.e has been rewritten
            if(prmTransition.getChilds().size()>0){
                actionDescription = "Subtasks: \n \n";
                int c = 1;
                for(Transition t : prmTransition.getChilds()){
                    actionDescription = actionDescription + " " + c +". " + t.getAction().getDescription() + "\n";
                    c++;
                }
            }else{
                actionDescription = "No Subtasks";
            }



        }
        else{
            actionStep = "Congratulations! \nYou finished your quest.";
        }

        if(mActivity!=null){
            FrameLayout container = (FrameLayout) mActivity.findViewById(R.id.flFragmentContainer);
            LayoutInflater.from(getActivity())
                    .inflate(R.layout.content_action_fragment, container, false);
            txtActionStep.setText(actionStep);
            txtActionDescription.setText(actionDescription);
        }

        if(prmTransition.getAction().getIsActionRewritten() == true){
            actionStep = actionStep + " Applied Rule";
        }

    }

    public void setObjects(GameManager prmGameL, Quest prmQuest){
        this.objQuest = prmQuest;
        this.objGameL = prmGameL;
        this.action = objQuest.getActions();

        for(String s:action){
            Log.d("Atomic Actions setObj: ", " " + s );
        }
    }
}
