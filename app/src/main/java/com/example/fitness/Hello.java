package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Hello extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
    }

    public void onClick(View view) {
        DBHelper helper = new DBHelper(this);
        EditText editname = findViewById(R.id.editName);
        EditText editweight = findViewById(R.id.editWeight);
        EditText editpass = findViewById(R.id.editPassword);
        helper.createUser(editname.getText().toString(),editpass.getText().toString(),Integer.parseInt(editweight.getText().toString()),this);
        helper.signIn(editpass.getText().toString());
    }
}
