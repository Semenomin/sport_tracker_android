package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Training extends AppCompatActivity {

    List<Exercise> exercises= new ArrayList<>();
    TextView id,date;
    RecyclerView recyclerView;
    ExerciseAdapter adapter;
    DBHelper helper;
    String id_tr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        if (arguments.getInt("enb")==0) {
            id_tr = arguments.getString("id");
            helper = new DBHelper(this);
            id = findViewById(R.id.numText);
            date = findViewById(R.id.DateText);
            id.setText(String.valueOf(helper.getIdTranByDate(id_tr)));
            date.setText(id_tr);
            Button bt = findViewById(R.id.button6);
            bt.setEnabled(false);
        }
        else {
            id_tr = arguments.getString("id");
            Button bt = findViewById(R.id.button6);
            bt.setEnabled(true);
            helper = new DBHelper(this);
            id = findViewById(R.id.numText);
            date = findViewById(R.id.DateText);
            id.setText(String.valueOf(helper.getIdTranByDate(id_tr)+1));
            date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
            setInitialData();
            recyclerView = findViewById(R.id.list_exer);
            adapter = new ExerciseAdapter(this, exercises);
            recyclerView.setAdapter(adapter);
    }

    private void setInitialData() {
        try {

            exercises = helper.getExercises(id.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Что-то пошло не так, попробуйте позже", Toast.LENGTH_SHORT).show();
        }
    }

    public void end(View view) {
        EditText wei = findViewById(R.id.editWeight);
        helper.addTrainee(date.getText().toString(),wei.getText().toString());
        startActivity(new Intent(this,MainActivity.class));
    }

    public void onClick(View view) {
        Intent intent = new Intent(this,add_exercise.class);
        intent.putExtra("date",id_tr);
        startActivity(intent);
    }
}
