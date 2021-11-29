package com.example.fatapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    public Calendar calendar;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public interface OnInputListener{
        void logWorkout(Workout workout);
        void keep(Calendar calendar);
    }
    public void initNewWorkoutInput(View outer_view){
        LinearLayout input_workout = (LinearLayout) outer_view.findViewById(R.id.input_new_workout);
        input_workout.removeAllViews();

        EditText nameEntry = new EditText(getActivity());
        nameEntry.setHint("Name");
        nameEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        input_workout.addView(nameEntry);

        EditText notesEntry = new EditText(getActivity());
        notesEntry.setHint("Notes");
        notesEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        input_workout.addView(notesEntry);

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
                input_workout.removeAllViews();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Workout workout = new Workout();
                workout.setName(nameEntry.getText().toString());
                workout.setNotes(notesEntry.getText().toString());
                calendar.workouts.add(workout);
                input_workout.removeAllViews();
                renderWorkouts(outer_view);
            }
        });
        input_workout.addView(bottom);
    }
    public void renderWorkouts(View view){
        LinearLayout workoutsView = (LinearLayout) view.findViewById(R.id.workouts_view);
        workoutsView.removeAllViews();
        for(Workout w : calendar.workouts){
            Button text = new Button(getActivity());
            text.setText(w.name);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnInputListener.logWorkout(w);
                }
            });
            workoutsView.addView(text);
        }
    }
    public OnInputListener mOnInputListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        View view_outer = inflater.inflate(R.layout.dialog_fragment, container, false);
        LinearLayout workouts = (LinearLayout) view_outer.findViewById(R.id.workouts);
        Button newWorkout = (Button)view_outer.findViewById(R.id.new_workout);

        newWorkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                initNewWorkoutInput(view_outer);
            }
        });
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
