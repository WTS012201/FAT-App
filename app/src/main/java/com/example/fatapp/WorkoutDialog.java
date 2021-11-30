package com.example.fatapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkoutDialog extends DialogFragment {
    private static final String TAG = "WorkoutDialog";
    private Calendar calendar;
    private Workout workout;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public interface OnInputListener{
        void logWorkout(Workout workout);
        void keep(Calendar calendar);
    }
    public void initNewWorkoutInput(View outer_view){
        LinearLayout inputWorkout = (LinearLayout) outer_view.findViewById(R.id.input_new_workout);
        inputWorkout.removeAllViews();
        Workout newWorkout = new Workout();

        EditText nameEntry = new EditText(getActivity());
        nameEntry.setHint("Name");
        nameEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWorkout.addView(nameEntry);

        EditText notesEntry = new EditText(getActivity());
        notesEntry.setHint("Notes");
        notesEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWorkout.addView(notesEntry);

        EditText timeEntry = new EditText(getActivity());
        timeEntry.setHint("Time");
        timeEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputWorkout.addView(timeEntry);

        LinearLayout bottom = new LinearLayout(getActivity());
        bottom.setGravity(Gravity.CENTER);
        Button cancel = new Button(getActivity());
        Button confirm = new Button(getActivity());
        cancel.setText("Cancel");
        confirm.setText("Confirm");
        bottom.addView(cancel);
        bottom.addView(confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputWorkout.removeAllViews();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newWorkout.setName(nameEntry.getText().toString());
                newWorkout.setNotes(notesEntry.getText().toString());
                if(timeEntry.getText().toString().isEmpty())
                    newWorkout.setTime(0);
                else
                    newWorkout.setTime(Integer.parseInt(timeEntry.getText().toString()));
                calendar.workouts.add(newWorkout);
                inputWorkout.removeAllViews();

                for(Exercise e : newWorkout.exercises){
                    EditText nameEntry = (EditText) e.exerciseLayout.findViewById(R.id.exercise_name);
                    e.setName(nameEntry.getText().toString());

                    for(View v : e.setLayouts){
                        EditText repEntry = (EditText) v.findViewById(R.id.rep);
                        EditText weightEntry = (EditText) v.findViewById(R.id.weight);
                        int rep, weight;

                        if(repEntry.getText().toString().isEmpty())
                            rep = 0;
                        else
                            rep = Integer.parseInt(repEntry.getText().toString());
                        if(weightEntry.getText().toString().isEmpty())
                            weight = 0;
                        else
                            weight = Integer.parseInt(weightEntry.getText().toString());
                        e.addSet(rep, weight);
                    }
                }
                renderWorkouts(outer_view);

            }
        });

        Button addExercise = new Button(getActivity());
        addExercise.setText("Add Exercise");
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View exerciseEntry = layoutInflater.inflate(R.layout.add_exercise, null, false);
                LinearLayout setTable = (LinearLayout) exerciseEntry.findViewById(R.id.exercise_fields);
                Exercise newExercise = new Exercise();

                Button remove = (Button)exerciseEntry.findViewById(R.id.remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newWorkout.exercises.remove(newExercise);
                        inputWorkout.removeView(exerciseEntry);
                    }
                });

                Button addSet = (Button)exerciseEntry.findViewById(R.id.add_set);
                addSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View exerciseField = layoutInflater.inflate(R.layout.exercise_field, null, false);
                        LinearLayout setEntry = (LinearLayout) exerciseField.findViewById(R.id.exercise_entry_field);
                        EditText set = setEntry.findViewById(R.id.set);

                        Button removeSet = (Button)setEntry.findViewById(R.id.remove);
                        removeSet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newExercise.setLayouts.remove(exerciseField);
                                setTable.removeView(setEntry);
                            }
                        });
                        ((ViewGroup)setEntry.getParent()).removeView(setEntry);

                        newExercise.setLayouts.add(setEntry);
                        setTable.addView(setEntry);
                    }
                });

                newExercise.exerciseLayout = exerciseEntry;
                newWorkout.exercises.add(newExercise);
                inputWorkout.addView(exerciseEntry);
                inputWorkout.removeView(bottom);
                inputWorkout.addView(bottom);
            }
        });

        inputWorkout.addView(addExercise);
        inputWorkout.addView(bottom);
    }

    public void renderWorkouts(View view){
        LinearLayout workoutsView = (LinearLayout) view.findViewById(R.id.workouts_view);
        workoutsView.removeAllViews();
        for(Workout w : calendar.workouts){
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View content = layoutInflater.inflate(R.layout.remove, null, false);

            Button remove = (Button)content.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.workouts.remove(w);
                    workoutsView.removeView(content);
                }
            });
            Button wContent = (Button)content.findViewById(R.id.content);
            wContent.setText(w.name);
            wContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnInputListener.logWorkout(w);
                    calendar.currentPage.selected.logged = true;
                    dismiss();
                }
            });
            workoutsView.addView(content);
        }
    }
    public void renderWorkout(View outerView){
        LinearLayout inputWorkout = (LinearLayout) outerView.findViewById(R.id.input_new_workout);
        inputWorkout.removeAllViews();
        Workout newWorkout = new Workout(workout);

        EditText nameEntry = new EditText(getActivity());
        nameEntry.setText(workout.name);
        nameEntry.setHint("Name");
        nameEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWorkout.addView(nameEntry);

        EditText notesEntry = new EditText(getActivity());
        notesEntry.setText(workout.notes);
        notesEntry.setHint("Notes");
        notesEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWorkout.addView(notesEntry);

        EditText timeEntry = new EditText(getActivity());
        timeEntry.setHint("Time");
        timeEntry.setText(Integer.toString(workout.time));
        timeEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputWorkout.addView(timeEntry);

        LinearLayout bottom = new LinearLayout(getActivity());
        bottom.setGravity(Gravity.CENTER);
        Button cancel = new Button(getActivity());
        Button confirm = new Button(getActivity());
        cancel.setText("Cancel");
        confirm.setText("Confirm");
        bottom.addView(cancel);
        bottom.addView(confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputWorkout.removeAllViews();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newWorkout.setName(nameEntry.getText().toString());
                newWorkout.setNotes(notesEntry.getText().toString());
                if(timeEntry.getText().toString().isEmpty())
                    newWorkout.setTime(0);
                else
                    newWorkout.setTime(Integer.parseInt(timeEntry.getText().toString()));
                calendar.workouts.remove(workout);
                calendar.workouts.add(newWorkout);
                inputWorkout.removeAllViews();
                for(Exercise e : newWorkout.exercises){
                    EditText nameEntry = (EditText) e.exerciseLayout.findViewById(R.id.exercise_name);
                    e.setName(nameEntry.getText().toString());

                    for(View v : e.setLayouts){
                        EditText repEntry = (EditText) v.findViewById(R.id.rep);
                        EditText weightEntry = (EditText) v.findViewById(R.id.weight);
                        int rep, weight;

                        if(repEntry.getText().toString().isEmpty())
                            rep = 0;
                        else
                            rep = Integer.parseInt(repEntry.getText().toString());
                        if(weightEntry.getText().toString().isEmpty())
                            weight = 0;
                        else
                            weight = Integer.parseInt(weightEntry.getText().toString());
                        e.addSet(rep, weight);
                    }
                }
                renderWorkouts(outerView);
            }
        });
        Button addExercise = new Button(getActivity());
        addExercise.setText("Add Exercise");
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View exerciseEntry = layoutInflater.inflate(R.layout.add_exercise, null, false);
                LinearLayout setTable = (LinearLayout) exerciseEntry.findViewById(R.id.exercise_fields);
                Exercise newExercise = new Exercise();

                Button remove = (Button)exerciseEntry.findViewById(R.id.remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newWorkout.exercises.remove(newExercise);
                        inputWorkout.removeView(exerciseEntry);
                    }
                });

                Button addSet = (Button)exerciseEntry.findViewById(R.id.add_set);
                addSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View exerciseField = layoutInflater.inflate(R.layout.exercise_field, null, false);
                        LinearLayout setEntry = (LinearLayout) exerciseField.findViewById(R.id.exercise_entry_field);
                        EditText set = setEntry.findViewById(R.id.set);

                        Button removeSet = (Button)setEntry.findViewById(R.id.remove);
                        removeSet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newExercise.setLayouts.remove(exerciseField);
                                setTable.removeView(setEntry);
                            }
                        });
                        ((ViewGroup)setEntry.getParent()).removeView(setEntry);

                        newExercise.setLayouts.add(setEntry);
                        setTable.addView(setEntry);
                    }
                });

                newExercise.exerciseLayout = exerciseEntry;
                newWorkout.exercises.add(newExercise);
                inputWorkout.addView(exerciseEntry);
                inputWorkout.removeView(bottom);
                inputWorkout.addView(bottom);
            }
        });
        inputWorkout.addView(addExercise);
        System.out.println("BEFORE");
        for(Exercise e : workout.exercises){
            inputWorkout.addView(e.exerciseLayout);
            e.setName(nameEntry.getText().toString());

            LinearLayout setTable = (LinearLayout) e.exerciseLayout.findViewById(R.id.exercise_fields);
            for(View v : e.setLayouts){
                ((ViewGroup)v.getParent()).removeView(v);
                setTable.addView(v);
            }
        }
        System.out.println("AFTER");
        inputWorkout.addView(bottom);
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public OnInputListener mOnInputListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        onViewStateRestored(savedInstanceState);

        View view_outer = inflater.inflate(R.layout.dialog_fragment, container, false);
        LinearLayout workouts = (LinearLayout) view_outer.findViewById(R.id.workouts);
        Button newWorkout = (Button)view_outer.findViewById(R.id.new_workout);

        newWorkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                initNewWorkoutInput(view_outer);
            }
        });

        if(workout != null)
            renderWorkout(view_outer);

        renderWorkouts(view_outer);
        return view_outer;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) calendar;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnInputListener.keep(calendar);
    }
}
