package com.example.fatapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
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
    public Month refresh(TableLayout table, Calendar cal){
        TextView textMonth = (TextView)calendar.findViewById(R.id.textMonth);
        textMonth.setText(intMonthToString(month) + ", " + year);

        int count = 0;
        for(int day = 1, offset = dayOffset; day <= calendarButtons.size(); offset = 0, count++){
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
                        generateLog(cal);
                        textMonth.setText(intMonthToString(month) + " " + button.label + ", " + year);
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
        if(count == 5){
            TableRow tableRow = new TableRow(calendar);
            table.addView(tableRow);
            for(int col = 0; col < COLUMNS; col++)
                tableRow.addView(new CalendarButton(calendar, ""));
        }
        selected = calendarButtons.get(0);
        selected.setBackgroundResource(R.drawable.day_button_selected);
        return this;
    }
    public Month generateNewMonthView(Calendar cal) {
        TableLayout table = (TableLayout) calendar.findViewById(R.id.dayTable);

        TextView textMonth = (TextView)calendar.findViewById(R.id.textMonth);
        textMonth.setText(intMonthToString(month) + ", " + year);
        int count = 0;
        for(int day = 1, offset = dayOffset;
            day <= getNumberOfDaysInMonth(month, year); offset = 0, count++){
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
                        generateLog(cal);
                        textMonth.setText(intMonthToString(month) + " " + button.label + ", " + year);
                    }
                });
                tableRow.addView(button);
                calendarButtons.add(button);
            }
        }
        System.out.println(count);
        if(count == 5){
            TableRow tableRow = new TableRow(calendar);
            table.addView(tableRow);
            for(int col = 0; col < COLUMNS; col++)
                tableRow.addView(new CalendarButton(calendar, ""));
        }
        selected = calendarButtons.get(0);
        selected.setBackgroundResource(R.drawable.day_button_selected);
        return this;
    }
    public void generateLog(Calendar cal){
        LinearLayout log = (LinearLayout) calendar.findViewById(R.id.logLayout);
        View div = (View)calendar.findViewById(R.id.logDivider);
        log.removeAllViews();
        log.addView(div);

        System.out.println(selected.label);

        for(Workout w : selected.workouts) {
            LayoutInflater layoutInflater = calendar.getLayoutInflater();
            View content = layoutInflater.inflate(R.layout.remove, null, false);

            Button remove = (Button)content.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected.workouts.remove(w);
                    log.removeView(content);
                    if(selected.reminders.size() == 0 && selected.workouts.size() == 0 && selected.meals.size() == 0)
                        selected.logged = false;
                }
            });
            Button wContent = (Button)content.findViewById(R.id.content);
            wContent.setText(w.name);
            wContent.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View view) {
                    WorkoutDialog workoutDialog = new WorkoutDialog();
                    workoutDialog.setCalendar(cal);
                    workoutDialog.setWorkout(w);
                    workoutDialog.show(cal.calendar.getFragmentManager(), "WorkoutDialog");
                }
            });
            log.addView(content);
        }
        for(ExampleItem m : selected.meals) {
            LayoutInflater layoutInflater = calendar.getLayoutInflater();
            View content = layoutInflater.inflate(R.layout.remove_meal, null, false);

            Button remove = (Button)content.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected.meals.remove(m);
                    log.removeView(content);
                    if(selected.reminders.size() == 0 && selected.workouts.size() == 0 && selected.meals.size() == 0)
                        selected.logged = false;
                }
            });
            TextView t1 = (TextView)content.findViewById(R.id.textView1);
            t1.setText(m.getLine1());
            TextView t2 = (TextView)content.findViewById(R.id.textView2);
            t2.setText(m.getLine2());

            log.addView(content);
        }
        for(ExampleItem r : selected.reminders) {
            LayoutInflater layoutInflater = calendar.getLayoutInflater();
            View content = layoutInflater.inflate(R.layout.remove_meal, null, false);

            Button remove = (Button)content.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected.reminders.remove(r);
                    log.removeView(content);
                    if(selected.reminders.size() == 0 && selected.workouts.size() == 0 && selected.meals.size() == 0)
                        selected.logged = false;
                }
            });
            TextView t1 = (TextView)content.findViewById(R.id.textView1);
            t1.setText(r.getLine1());
            TextView t2 = (TextView)content.findViewById(R.id.textView2);
            t2.setText(r.getLine2());

            log.addView(content);
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
                if(year%4 != 0)
                    return 28;
                else if (year%100 != 0)
                    return 29;
                else if (year%400 != 0)
                    return 28;
                else
                    return 29;
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
