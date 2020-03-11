package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Auth extends AppCompatActivity {

    DBHelper help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView nameText = findViewById(R.id.nameText);
        help = new DBHelper(this);
        if(help.userExist()){
            nameText.setText(help.getUserName());
        }
        else startActivity(new Intent(this, Hello.class));
    }

    public void onClick(View view) {
        EditText pass = findViewById(R.id.editpass);
        help.signIn(pass.getText().toString());
    }
}
