package com.example.fatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private ReminderDialog reminderDialog;
    public interface OnInputListener{
        void addReminder(ExampleItem reminder);
    }
    public void setReminderDialog(ReminderDialog reminderDialog){
        this.reminderDialog = reminderDialog;
    }
    private ReminderAdapter.OnInputListener mOnInputListener;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewLine1;
        public TextView mTextViewLine2;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextViewLine1 = itemView.findViewById(R.id.textview_line1);
            mTextViewLine2 = itemView.findViewById(R.id.textview_line_2);
        }
    }

    public ReminderAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ReminderAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_items, parent, false);
        ReminderAdapter.ExampleViewHolder evh = new ReminderAdapter.ExampleViewHolder(v);
        Button remove = v.findViewById(R.id.remove);

        mOnInputListener = (ReminderAdapter.OnInputListener) reminderDialog;

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExampleList.remove(evh.getAdapterPosition());
                ReminderAdapter.super.notifyItemRemoved(evh.getAdapterPosition());
            }
        });
        Button add = v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnInputListener.addReminder(mExampleList.get(evh.getAdapterPosition()));
            }
        });
        return evh;
    }
    @Override
    public void onBindViewHolder(ReminderAdapter.ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mTextViewLine1.setText(currentItem.getLine1());
        holder.mTextViewLine2.setText(currentItem.getLine2());

    }
    public int getItemCount() {
        return mExampleList.size();
    }
}
