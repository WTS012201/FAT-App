package com.example.fatapp;

import java.util.ArrayList;

public class Workout {
    public String name;
    public String notes;
    public ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    Workout(){
        this.name = "New Workout";
    }
    public void addExercise(String name) {
        exercises.add(new Exercise(name));
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
}
