package com.example.fatapp;

import java.util.ArrayList;

public class Workout {
    public String name;
    public String notes;
    public int time;
    public int wid;
    public ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    public Workout(){
        this.name = "New Workout";
        this.wid = 0;
    }
    public void removeExercise(int idx) {
        exercises.remove(idx);
    }
    public void setName(String name){
        this.name = name;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    public void setTime(int timeMin) {
        this.time = timeMin;
    }
}
