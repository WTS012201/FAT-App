package com.example.fatapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.TestLooperManager;
import android.text.format.Time;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calendar {

    private Map<Pair<Integer, Integer>, Month> monthMap =
        new HashMap<Pair<Integer, Integer>, Month>();
    private Month currentPage;
    private Button addWorkout;
    private Button addReminder;
    private Button next;
    private Button prev;
    private Activity calendar;
    public Time currentTime;

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
                currentPage.selected.logged = true;
                currentPage.selected.setBackgroundResource(R.drawable.day_button_logged);
            }
        });

        addReminder = calendar.findViewById(R.id.addReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage.selected.logged = true;
                currentPage.selected.setBackgroundResource(R.drawable.day_button_logged);
            }
        });
        prev = calendar.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
                System.out.println("Map Size: " + monthMap.size() + " Page: "
                        + currentPage.year + " " + currentPage.month);
            }
        });
        next = calendar.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
                System.out.println("Map Size: " + monthMap.size() + " Page: "
                    + currentPage.year + " " + currentPage.month);
            }
        });
        currentPage.generateNewMonthView();
        currentPage.selected.setBackgroundResource(R.drawable.day_button_normal);
        currentPage.selected = currentPage.days.get(currentTime.monthDay - 1);
        currentPage.selected.setBackgroundResource(R.drawable.day_button_selected);

        textMonth.setText(currentPage.intMonthToString(currentPage.month) + " "
                + currentPage.selected.dayOfMonth + ",\n" + currentPage.year);
    }
    public Month prev(){
        int year = currentPage.year - (currentPage.month - 1 == -1 ? 1 : 0);
        int month = (currentPage.month - 1 == -1 ? 11 : currentPage.month - 1);
        Pair<Integer, Integer> pair =
            new Pair<Integer, Integer>(year, month);

        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);
        table.removeAllViews();

        if (!monthMap.containsKey(pair)) {
            monthMap.put(pair, new Month(calendar, year, month));
            currentPage = monthMap.get(pair).generateNewMonthView();
        }else {
            currentPage = monthMap.get(pair).refresh(table);
        }
        return currentPage;
    }
    public Month next(){
        int year = currentPage.year + (currentPage.month + 1 == 12 ? 1 : 0);
        int month = (currentPage.month + 1) % 12;
        Pair<Integer, Integer> pair =
            new Pair<Integer, Integer>(year, month);

        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);
        table.removeAllViews();

        if (!monthMap.containsKey(pair)) {
            monthMap.put(pair, new Month(calendar, year, month));
            currentPage = monthMap.get(pair).generateNewMonthView();
        }else {
            currentPage = monthMap.get(pair).refresh(table);
        }
        return currentPage;
    }
    //Select day block => changes log view to the selected day of the month
}
