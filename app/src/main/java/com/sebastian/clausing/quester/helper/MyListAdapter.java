package com.sebastian.clausing.quester.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sebs-Desktop on 05.08.2017.
 */

public class MyListAdapter extends ArrayAdapter<String> {

    public MyListAdapter(Context context, int resource, ArrayList<String> list) {
        super(context, resource, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = (TextView) super.getView(position, convertView, parent);

       if(getItem(position).contains(">")){
          textView.setTypeface(null, Typeface.ITALIC);
           textView.setTextSize(15);
       }else{
           textView.setTypeface(null, Typeface.BOLD);
           textView.setTextSize(15);
       }



        return textView;
    }


   // @Override
    //public View getView(int position, View convertView, ViewGroup parent) {

      //  View v= super.getView(position, convertView, parent);
       // if (getItem(position).contains(">")) {
        //}else{
         //   v.setBackgroundColor(Color.parseColor("#4D137E64"));
        //}
        //return v;
   // }
}
