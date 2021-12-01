package com.example.fatapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MealPlan extends DialogFragment implements ExampleAdapter.OnInputListener{
    ArrayList<ExampleItem> mExampleList = new ArrayList<>();
    private static final String TAG = "MealDialog";
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Calendar calendar;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
         @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mealplan, container, false);
        buildRecyclerView(view);
        setInsertButton(view);
        onViewStateRestored(savedInstanceState);
        renderMeals(view);
        return view;
    }

    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(mExampleList);
        mAdapter.setMealPlan(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setInsertButton(View view) {
        Button buttonInsert = view.findViewById(R.id.button_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText line1 = view.findViewById(R.id.edittext_line_1);
                EditText line2 = view.findViewById(R.id.edittext_line_2);

                insertItem(" Meal: " + line1.getText().toString()," Calories: " + line2.getText().toString());
                line1.setText("");
                line2.setText("");
            }
        });
    }
    private void renderMeals(View view){
        for(ExampleItem e : calendar.meals){
            insertItem(e.getLine1(), e.getLine2());
        }
    }

    private void insertItem(String line1, String line2) {
        mExampleList.add(new ExampleItem(line1, line2));
        mAdapter.notifyItemInserted(mExampleList.size());
        calendar.meals = mExampleList;
    }

    @Override
    public void addMeal(ExampleItem meal) {
        mOnInputListener.logMeal(meal);
    }

    public interface OnInputListener{
        void logMeal(ExampleItem meal);
        void keep(Calendar calendar);
    }
    public OnInputListener mOnInputListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (MealPlan.OnInputListener) calendar;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        calendar.currentPage.generateLog(calendar);
        mOnInputListener.keep(calendar);
    }
}