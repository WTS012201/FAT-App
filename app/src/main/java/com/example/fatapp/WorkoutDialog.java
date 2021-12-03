package com.example.fatapp;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkoutDialog extends DialogFragment{
    private static final String TAG = "WorkoutDialog";
    private Calendar calendar;
    private Workout workout;
    //  Widgets
    private View workoutView;
    private LinearLayout inputWorkout;
    private EditText nameEntry;
    private EditText notesEntry;
    private EditText timeEntry;
    private LinearLayout bottom;
    private Button cancel;
    private Button confirm;
    private Button addExercise;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public interface OnInputListener{
        void logWorkout(Workout workout);

        void logMeal(ExampleItem meal);

        void keep(Calendar calendar);
    }

    public void renderWorkouts(){
        LinearLayout workoutListView = (LinearLayout) workoutView.findViewById(R.id.workouts_view);
        workoutListView.removeAllViews();
        for(Workout w : calendar.workouts){
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View content = layoutInflater.inflate(R.layout.remove, null, false);

            Button remove = (Button)content.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.workouts.remove(w);
                    workoutListView.removeView(content);
                }
            });
            Button wContent = (Button)content.findViewById(R.id.content);
            wContent.setText(w.name);
            wContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {    //  Bug here
                    mOnInputListener.logWorkout(w);
                    calendar.currentPage.selected.logged = true;
                    dismiss();
                }
            });
            workoutListView.addView(content);
        }
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public OnInputListener mOnInputListener;

    public void getBottom(Workout newWorkout){
        bottom = new LinearLayout(getActivity());
        bottom.setGravity(Gravity.CENTER);

        cancel = new Button(getActivity());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputWorkout.removeAllViews();
            }
        });
        cancel.setText("Cancel");

        confirm = new Button(getActivity());
        confirm.setText("Confirm");
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
                renderWorkouts();
            }
        });
        confirm.setText("Confirm");
        bottom.addView(cancel);
        bottom.addView(confirm);
    }
    public void getTop(){
        nameEntry = new EditText(getActivity());
        nameEntry.setHint("Name");
        nameEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWorkout.addView(nameEntry);

        notesEntry = new EditText(getActivity());
        notesEntry.setHint("Notes");
        notesEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWorkout.addView(notesEntry);

        timeEntry = new EditText(getActivity());
        timeEntry.setHint("Time");
        timeEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputWorkout.addView(timeEntry);
    }
    public void genExercise(Workout newWorkout){
        addExercise = new Button(getActivity());
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
    }
    public void initNewWorkoutInput(){
        inputWorkout = (LinearLayout) workoutView.findViewById(R.id.input_new_workout);
        inputWorkout.removeAllViews();

        Workout newWorkout = new Workout();
        getTop();
        genExercise(newWorkout);
        getBottom(newWorkout);

        inputWorkout.addView(addExercise);
        inputWorkout.addView(bottom);
    }

    public void renderNewWorkout(){
        inputWorkout = (LinearLayout) workoutView.findViewById(R.id.input_new_workout);
        inputWorkout.removeAllViews();
        Workout newWorkout = new Workout(workout);

        getTop();
        nameEntry.setText(workout.name);
        notesEntry.setText(workout.notes);
        timeEntry.setText(Integer.toString(workout.time));

        genExercise(newWorkout);
        getBottom(newWorkout);

        for(Exercise e : workout.exercises){
            if(e.exerciseLayout.getParent() != null)
                ((ViewGroup)e.exerciseLayout.getParent()).removeView(e.exerciseLayout);
            inputWorkout.addView(e.exerciseLayout);
            Button remove = (Button)e.exerciseLayout.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newWorkout.exercises.remove(e);
                    inputWorkout.removeView(e.exerciseLayout);
                }
            });

            LinearLayout setTable = (LinearLayout) e.exerciseLayout.findViewById(R.id.exercise_fields);
            for(View v : e.setLayouts) {
                ((ViewGroup) v.getParent()).removeView(v);
                setTable.addView(v);
            }
        }

        inputWorkout.addView(bottom);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        onViewStateRestored(savedInstanceState);
        workoutView = inflater.inflate(R.layout.dialog_fragment, container, false);
        LinearLayout workouts = (LinearLayout) workoutView.findViewById(R.id.workouts);
        Button newWorkout = (Button)workoutView.findViewById(R.id.new_workout);

        newWorkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                initNewWorkoutInput();
            }
        });

        if(workout != null)
            renderNewWorkout();
        renderWorkouts();
        return workoutView;
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
