package com.example.fatapp;

import android.view.View;

import java.util.ArrayList;

public class Workout {
    public String name;
    public String notes;
    public int time;
    public ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    Workout(){
        this.name = "New Workout";
    }
    Workout(Workout workout){
        this.name = workout.name;
        this.notes = workout.notes;
        this.time = workout.time;
    this.exercises = workout.exercises;
    }
    public void addExercise() {
        exercises.add(new Exercise());
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
