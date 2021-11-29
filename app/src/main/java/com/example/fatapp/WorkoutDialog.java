package com.example.fatapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    ArrayList<Workout> workouts = new ArrayList<Workout>();

    public void setCalendar(Calendar calendar, ArrayList<Workout> workouts) {
        this.calendar = calendar;
        this.workouts = workouts;
    }

    public interface OnInputListener{
        void logWorkout(Workout workout);
        void keepWorkouts(ArrayList<Workout> workouts);
    }
    public void initNewWorkoutInput(View view){
        LinearLayout workouts = (LinearLayout) view.findViewById(R.id.input_new_workout);
        TextView text = new TextView(getActivity());
        text.setText("test");
        workouts.addView(text);
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
        mOnInputListener.keepWorkouts(workouts);
    }
}
