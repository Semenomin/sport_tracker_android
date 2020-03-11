package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class add_exercise extends AppCompatActivity {

    int id;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        Bundle arguments = getIntent().getExtras();
        date = arguments.getString("date");
        DBHelper help = new DBHelper(this);
        id = help.getIdTranByDate(date);
    }

    public void onClick(View view) {
        Exercise exercise = new Exercise();
        EditText name = findViewById(R.id.editName);
        EditText type = findViewById(R.id.editType);
        EditText podh = findViewById(R.id.editPodh);
        EditText kollvo = findViewById(R.id.editKollvo);
        if (!name.getText().toString().isEmpty()) {
            exercise.setName(name.getText().toString());
        }
        if (!type.getText().toString().isEmpty()) {
            exercise.setType(type.getText().toString());
        }
        if(podh.getText().toString().isEmpty() || kollvo.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Null Fields",Toast.LENGTH_LONG).show();
        }
        else {
            exercise.setPodhody(Integer.parseInt(podh.getText().toString()));
            exercise.setKollvo(Integer.parseInt(kollvo.getText().toString()));
            DBHelper helper = new DBHelper(this);
            helper.addExercise(exercise, id+1);
            Intent intent = new Intent(this,Training.class);
            intent.putExtra("enb",1);
            intent.putExtra("id",date);
            startActivity(intent);
        }
    }
}
