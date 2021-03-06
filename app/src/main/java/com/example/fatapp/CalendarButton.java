package com.example.fatapp;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;

public class CalendarButton extends androidx.appcompat.widget.AppCompatButton{
    public String label;
    public ArrayList<ExampleItem> reminders = new ArrayList<ExampleItem>();
    public ArrayList<Workout> workouts = new ArrayList<Workout>();
    public ArrayList<ExampleItem> meals = new ArrayList<ExampleItem>();

    public boolean logged;

    public CalendarButton(Context context, String label) {
        super(context);
        this.logged = false;
        this.label = label;
        if(label.equals("")) {
            setBackgroundResource(R.drawable.day_button_dummy);
            setEnabled(false);
        }
        else {
            setText(label);
            setBackgroundResource(R.drawable.day_button_normal);
        }
        setScaleX(0.9f);
        setScaleY(0.9f);
        setTextSize(25.0f);
    }
    public CalendarButton(Context context, CalendarButton calendarButton){
        super(context);
        this.logged = calendarButton.logged;
        this.label = calendarButton.label;
        this.workouts = calendarButton.workouts;
        this.reminders = calendarButton.reminders;
        this.meals = calendarButton.meals;
        if(calendarButton.label.equals("")) {
            setBackgroundResource(R.drawable.day_button_dummy);
            setEnabled(false);
        }
        else {
            setText(label);
            setBackgroundResource(R.drawable.day_button_normal);
        }
        setScaleX(0.9f);
        setScaleY(0.9f);
        setTextSize(25.0f);
    }
    //AddReminder
    public void addWorkout(Workout workout){
        workouts.add(workout);
    }

}