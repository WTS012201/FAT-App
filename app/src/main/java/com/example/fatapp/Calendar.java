package com.example.fatapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.text.Layout;
import android.text.format.Time;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Calendar implements WorkoutDialog.OnInputListener, MealPlan.OnInputListener
    , ReminderDialog.OnInputListener{
    private Map<Pair<Integer, Integer>, Month> monthMap =
        new HashMap<Pair<Integer, Integer>, Month>();
    public ArrayList<Workout> workouts = new ArrayList<Workout>();
    public ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    public ArrayList<ExampleItem> meals = new ArrayList<ExampleItem>();
    public ArrayList<ExampleItem> reminders = new ArrayList<ExampleItem>();

    public Month currentPage;
    public Activity calendar;
    public Time currentTime;

    //  widgets
    private Button addWorkout;
    private Button addReminder;
    private Button addMeal;
    private CalendarButton next;
    private CalendarButton prev;

    private static final String TAG = "Calendar";

    public Calendar(Activity calendar){
        this.calendar = calendar;
        currentTime = new Time(Time.getCurrentTimezone());
        currentTime.setToNow();
        // Will have to load save file here. Below will be used for now
        Pair<Integer, Integer> pair =
            new Pair<Integer, Integer>(currentTime.year, currentTime.month);
        this.monthMap.put(pair,
            new Month(calendar, currentTime.year, currentTime.month));
        this.currentPage = monthMap.get(pair);
    }

    public void generateCalendarView() {
        TextView textMonth = (TextView)calendar.findViewById(R.id.textMonth);

        addWorkout = calendar.findViewById(R.id.addWorkout);

        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentPage.selected.setBackgroundResource(R.drawable.day_button_logged);
                WorkoutDialog workoutDialog = new WorkoutDialog();
                workoutDialog.setCalendar(Calendar.this);
                workoutDialog.show(calendar.getFragmentManager(), "WorkoutDialog");
            }
        });

        addReminder = calendar.findViewById(R.id.addReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReminderDialog reminderDialog = new ReminderDialog();
                reminderDialog.setCalendar(Calendar.this);
                reminderDialog.show(calendar.getFragmentManager(), "ReminderDialog");
            }
        });

        addMeal = calendar.findViewById(R.id.addMeal);
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentPage.selected.setBackgroundResource(R.drawable.day_button_logged);
                MealPlan mealDialog = new MealPlan();
                mealDialog.setCalendar(Calendar.this);
                mealDialog.show(calendar.getFragmentManager(), "MealDialog");
            }
        });

        currentPage.generateNewMonthView(this);
        generateNextPrev();
        currentPage.selected.setBackgroundResource(R.drawable.day_button_normal);
        currentPage.selected = currentPage.calendarButtons.get(currentTime.monthDay - 1);
        currentPage.selected.setBackgroundResource(R.drawable.day_button_selected);
        textMonth.setText(currentPage.intMonthToString(currentPage.month) + " "
                + currentPage.selected.label + ", " + currentPage.year);
    }
    private void generateNextPrev(){
        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);
        TableRow tableRow = new TableRow(calendar);

        prev = new CalendarButton(calendar, "<");
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
                System.out.println("Map Size: " + monthMap.size() + " Page: "
                        + currentPage.year + " " + currentPage.month);
            }
        });
        next = new CalendarButton(calendar, ">");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
                System.out.println("Map Size: " + monthMap.size() + " Page: "
                        + currentPage.year + " " + currentPage.month);
            }
        });
        tableRow.addView(prev);
        for(int i = 0; i < 5; i++)
            tableRow.addView(new CalendarButton(calendar, ""));
        tableRow.addView(next);
        table.addView(tableRow);
    }
    private Month prev(){
        int year = currentPage.year - (currentPage.month - 1 == -1 ? 1 : 0);
        int month = (currentPage.month - 1 == -1 ? 11 : currentPage.month - 1);
        Pair<Integer, Integer> pair =
            new Pair<Integer, Integer>(year, month);

        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);
        table.removeAllViews();

        if (!monthMap.containsKey(pair)) {
            monthMap.put(pair, new Month(calendar, year, month));
            currentPage = monthMap.get(pair).generateNewMonthView(this);
        }else {
            currentPage = monthMap.get(pair).refresh(table, this);
        }
        currentPage.generateLog(this);
        generateNextPrev();
        return currentPage;
    }
    private Month next(){
        int year = currentPage.year + (currentPage.month + 1 == 12 ? 1 : 0);
        int month = (currentPage.month + 1) % 12;
        Pair<Integer, Integer> pair =
            new Pair<Integer, Integer>(year, month);

        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);
        table.removeAllViews();

        if (!monthMap.containsKey(pair)) {
            monthMap.put(pair, new Month(calendar, year, month));
            currentPage = monthMap.get(pair).generateNewMonthView(this);
        }else {
            currentPage = monthMap.get(pair).refresh(table, this);
        }
        currentPage.generateLog(this);
        generateNextPrev();
        return currentPage;
    }

    @Override
    public void logWorkout(Workout workout) {
        currentPage.selected.addWorkout(workout);
        currentPage.generateLog(this);
    }

    @Override
    public void logMeal(ExampleItem meal) {
        currentPage.selected.meals.add(meal);
        currentPage.selected.logged = true;
        currentPage.generateLog(this);
    }

    @Override
    public void logReminder(ExampleItem reminder) {
        currentPage.selected.reminders.add(reminder);
        currentPage.selected.logged = true;
        currentPage.generateLog(this);
    }

    @Override
    public void keep(Calendar cal) {
        workouts = cal.workouts;
        exercises = cal.exercises;
        meals = cal.meals;
    }
}
