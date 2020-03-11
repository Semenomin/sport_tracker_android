package com.example.fitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>  {
    private LayoutInflater inflater;
    private List<Exercise> exercises;
    private Context mainContext;

    ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.exercises = exercises;
        this.inflater = LayoutInflater.from(context);
        mainContext = context;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.exercise_item, parent, false);
        return new ExerciseAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ExerciseAdapter.ViewHolder holder, final int position) {
        final Exercise we = exercises.get(position);
        holder.name.setText(we.getName());
        holder.type.setText(we.getType());
        holder.podh.setText(String.valueOf(we.getPodhody()));
        holder.kollvo.setText(String.valueOf(we.getKollvo()));

    }
    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView name,type,podh,kollvo;
        final LinearLayout main;
        ViewHolder(View view){
            super(view);
            main = view.findViewById(R.id.main);
            podh = view.findViewById(R.id.podh);
            kollvo = view.findViewById(R.id.koll);
            name = view.findViewById(R.id.nameText);
            type = view.findViewById(R.id.typeText);
        }
    }
}
