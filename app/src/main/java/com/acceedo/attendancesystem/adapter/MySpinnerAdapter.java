package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.acceedo.attendancesystem.R;

import java.util.ArrayList;

public class MySpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> list;
    private int defaultPosition;
    boolean toolbarspinner = false;
    boolean aartispinner = false;
    boolean settingsspinner = false;
    String screenType="";

    public int getDefaultPosition() {
        return defaultPosition;
    }

    public MySpinnerAdapter(Context context, ArrayList<String> objects) {
        super(context, 0, objects);
        this.context = context;
        list = objects;
    }

    public MySpinnerAdapter(Context context, String[] objects) {
        super(context, 0, objects);
        this.context = context;

        list = new ArrayList<String>();

        for(int i=0;i<objects.length;i++)
        {
            list.add(objects[i]);
        }
    }

    public MySpinnerAdapter(Context context, ArrayList<String> objects, boolean toolbarspinner) {
        super(context, 0, objects);
        this.context = context;
        this.toolbarspinner = toolbarspinner;
        list = objects;
    }

    public MySpinnerAdapter(Context context, ArrayList<String> objects, boolean toolbarspinner, boolean aartispinner) {
        super(context, 0, objects);
        this.context = context;
        this.toolbarspinner = toolbarspinner;
        this.aartispinner = aartispinner;
        this.list = objects;
    }

    public MySpinnerAdapter(Context context, ArrayList<String> objects, boolean toolbarspinner, String settingsspinner,String screenType) {
        super(context, 0, objects);
        this.context = context;
        this.toolbarspinner = toolbarspinner;
        this.aartispinner = aartispinner;
        this.list = objects;
        this.screenType = screenType;

        if(settingsspinner.equals("true"))
        {
            this.settingsspinner = true;
        }

    }

    public MySpinnerAdapter(Context context, String[] objects, boolean toolbarspinner) {
        super(context, 0, objects);
        this.context = context;
        this.toolbarspinner = toolbarspinner;

        list = new ArrayList<String>();

        for(int i=0;i<objects.length;i++)
        {
            list.add(objects[i]);
        }
    }

    public void setDefaultPostion(int position) {
        this.defaultPosition = position;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustom(position, convertView, parent);
    }

    public View getCustom(int position, View convertView, ViewGroup parent) {

//        View row = LayoutInflater.from(context).inflate(
//                android.R.layout.simple_spinner_item, parent, false);
        View row = LayoutInflater.from(context).inflate(
                R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(android.R.id.text1);

        if(toolbarspinner)
        {
            label.setTextColor(Color.WHITE);
        }

        if(aartispinner)
        {
            label.setTextColor(Color.TRANSPARENT);
        }

        if(settingsspinner)
        {
            if(screenType.equals("Project")){
                label.setTextColor(context.getResources().getColor(R.color.PrimarytextColor));
                label.setGravity(Gravity.LEFT);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }else {
                label.setTextColor(context.getResources().getColor(R.color.white));
                label.setGravity(Gravity.LEFT);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }

        }

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
        label.setTypeface(tf);
        label.setText(list.get(position));

        return row;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

//        View row = LayoutInflater.from(context).inflate(
//                android.R.layout.simple_spinner_dropdown_item, parent, false);
        View row = LayoutInflater.from(context).inflate(
                R.layout.spinner_dropdown_item, parent, false);
        TextView label = (TextView) row.findViewById(android.R.id.text1);

        if(toolbarspinner)
        {
            label.setBackgroundColor(context.getResources().getColor(R.color.PrimaryColor));
            label.setTextColor(Color.WHITE);
        }

        if(aartispinner)
        {
            label.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if(settingsspinner)
        {
            label.setBackgroundColor(context.getResources().getColor(R.color.white));
            label.setGravity(Gravity.CENTER_VERTICAL);
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
        label.setTypeface(tf);
        label.setText(list.get(position));

        return row;
    }
}
