package com.example.fatapp;

import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

public class Exercise {
    public ArrayList<Pair<Integer, Integer>> sets = new ArrayList<Pair<Integer, Integer>>();
    public ArrayList<View> setLayouts = new ArrayList<View>();
    public View exerciseLayout;
    public String name;

    public void addSet(int reps, int weight){
        sets.add(new Pair(reps, weight));
    }
    public void removeSet(int idx) {
        sets.remove(idx);
    }
    public void setName(String name){ this.name = name;}

}
