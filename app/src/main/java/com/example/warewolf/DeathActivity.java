package com.example.warewolf;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeathActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.death_announcement);

        String time = getIntent().getStringExtra("day");

        TextView deadMan = findViewById(R.id.deathName);
        TextView deadRole = findViewById(R.id.roleDead);
        ImageView deadManImg = findViewById(R.id.imageViewdead);




        deadMan.setText(getIntent().getStringExtra("votedName"));

        if(time.equals("day")){
            String deadmanRole = getIntent().getStringExtra("votedRole");
            deadRole.setText(deadmanRole);
        }

    }
}
