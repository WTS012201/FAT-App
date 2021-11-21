package com.example.fatapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Day extends androidx.appcompat.widget.AppCompatButton{
    public int dayOfMonth;
    private ArrayList<Reminder> reminders = new ArrayList<Reminder>();
    private ArrayList<Workout> workouts = new ArrayList<Workout>();
    public boolean logged;

    public Day(Context context, int dayOfMonth) {
        super(context);
        this.logged = false;
        this.dayOfMonth = dayOfMonth;
        if(dayOfMonth == -1) {
            setBackgroundResource(R.drawable.day_button_dummy);
            setEnabled(false);
        }
        else {
            setText("" + dayOfMonth + "");
            setBackgroundResource(R.drawable.day_button_normal);
        }
        setScaleX(0.9f);
        setScaleY(0.9f);
        setTextSize(25.0f);
    }
    public Day(Context context, Day day){
        super(context);
        this.logged = day.logged;
        this.dayOfMonth = day.dayOfMonth;
        if(day.dayOfMonth == -1) {
            setBackgroundResource(R.drawable.day_button_dummy);
            setEnabled(false);
        }
        else {
            setText("" + dayOfMonth + "");
            setBackgroundResource(R.drawable.day_button_normal);
        }
        setScaleX(0.9f);
        setScaleY(0.9f);
        setTextSize(25.0f);
    }
    //AddReminder
    public void addReminder(Reminder reminder){
        reminders.add(reminder);
    }
    //RemoveReminder
    public void removeReminder(int reminderNum){
        reminders.remove(reminderNum);
    }
    //AddWorkout
    public void addWorkout(Workout workout){
        workouts.add(workout);
    }
    //RemoveWorkout
    public void removeWorkout(int workoutNum){
        workouts.remove(workoutNum);
    }
}