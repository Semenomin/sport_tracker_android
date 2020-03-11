package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecycleAdapter adapter;
    List<Weight> weights;
    String name;
    int weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper helper = new DBHelper(this);
        setContentView(R.layout.activity_main);
        setInitialData();
        recyclerView = findViewById(R.id.list);
        adapter = new RecycleAdapter(this, weights);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        name = helper.getUserName();
        if(weights.size() == 0)
        {
            weight = helper.getWeight();
        }
        else {
            weight = weights.get(weights.size()-1).getWeight();
        }
        TextView nameText = findViewById(R.id.nameText);
        nameText.setText(name);
        TextView weightText = findViewById(R.id.weightText);
        weightText.setText(String.valueOf(weight));
    }

    private void setInitialData() {
        try {
            DBHelper helper = new DBHelper(this);
            weights = helper.getWeights();
        } catch (Exception e) {
            Toast.makeText(this, "Что-то пошло не так, попробуйте позже", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this,Training.class);
        intent.putExtra("id",weights.get(weights.size()-1).getDate());
        intent.putExtra("enb",1);
        startActivity(intent);
    }
}
