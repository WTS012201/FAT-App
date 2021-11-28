package com.example.fatapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.preference.AndroidResources;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.sql.Date;
import java.util.ArrayList;

public class Month {
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private int dayOffset = 0;
    public int year;
    public int month;

    public ArrayList<CalendarButton> calendarButtons = new ArrayList<CalendarButton>();
    public CalendarButton selected;
    private Activity calendar;

    public Month(Activity calendar, int year, int month){
        Date date = new Date(year, month, 1);

        this.year = year;
        this.month = month;
        this.calendar = calendar;
        this.dayOffset = (date.getDay() - 1 == -1) ? 6 : date.getDay() - 1;
    }
    public Month refresh(TableLayout table){
        TextView textMonth = (TextView)calendar.findViewById(R.id.textMonth);
        textMonth.setText(intMonthToString(month) + ",\n" + year);

        for(int day = 1, offset = dayOffset; day <= calendarButtons.size(); offset = 0){
            TableRow tableRow = new TableRow(calendar);
            table.addView(tableRow);
            if(day == 1){   //  dummy buttons for the offset
                for(int col = 0; col < offset; col++)
                    tableRow.addView(new CalendarButton(calendar, ""));
            }
            for(int col = offset; col < COLUMNS &&
                    day <= calendarButtons.size(); col++, day++){
                CalendarButton button = new CalendarButton(calendar, calendarButtons.get(day-1));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!selected.logged)    //  Switches previous selected to original state
                            selected.setBackgroundResource(R.drawable.day_button_normal);
                        else if(selected.logged)
                            selected.setBackgroundResource(R.drawable.day_button_logged);
                        button.setBackgroundResource(R.drawable.day_button_selected);
                        selected = button;
                        generateLog();
                        textMonth.setText(intMonthToString(month) + " " + button.label + ",\n" + year);
                    }
                });
                if(!button.logged)    //  Show what is logged
                    button.setBackgroundResource(R.drawable.day_button_normal);
                else if(button.logged)
                    button.setBackgroundResource(R.drawable.day_button_logged);

                tableRow.addView(button);
                calendarButtons.set(day-1, button);
            }
        }
        selected = calendarButtons.get(0);
        selected.setBackgroundResource(R.drawable.day_button_selected);
        return this;
    }
    public Month generateNewMonthView() {
        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);

        TextView textMonth = (TextView)calendar.findViewById(R.id.textMonth);
        textMonth.setText(intMonthToString(month) + ",\n" + year);

        for(int day = 1, offset = dayOffset;
            day <= getNumberOfDaysInMonth(month, year); offset = 0){
            TableRow tableRow = new TableRow(calendar);
            table.addView(tableRow);
            if(day == 1){   //  dummy buttons for the offset
                for(int col = 0; col < offset; col++)
                    tableRow.addView(new CalendarButton(calendar, ""));
            }
            for(int col = offset; (col < COLUMNS) &&
                    (day <= getNumberOfDaysInMonth(month, year)); col++, day++){
                CalendarButton button = new CalendarButton(calendar, Integer.toString(day));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!selected.logged)    //  Switches previous selected to original state
                            selected.setBackgroundResource(R.drawable.day_button_normal);
                        else if(selected.logged)
                            selected.setBackgroundResource(R.drawable.day_button_logged);
                        button.setBackgroundResource(R.drawable.day_button_selected);
                        selected = button;
                        generateLog();
                        textMonth.setText(intMonthToString(month) + " " + button.label + ",\n" + year);
                    }
                });
                tableRow.addView(button);
                calendarButtons.add(button);
            }
        }
        selected = calendarButtons.get(0);
        selected.setBackgroundResource(R.drawable.day_button_selected);
        return this;
    }
    public void generateLog(){
        LinearLayout log = (LinearLayout) calendar.findViewById(R.id.logLayout);
        View div = (View)calendar.findViewById(R.id.logDivider);
        log.removeAllViews();
        log.addView(div);
        for(Workout w : selected.workouts) {
            Button b = new Button(calendar);
            b.setText("Workout");
            log.addView(b);
        }
        for(Reminder r : selected.reminders) {
            Button b = new Button(calendar);
            b.setText("Reminder");
            log.addView(b);
        }
    }
    private static int getNumberOfDaysInMonth(int month, int year){
        switch(month){
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return (year%4 == 0) ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
        }
        return -1;
    }

    public static String intMonthToString(int month){
        switch(month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return null;
    }
}
