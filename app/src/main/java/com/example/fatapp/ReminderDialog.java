package com.example.fatapp;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderDialog extends DialogFragment implements ReminderAdapter.OnInputListener{
    ArrayList<ExampleItem> mExampleList = new ArrayList<>();
    private static final String TAG = "ReminderDialog";
    private RecyclerView mRecyclerView;
    private ReminderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Calendar calendar;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mealplan, container, false);
        EditText entryHour = (EditText) view.findViewById(R.id.edittext_line_1);
        EditText entryMin = (EditText)  view.findViewById(R.id.edittext_line_2);
        entryHour.setHint("Hour");
        entryHour.setInputType(InputType.TYPE_CLASS_NUMBER);
        entryMin.setHint("Minute");
        buildRecyclerView(view);
        setInsertButton(view);
        onViewStateRestored(savedInstanceState);
        renderReminders(view);
        return view;
    }

    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ReminderAdapter(mExampleList);
        mAdapter.setReminderDialog(this);

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
                int hour = 0, min = 0;

                if(!line1.getText().toString().isEmpty())
                    hour = Integer.parseInt(line1.getText().toString());
                else
                    return;

                if(!line2.getText().toString().isEmpty())
                    min = Integer.parseInt(line2.getText().toString());
                else
                    return;

                if((hour > 24 || hour < 0) || (min > 60 || min < 0))
                    return;

                insertItem(" Hour: " + Integer.toString(hour)," Minute: " + Integer.toString(min));
                line1.setText("");
                line2.setText("");
            }
        });
    }
    private void renderReminders(View view){
        for(ExampleItem e : calendar.reminders){
            insertItem(e.getLine1(), e.getLine2());
        }
    }

    private void insertItem(String line1, String line2) {
        mExampleList.add(new ExampleItem(line1, line2));
        mAdapter.notifyItemInserted(mExampleList.size());
        calendar.reminders = mExampleList;
    }

    @Override
    public void addReminder(ExampleItem reminder) {
        mOnInputListener.logReminder(reminder);
    }

    public interface OnInputListener{
        void logReminder(ExampleItem reminder);
        void keep(Calendar calendar);
    }
    public ReminderDialog.OnInputListener mOnInputListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (ReminderDialog.OnInputListener) calendar;
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
