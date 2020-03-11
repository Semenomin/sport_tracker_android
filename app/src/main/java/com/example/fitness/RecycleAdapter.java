package com.example.fitness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Weight> weights;
    private Context mainContext;

    RecycleAdapter(Context context, List<Weight> weights) {
        this.weights = weights;
        this.inflater = LayoutInflater.from(context);
        mainContext = context;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecycleAdapter.ViewHolder holder, final int position) {
        final Weight we = weights.get(position);
        holder.weight.setText(String.valueOf(we.getWeight()));
        holder.date.setText(we.getDate());
        holder.main.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainContext, Training.class);
                intent.putExtra("id",we.getDate());
                intent.putExtra("enb",0);
                mainContext.startActivity(intent);
            }
        });
        holder.main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(mainContext,holder.main);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        DBHelper helper = new DBHelper(mainContext);
                        helper.deleteTrainee(we.getDate());
                        return true;
                    }
                });
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return weights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView weight,date;
        final LinearLayout main;
        ViewHolder(View view){
            super(view);
            main = view.findViewById(R.id.main);
            weight = view.findViewById(R.id.wegiht);
            date = view.findViewById(R.id.date);
        }
    }
}
